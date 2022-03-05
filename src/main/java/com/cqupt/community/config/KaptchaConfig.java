package com.cqupt.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 配置Kaptcha
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/18 10:51
 */
@Configuration
public class KaptchaConfig {
    /**
     * Producer -> Kaptcha 核心接口
     * 接口中两个方法:
     * 1.创建验证码  String
     * 2.创建图片 BufferImage
     * Producer接口默认实现类 -> DefaultKaptcha
     * @return
     */
    @Bean
    public Producer kaptcharProducer(){
        Properties properties = new Properties();
        properties.setProperty("kaptcha.img.width","100");
        properties.setProperty("kaptcha.img.height","40");
        properties.setProperty("kaptcha.textproducer.font.size","32");
        properties.setProperty("kaptcha.textproducer.font.color", "0,0,0");
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYAZ");//随机生成字符
        properties.setProperty("kaptcha.textproducer.char.length", "4");//生成字符的长度
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");

        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }
}
