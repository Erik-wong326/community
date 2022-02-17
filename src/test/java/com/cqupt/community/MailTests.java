package com.cqupt.community;

import com.cqupt.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/17 0:54
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine; //利用 thymeleaf 的 templateEngine 生成动态网页

    @Test
    public void testTextMail(){
        mailClient.sendMail("315701437@qq.com","test","hello mark");
    }

    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username","Erik");
        //process: 生成动态网页
        String content = templateEngine.process("/mail/mailDemo", context);
        System.out.println(content);//测试 是否生成了动态网页
        mailClient.sendMail("315701437@qq.com","Test2",content);
    }


}
