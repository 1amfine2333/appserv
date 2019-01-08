package com.xianglin.appserv.common.util;

import org.apache.shiro.crypto.hash.Sha512Hash;

public class EncryptUtil {
	
	public static String sha512(String source) {
		return new Sha512Hash(source).toBase64();
	}
	
	public static String sha512(String source, String salt) {
		return new Sha512Hash(source, salt).toBase64();
	}
}
