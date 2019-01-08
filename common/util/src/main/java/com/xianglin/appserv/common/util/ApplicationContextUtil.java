package com.xianglin.appserv.common.util;

import org.springframework.context.ApplicationContext;

/**
 * 
 * @author gengchaogang
 * @dateTime 2015年12月4日 上午10:56:20
 *
 */
public class ApplicationContextUtil {
	private static ApplicationContext applicationContext;

	public static void setApplicationContext(ApplicationContext applicationContext) {
		ApplicationContextUtil.applicationContext = applicationContext;
	}
	
	/**
	 * 获取 bean
	 * @author gengchaogang
	 * @dateTime 2015年12月4日 上午10:55:53
	 * @param name	
	 * @param requiredType	
	 * @return
	 */
	public static <T> T getBean(String name,Class<T> requiredType){
		return applicationContext.getBean(name, requiredType);
	}
}
