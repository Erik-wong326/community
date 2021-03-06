package com.cqupt.community;

import com.cqupt.community.dao.DiscussPostMapper;
import com.cqupt.community.dao.LoginTicketMapper;
import com.cqupt.community.dao.MessageMapper;
import com.cqupt.community.dao.UserMapper;
import com.cqupt.community.entity.DiscussPost;
import com.cqupt.community.entity.LoginTicket;
import com.cqupt.community.entity.Message;
import com.cqupt.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;


/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/15 15:14
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testSelect(){
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("lxb");
        user.setPassword("123");
        user.setSalt("qwe");
        user.setEmail("lxb@cqupt.com");
        user.setHeaderUrl("http://www.cqupt.com/lxb.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void updateUser(){
        int rows = userMapper.updateStatus(150, 1);
        System.out.println(rows);
        rows = userMapper.updateHeader(150, "http://www.cqupt.com/lxb.png2");
        System.out.println(rows);
        rows = userMapper.updatePassword(150, "1234");
        System.out.println(rows);

    }

    //discussPost??????
    @Test
    public void testSelectPosts(){
        List<DiscussPost> ret = discussPostMapper.selectDiscussPost(149, 0, 10,0);
        for (DiscussPost post : ret) {
            System.out.println(post);
        }

        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);

    }

    //????????????DAO??????
    //????????????
    @Test
    public void testInsertLoginTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("jarvis");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    //??????,????????????
    @Test
    public void testSelectLoginTicket(){
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("jarvis");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("jarvis",1);
        loginTicket = loginTicketMapper.selectByTicket("jarvis");
        System.out.println(loginTicket);
    }

    /**
     * ??????????????????
     * ??????????????????
     */
    @Test
    public void testSelectLetters() {
        //????????????
        List<Message> list = messageMapper.selectConversations(111, 0, 20);
        for (Message message : list) {
            System.out.println(message);
        }

        //??????????????????
        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        //??????????????????????????????, ?????? 111_112???????????????
        list = messageMapper.selectLetters("111_112", 0, 10);
        for (Message message : list) {
            System.out.println(message);
        }
        //?????? 111_112?????????????????????
        count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);
        //??????111_131????????? 111??????131???131(userId)?????????????????????
        count = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(count);

    }


}
