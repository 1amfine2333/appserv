/**
 * 
 */
package com.xianglin.appserv;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 
 * 
 * @author zhangyong 2016年9月21日下午2:09:21
 */
public class PKCS7Encoder {
	static Charset CHARSET = StandardCharsets.UTF_8;
	static int BLOCK_SIZE = 32;

	static byte[] encode(int count) {
		int amountToPad = BLOCK_SIZE - count % BLOCK_SIZE;
		if (amountToPad == 0) {
			amountToPad = BLOCK_SIZE;
		}

		char padChr = chr(amountToPad);
		String tmp = new String();
		for (int index = 0; index < amountToPad; index++) {
			tmp = tmp + padChr;
		}
		System.out.println(tmp);
		return tmp.getBytes(CHARSET);
	}

	static byte[] decode(byte[] decrypted) {
		int pad = decrypted[(decrypted.length - 1)];
		if ((pad < 1) || (pad > 32)) {
			pad = 0;
		}
		return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
	}

	static char chr(int a) {
		byte target = (byte) (a & 0xFF);
		return (char) target;
	}

	public static void main(String[] args) {
//		encode(67);
		String s ="";
		System.out.println("===");
		System.out.println( 0xFF);
		System.out.println("===");
	}
}