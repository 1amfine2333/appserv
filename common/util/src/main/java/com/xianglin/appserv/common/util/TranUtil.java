package com.xianglin.appserv.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TranUtil {
	Logger logger = LoggerFactory.getLogger(getClass());

	private static final String EQUAL_FLAG = "=";
	private static final String APPEND_FLAG = "&";
	
	public static void main(String[] args){
		List<String> a= new ArrayList<String>();
		
		for(int i=0;i<20;i++){
			a.add(null);
		}
		
		a.set(1, "1adsd");
		a.set(10, "10adsd");
		a.set(7, "7adsd");

		a.add("-1adsd");

		System.out.println(a.size());
		
		for(int i=0;i<a.size();i++){
			System.out.println(a.get(i));
		}
	}
	
	public static boolean check(String content,Map<String,String> para,String sign,String publicKey){
		
		para.put("content", content);
		
		String s =formatSignMsg(para,false,true);
		
		String s1 = SHAUtil.Sha1(s);
		System.out.println("sha摘要后，未进行rsa加密前的sign值："+s1);;
		return RSAUtil.doCheck(s1, sign, publicKey, "UTF-8");
		
//		if(sign.equals(s1)){
//			return true;
//		}else 
//		{
//			return false;
//		}
	}
	
   public static String gcheck(String content,Map<String,String> para){
		
		para.put("content", content);
		
		String s =formatSignMsg(para,false,true);
		
		String s1 = SHAUtil.Sha1(s);
		
		return s1;
		
//		if(sign.equals(s1)){
//			return true;
//		}else 
//		{
//			return false;
//		}
   }
	public static String formatSignMsg(Map<String, String> paraMap,boolean urlencode,boolean keyLowerCase) {

		StringBuffer buff = new StringBuffer();
		List<Map.Entry<String, String>> keyList = new ArrayList<Map.Entry<String, String>>(paraMap.entrySet());

		Collections.sort(keyList,new Comparator<Map.Entry<String, String>>() {
					@Override
					public int compare(Map.Entry<String, String> src,Map.Entry<String, String> tar) {
						return (src.getKey()).toString().compareTo(tar.getKey());
					}
				});

		for (int i = 0; i < keyList.size(); i++) {
			Map.Entry<String, String> item = keyList.get(i);
			String key = item.getKey();
			String val = item.getValue();
			if (!StringUtils.isEmpty(key)) {
				
				if (urlencode) {
					try {
						val = URLEncoder.encode(val, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						
						e.printStackTrace();
						return null;
					}

				}
				buff.append(keyLowerCase?key.toLowerCase():key);
				buff.append(EQUAL_FLAG);
				buff.append(val);
				buff.append(APPEND_FLAG);

			}
		}

		if (buff.length() > 0) {
			return buff.substring(0, buff.length() - 1);
		}else return null;
	}
}
