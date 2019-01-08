package com.xianglin.appserv.common.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Hex;

public class DigestUtils extends org.apache.commons.codec.digest.DigestUtils {
	public static final String CHAR_SET = "UTF-8";
	
	/**
	 * 生成MD5
	 * @author gengchaogang
	 * @dateTime 2016年3月30日 下午4:58:04
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String getHexMd5(String str) throws UnsupportedEncodingException{
		return Hex.encodeHexString(getMd5Digest().digest(str.getBytes(CHAR_SET)));
	}
	/**
	 * 生成SHA-1
	 * @author gengchaogang
	 * @dateTime 2016年3月30日 下午4:58:40
	 * @param str
	 * @returns
	 * @throws UnsupportedEncodingException 
	 */
	public static String getHexSHA1(String str) throws UnsupportedEncodingException{
		return Hex.encodeHexString(getSha1Digest().digest(str.getBytes(CHAR_SET)));
	}
}
