package com.xianglin.appserv.common.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
/**
 * 
 * 
 * 
 * @author zhangyong 2016年8月15日下午5:22:51
 */
public class PropertiesUtil {
	/** logger */
	public static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	private static Properties properties = null;
	
	static  { 
			String COMMON_PROPERTIES_FILE="common.properties";
			properties = new Properties();
			Resource resource = new ClassPathResource(COMMON_PROPERTIES_FILE);
			try {
				Reader r = new InputStreamReader(resource.getInputStream(), "utf-8");
				properties.load(r);
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}  
	}
	public static String getProperty(String key){ 
		return properties.getProperty(key);
		
	}
	public static Integer getInteger(String key){
		String value = properties.getProperty(key);
		if(NumberUtils.isNumber(value)){
			return Integer.valueOf(value);
		}else{
			return 0;
		}
	}
	public static Double getDoublel(String key){
		String value = properties.getProperty(key);
		if(NumberUtils.isNumber(value)){
			return Double.valueOf(value);
		}else{
			return 0.00;
		}
	}
	public static void main(String[] args) {
		System.out.println(PropertiesUtil.getProperty("wx.corpsecret"));
	}
	
}
