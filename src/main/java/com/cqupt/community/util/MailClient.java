package com.cqupt.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.sql.Struct;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/17 0:46
 */
@Component
public class MailClient {
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    @Autowired
    private JavaMailSender mailSender;

    //邮件的发送人
    @Value("${spring.mail.username}")
    private String from;

    //发邮件的功能
    public void sendMail(String to,String topic,String content){
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            //利用MimeMessage的帮助类 MimeMessageHelper 构建内容
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom(from); //设置发件人
            helper.setTo(to); //设置收件人
            helper.setSubject(topic); // 设置主题
            helper.setText(content, true); // 设置内容  true:支持html文本
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送邮件失败" + e.getMessage());
        }
    }
}
