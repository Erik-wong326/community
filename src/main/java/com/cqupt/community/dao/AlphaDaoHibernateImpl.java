package com.cqupt.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/14 17:29
 * 测试1
 * 访问数据库的接口实现
 * 用于演示Spring 容器扫描 Bean
 *
 * 场景3：有的地方需要使用 Mybatis;有的地方需要使用 Hibernate
 * 此时只要  @Repository("alphaHibernate") 自定义 Bean 名
 * 在Spring 容器中采用 alphaHibernate 即可识别
 */
//@Repository //测试1
@Repository("alphaHibernate")  // 场景3
public class AlphaDaoHibernateImpl implements AlphaDao{

    @Override
    public String select() {
        return "Spring容器测试";
    }
}
