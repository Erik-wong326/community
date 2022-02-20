package com.cqupt.community.service;

import com.cqupt.community.dao.LoginTicketMapper;
import com.cqupt.community.dao.UserMapper;
import com.cqupt.community.entity.LoginTicket;
import com.cqupt.community.entity.User;
import com.cqupt.community.util.CommunityConstant;
import com.cqupt.community.util.CommunityUtil;
import com.cqupt.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/15 17:40
 */
@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine; //利用 thymeleaf 的 templateEngine 生成动态网页

    @Value("${community.path.domain}")
    private String domain;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    /**
     * 注册
     * 传入 用户信息 进行注册
     * @param user
     * 返回具有多种情况
     * 如：账号不为空/密码不为空/邮箱不为空 等
     * @return
     */
    public Map<String, Object> register(User user){
        Map<String,Object> map = new HashMap<>();
        // 空值处理
        if (null == user){
            throw new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getUsername())){
            map.put("userNameMsg","账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())){
            map.put("PasswordMsg","密码不能为空");
        }
        if (StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空");
        }

        //验证账号是否唯一
        User user1 = userMapper.selectByName(user.getUsername());
        if (user1 != null){
            map.put("userNameMsg","该账号已存在");
            return map;
        }
        //验证邮箱
        user1 = userMapper.selectByEmail(user.getEmail());
        if (user1 != null){
            map.put("emailMsg","该邮箱已被注册");
            return map;
        }

        // 注册用户
        // 1.密码加密
        // 1.1 生成salt
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        // 1.2 加密: md5 + salt
        user.setPassword(CommunityUtil.md5Encode(user.getPassword() + user.getSalt()));
        // 2.其他字段设置
        user.setType(0); // 普通用户
        user.setStatus(0); // 初始状态:未激活 -> 0
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("https://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        // 3.调用insert方法加入数据库
        userMapper.insertUser(user);
        // 4.设置激活邮件
        Context context = new Context();
        // 4.1 用户邮箱
        context.setVariable("email",user.getEmail());
        // 4.2 激活链接的路径
        // http://localhost:8080/activation/101/code
        String url = domain + "/activation" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url",url);
        // 4.3 process: 生成动态网页
        String content = templateEngine.process("/mail/activation",context);
        // 5.调用方法 发送邮件
        mailClient.sendMail(user.getEmail(),"账号激活",content);

        return map; //最后返回的 map 为空则代表发送成功
    }

    /**
     * 处理激活逻辑
     * 用户点击链接传递回 userId 和 code
     * 我们对其进行处理
     * @param userId
     * @param code
     * @return
     */
    public int activation(int userId,String code){
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1){
            //重复激活
            return ACTIVATION_REPEAT;
        }else if(user.getActivationCode().equals(code)){
            //激活
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        }else {
            //激活失败
            return ACTIVATION_FAILURE;
        }
    }

    /**
     * 返回的情况有多种 -> Map
     *
     * 密码需要处理 password + md5 + salt
     * 才能与数据库中的密码对比
     * @param username  账号
     * @param password  密码
     * @param expiredSeconds 登录凭证过期时长
     * @return
     */
    public Map<String,Object> login(String username,String password,int expiredSeconds){
        Map<String, Object> map = new HashMap<>();

        //1.空值处理
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空");
            return map;
        }

        //2.验证账号
        User user = userMapper.selectByName(username);
        if (null == user){
            map.put("usernameMsg","账号不存在");
            return map;
        }

        //3.验证状态
        if (user.getStatus() == 0){
            map.put("usernameMsg","该账号未激活");
            return map;
        }
        //4.验证密码
        //密码处理: md5 + salt
        password = CommunityUtil.md5Encode(password  + user.getSalt());
        if (!user.getPassword().equals(password)){
            map.put("passwordMsg","密码不正确");
            return map;
        }

        //5.全部验证完毕 -> 登录凭证生成
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket",loginTicket.getTicket());
        return map;
    }

    /**
     * 登出
     * 更改登录凭证ticket状态 0 -> 1
     * @param ticket
     */
    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket,1);
    }

    /**
     * 查询 Ticket 的值
     * @param ticket 登录凭证
     * @return
     */
    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }
}
