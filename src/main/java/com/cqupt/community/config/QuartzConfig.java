package com.cqupt.community.config;

import com.cqupt.community.quartz.AlphaJob;
import com.cqupt.community.quartz.PostScoreRefreshJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/3/5 19:06
 * 配置 -> 数据库 -> Quartz 访问数据库调用任务,不再访问配置文件
 */
@Configuration
public class QuartzConfig {

    // FactoryBean:用于简化Bean的实例过程.
    // 1. Spring 通过 FactoryBean 封装了 Bean 的实例化过程
    // 2. 将 FactoryBean 装配到Spring 容器.
    // 3. 将 FactoryBean 注入给其他的 Bean
    // 4. 其他的 Bean 得到的是 FactoryBean 所管理的对象实例
    // 例如:JobDetailFactoryBean 装配到了Spring容器中;
    // 在simpleTrigger 的参数中需要 JobDetail
    // 将 JobDetailFactoryBean 注入给 Bean:SimpleTriggerFactoryBean,
    // 则 simpleTrigger 就得到了 JobDetail 这个对象实例


    //Quartz测试方法1
    //配置JobDetail
//    @Bean
    public JobDetailFactoryBean alphaJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(AlphaJob.class);
        factoryBean.setName("alphaJob");
        factoryBean.setGroup("alphaJobGroup");
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }

    //Quartz测试方法2
    //配置Trigger(SimpleTriggerFactoryBean, CronTriggerFactoryBean)
    //简单Trigger(SimpleTrigger) : 例如:每十分钟执行一次
    //复杂Trigger(CronTrigger) : 例如:每个月月底凌晨两点执行任务
//    @Bean
    public SimpleTriggerFactoryBean alphaTrigger(JobDetail alphaJobDetail) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(alphaJobDetail);
        factoryBean.setName("alphaTrigger");
        factoryBean.setGroup("alphaTriggerGroup");
        factoryBean.setRepeatInterval(3000);
        factoryBean.setJobDataMap(new JobDataMap());
        return factoryBean;
    }

    // 刷新帖子分数任务
    @Bean
    public JobDetailFactoryBean postScoreRefreshJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(PostScoreRefreshJob.class);
        factoryBean.setName("postScoreRefreshJob");
        factoryBean.setGroup("communityJobGroup");
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }

    @Bean
    public SimpleTriggerFactoryBean postScoreRefreshTrigger(JobDetail postScoreRefreshJobDetail) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(postScoreRefreshJobDetail);
        factoryBean.setName("postScoreRefreshTrigger");
        factoryBean.setGroup("communityTriggerGroup");
        factoryBean.setRepeatInterval(1000 * 60 * 5);//5分钟
        factoryBean.setJobDataMap(new JobDataMap());
        return factoryBean;
    }

}

