package com.cqupt.community.service;

import com.cqupt.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

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
}
