package com.cqupt.community.service;

import com.cqupt.community.dao.AlphaDao;
import com.cqupt.community.dao.DiscussPostMapper;
import com.cqupt.community.dao.UserMapper;
import com.cqupt.community.entity.DiscussPost;
import com.cqupt.community.entity.User;
import com.cqupt.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/14 17:51
 *
 */
@Service
//@Scope("prototype")  //非单例，每次访问这个 Bean 都会创建新的实例
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public AlphaService(){
        System.out.println("实例化 AlphaService");
    }

    @PostConstruct  //在构造器之后调用
    public void init(){
        System.out.println("初始化 AlphaService");
    }

    @PreDestroy
    public void destory(){
        System.out.println("销毁 AlphaService");
    }

    //模拟实现查询业务,体会 Service 调用 Dao
    public String find(){
        return alphaDao.select();
    }

    /**
     * Spring声明式事务测试
     * @return
     */
    // propagation常用常量
    // REQUIRED: 支持当前事务(外部事务),如果不存在则创建新事务. A调用B，A自己有当前事务，B就直接用，没有的话B创建一个新事务.
    // REQUIRES_NEW: 创建一个新事务,并且暂停当前事务(外部事务).
    // NESTED: 如果当前存在事务(外部事务),则嵌套在该事务中执行(独立的提交和回滚),否则就会REQUIRED一样.
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Object save1() {
        // 新增用户
        User user = new User();
        user.setUsername("alpha");
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5Encode("123" + user.getSalt()));
        user.setEmail("alpha@126.com");
        user.setHeaderUrl("http://image.nowcoder.com/head/88t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 新增帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("Hello");
        post.setContent("新人报道!");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);

        //造错误,测试是否有回滚
        Integer.valueOf("abc");

        return "ok";
    }

    /**
     * Spring编程式事务测试
     * @return
     */
    public Object save2() {
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        return transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                // 新增用户
                User user = new User();
                user.setUsername("beta");
                user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
                user.setPassword(CommunityUtil.md5Encode("123" + user.getSalt()));
                user.setEmail("beta@qq.com");
                user.setHeaderUrl("http://image.nowcoder.com/head/999t.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);

                // 新增帖子
                DiscussPost post = new DiscussPost();
                post.setUserId(user.getId());
                post.setTitle("你好");
                post.setContent("我是新人!");
                post.setCreateTime(new Date());
                discussPostMapper.insertDiscussPost(post);

                Integer.valueOf("abc");

                return "ok";
            }
        });
    }

}
