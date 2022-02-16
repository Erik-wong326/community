package com.cqupt.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/14 17:34
 * 场景：新技术 Mybatis 诞生,在不用替换之前 Hibernate 的代码的前提下
 * 把 Mybatis 应用于项目之中 ,替换 Hibernate
 * 目的： 体会Spring 容器自动扫描 Bean 的好处
 *
 * 解释：不需要改动主程序写的调用 Hibernate 的代码
 * 只需要在 Dao 这里新增一个业务逻辑,加上 Primary 即可.
 */
@Repository
@Primary
public class AlphaDaoMybatisImpl implements AlphaDao{
    @Override
    public String select() {
        return "I am Mybatis";
    }
}
