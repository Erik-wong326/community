package com.cqupt.community;

import com.cqupt.community.dao.AlphaDao;
import com.cqupt.community.service.AlphaService;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {
	//ApplicationContextAware获取 Spring 容器

	private ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Test
	public void testApplicationContext(){
		//测试 Spring 容器
		System.out.println(applicationContext);
		//测试 Spring 容器获取自动装配的 Bean (即管理 Bean )
		AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class); //从容器中获取 AlphaDao.class 类型的bean
		System.out.println(alphaDao.select());

		alphaDao = applicationContext.getBean("alphaHibernate",AlphaDao.class);
		System.out.println(alphaDao.select());
	}

	//测试Bean的管理方式
	@Test
	public void testBeanManagement(){
		AlphaService alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);

		//测试Spring容器管理的 Bean 是否是单例的 -> 是单例的
		//对Bean加注解 @Scope("prototype") 可以改成非单例 ,但是很少用,基本上项目都是单例
		alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
	}

	@Test
	public void testBeanConfig(){
		//Spring 容器主动获取 Bean 并使用
		SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}


	/**
	 * 依赖注入演示
	 * 例:当前的Bean 使用 AlphaDao等
	 * 目的：体会Spring容器管理Bean的方式
	 */
	@Autowired
	//希望使用的是(Hibernate)AlphaDao
	@Qualifier("alphaHibernate")
	private AlphaDao alphaDao;// 希望 Spring容器把 AlphaDao 注入给属性 alphaDao，便于我直接使用属性 alphaDao

	@Autowired
	private AlphaService alphaService;

	@Autowired
	private SimpleDateFormat simpleDateFormat;

	//依赖注入测试
	@Test
	public void testDI(){
		System.out.println(alphaDao);
		System.out.println(alphaService);
		System.out.println(simpleDateFormat);
	}

}
