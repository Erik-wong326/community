package com.cqupt.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/14 18:00
 * 场景：希望Spring容器 控制第三方写好的类
 */
@Configuration
public class AlphaConfig {

    @Bean
    public SimpleDateFormat simpleDateFormat(){
        return new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
    }
}
