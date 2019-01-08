package com.xianglin.appserv.common.util;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class SerialNumberUtil {

	public static String getDateStr() {
		Date now = new Date();
		String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(now);
		return dateStr;
	}

	public static String padding0ToStr(String str) {
		return flushLeft("0", 10L, str);
	}

	public static String flushLeft(String c, long length, String content) {
		String str = "";
		String cs = "";
		if (content.length() > length) {
			str = content;
		} else {
			for (int i = 0; i < length - content.length(); i++) {
				cs = cs + c;
			}
		}
		str = cs + content;
		return str;
	}
	
	public static String getOrderSerial(String str){
		return getDateStr()+padding0ToStr(str);
	}
	
	public static String phoneNumberEncrypt(String number){
		if(number == null || number.length() != 11){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<number.length();i++){
			if(i<=2 || i>=7){
				sb.append(number.charAt(i));
			}else{
				sb.append("*");
			}
		}
		return sb.toString();
	}
	public static void main(String[] args) {
		System.out.println(phoneNumberEncrypt("18621898968"));
		System.out.println(getOrderSerial("11"));
		System.out.println(getOrderSerial("11").length());
	}
}
