package com.cqupt.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/3/5 18:37
 */
@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPoolConfig {
}
