package com.cqupt.community.controller;

import com.cqupt.community.annotation.LoginRequired;
import com.cqupt.community.entity.User;
import com.cqupt.community.service.FollowService;
import com.cqupt.community.service.LikeService;
import com.cqupt.community.service.UserService;
import com.cqupt.community.util.CommunityConstant;
import com.cqupt.community.util.CommunityUtil;
import com.cqupt.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/20 15:12
 */
@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

//    @Value("${server.servlet.context-path}") //项目访问路径,可以根据需要设置,这里我没有设置
//    private String contextPath;

    @Autowired
    private UserService userService;

    //HostHolder 取当前用户的数据
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    /**
     * 账号设置页面跳转
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    /**
     * 用户头像上传
     * 浏览器上传到服务器
     * @param headerImage  头像图片
     * @param model
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        //1.图片为空值 -> 返回错误
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片!");
            return "/site/setting";
        }
        //图片非空
        //2.检查文件格式
        //2.1取图片的原始文件名
        String fileName = headerImage.getOriginalFilename();
        //2.2 取图片源文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //2.3 图片后缀为空 -> 返回错误
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确!");
            return "/site/setting";
        }

        //3.文件存储逻辑
        //3.1 生成随机文件名 -> 防止如 1.png 和 1.png 的文件重名 -> 文件覆盖,导致数据丢失
        fileName = CommunityUtil.generateUUID() + suffix;
        //3.2 确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            //3.3 利用 MultipartFile 的 transferTo方法 存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败: " + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }

        //4. 更新当前用户的头像的路径(web访问路径)
        //例: http://localhost:8080/user/header/xxx.png
        //4.1 拼接路径
        User user = hostHolder.getUser();
        String headerUrl = domain + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);

        //5.更新完成,重定向到首页
        return "redirect:/index";
    }


    /**
     * 显示头像数据
     * 服务器响应更新后的头像数据在浏览器(客户端)上
     * @param fileName  头像文件名
     * @param response  返回体
     */
    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 1.图片文件在服务器上的存放路径
        fileName = uploadPath + "/" + fileName;
        // 2.解析文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 3.服务器响应图片
        response.setContentType("image/" + suffix); //设置文件格式
        try (
                FileInputStream fis = new FileInputStream(fileName);//头像文件的输入流
                OutputStream os = response.getOutputStream(); //头像的二进制流输出
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                //循环读写头像文件
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }
    }

    /**
     * 访问(任意)用户的个人主页
     * @param userId
     * @param model
     * @return
     */
    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }

        // 用户
        model.addAttribute("user", user);
        // 点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        // 用户的关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // 用户的粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 是否已关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }
}
