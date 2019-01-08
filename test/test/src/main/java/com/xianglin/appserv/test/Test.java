/**
 * 
 */
package com.xianglin.appserv.test;

import java.text.MessageFormat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xianglin.appserv.common.util.PropertiesUtil;

/**
 * 
 * 
 * @author wanglei 2016年8月17日下午5:34:33
 */
public class Test {

	/**
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	/*	System.out.println(PropertiesUtil.getProperty("LOAN_H5_URL"));
		Gson gs = new Gson();
		gs.fromJson("1",Integer.class);*/
		
		
		
		String s="12,";
		System.out.println(s.split(",").length);
		System.out.println(MessageFormat.format("恭喜您成功邀请了{0}位好友，/r/n 获得{1}元现金", 1,100));
	}

}
