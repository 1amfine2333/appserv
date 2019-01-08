package com.xianglin.appserv.common.util; 

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.springframework.util.Assert;


/** 
 *   <B>说       明</B>:消息摘要工具类
 *
 */
public class MessageDigestUtils {
	
	/**
	 * 默认字节缓冲区大小。
	 */
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 8;
	
	/**
	 * 通过MD2算法进行消息摘要
	 * 
	 * @param input 要进行消息摘要的数据
	 * @return
	 *      字节数组形式的消息摘要
	 * @throws IllegalArgumentException 如果参数input为null或者JDK不支持MD2算法。
	 */
	public static byte[] getMD2Digest(byte[] input){
		return getMessageDigest(input, MessageDigestAlgorithm.MD2);
	}
	
	/**
	 * 通过MD2算法进行消息摘要
	 * 
	 * @param inputStream 要进行消息摘要的数据流
	 * @return
	 *      字节数组形式的消息摘要
	 * @throws IOException 如果从数据流中读数据发生错误
	 * @throws IllegalArgumentException 如果参数inputStream为null或者JDK不支持MD2算法。
	 */
	public static byte[] getMD2Digest(InputStream inputStream) throws IOException{
		return getMessageDigest(inputStream, MessageDigestAlgorithm.MD2);
	}
	
	/**
	 * 通过MD2算法进行消息摘要
	 * 
	 * @param input 要进行消息摘要的数据
	 * @return
	 *      字符串形式的消息摘要
	 * @throws IllegalArgumentException 如果参数input为null或者JDK不支持MD2算法。
	 */
	public static String getMD2StringDigest(byte[] input){
		return getStringMessageDigest(input, MessageDigestAlgorithm.MD2);
	}
	
	/**
	 * 通过MD2算法进行消息摘要
	 * 
	 * @param inputStream 要进行消息摘要的数据流
	 * @return
	 *      字符串形式的消息摘要
	 * @throws IOException 如果从数据流中读数据发生错误
	 * @throws IllegalArgumentException 如果参数inputStream为null或者JDK不支持MD2算法。
	 */
	public static String getMD2StringDigest(InputStream inputStream) throws IOException{
		return getStringMessageDigest(inputStream, MessageDigestAlgorithm.MD2);
	}
	
	/**
	 * 通过MD5算法进行消息摘要
	 * 
	 * @param input 要进行消息摘要的数据
	 * @return
	 *      字节数组形式的消息摘要
	 * @throws IllegalArgumentException 如果参数input为null或者JDK不支持MD5算法。
	 */
	public static byte[] getMD5Digest(byte[] input){
		return getMessageDigest(input, MessageDigestAlgorithm.MD5);
	}
	
	/**
	 * 通过MD5算法进行消息摘要
	 * 
	 * @param inputStream 要进行消息摘要的数据流
	 * @return
	 *      字节数组形式的消息摘要
	 * @throws IOException 如果从数据流中读数据发生错误
	 * @throws IllegalArgumentException 如果参数inputStream为null或者JDK不支持MD5算法。
	 */
	public static byte[] getMD5Digest(InputStream inputStream) throws IOException{
		return getMessageDigest(inputStream, MessageDigestAlgorithm.MD5);
	}
	
	/**
	 * 通过MD5算法进行消息摘要
	 * 
	 * @param input 要进行消息摘要的数据
	 * @return
	 *      字符串形式的消息摘要
	 * @throws IllegalArgumentException 如果参数input为null或者JDK不支持MD5算法。
	 */
	public static String getMD5StringDigest(byte[] input){
		return getStringMessageDigest(input, MessageDigestAlgorithm.MD5);
	}
	
	/**
	 * 通过MD5算法进行消息摘要
	 * 
	 * @param inputStream 要进行消息摘要的数据流
	 * @return
	 *      字符串形式的消息摘要
	 * @throws IOException 如果从数据流中读数据发生错误
	 * @throws IllegalArgumentException 如果参数inputStream为null或者JDK不支持MD5算法。
	 */
	public static String getMD5StringDigest(InputStream inputStream) throws IOException{
		return getStringMessageDigest(inputStream, MessageDigestAlgorithm.MD5);
	}
	
	/**
	 * 通过SHA-1(简称SHA)算法进行消息摘要
	 * 
	 * @param input 要进行消息摘要的数据
	 * @return
	 *      字节数组形式的消息摘要
	 * @throws IllegalArgumentException 如果参数input为null或者JDK不支持SHA-1算法。
	 */
	public static byte[] getSHADigest(byte[] input){
		return getMessageDigest(input, MessageDigestAlgorithm.SHA);
	}
	
	/**
	 * 通过SHA-1(简称SHA)算法进行消息摘要
	 * 
	 * @param inputStream 要进行消息摘要的数据流
	 * @return
	 *      字节数组形式的消息摘要
	 * @throws IOException 如果从数据流中读数据发生错误
	 * @throws IllegalArgumentException 如果参数inputStream为null或者JDK不支持SHA-1算法。
	 */
	public static byte[] getSHADigest(InputStream inputStream) throws IOException{
		return getMessageDigest(inputStream, MessageDigestAlgorithm.SHA);
	}
	
	/**
	 * 通过SHA-1(简称SHA)算法进行消息摘要
	 * 
	 * @param input 要进行消息摘要的数据
	 * @return
	 *      字符串形式的消息摘要
	 * @throws IllegalArgumentException 如果参数input为null或者JDK不支持SHA-1算法。
	 */
	public static String getSHAStringDigest(byte[] input){
		return getStringMessageDigest(input, MessageDigestAlgorithm.SHA);
	}
	
	/**
	 * 通过SHA-1(简称SHA)算法进行消息摘要
	 * 
	 * @param inputStream 要进行消息摘要的数据流
	 * @return
	 *      字符串形式的消息摘要
	 * @throws IOException 如果从数据流中读数据发生错误
	 * @throws IllegalArgumentException 如果参数inputStream为null或者JDK不支持SHA-1算法。
	 */
	public static String getSHAStringDigest(InputStream inputStream) throws IOException{
		return getStringMessageDigest(inputStream, MessageDigestAlgorithm.SHA);
	}
	
	/**
	 * 通过SHA-256算法进行消息摘要
	 * 
	 * @param input 要进行消息摘要的数据
	 * @return
	 *      字节数组形式的消息摘要
	 * @throws IllegalArgumentException 如果参数input为null或者JDK不支持SHA-256算法。
	 */
	public static byte[] getSHA256Digest(byte[] input){
		return getMessageDigest(input, MessageDigestAlgorithm.SHA256);
	}
	
	/**
	 * 通过SHA-256算法进行消息摘要
	 * 
	 * @param inputStream 要进行消息摘要的数据流
	 * @return
	 *      字节数组形式的消息摘要
	 * @throws IOException 如果从数据流中读数据发生错误
	 * @throws IllegalArgumentException 如果参数inputStream为null或者JDK不支持SHA-256算法。
	 */
	public static byte[] getSHA256Digest(InputStream inputStream) throws IOException{
		return getMessageDigest(inputStream, MessageDigestAlgorithm.SHA256);
	}
	
	/**
	 * 通过SHA-256算法进行消息摘要
	 * 
	 * @param input 要进行消息摘要的数据
	 * @return
	 *      字符串形式的消息摘要
	 * @throws IllegalArgumentException 如果参数input为null或者JDK不支持SHA-256算法。
	 */
	public static String getSHA256StringDigest(byte[] input){
		return getStringMessageDigest(input, MessageDigestAlgorithm.SHA256);
	}
	
	/**
	 * 通过SHA-256算法进行消息摘要
	 * 
	 * @param inputStream 要进行消息摘要的数据流
	 * @return
	 *      字符串形式的消息摘要
	 * @throws IOException 如果从数据流中读数据发生错误
	 * @throws IllegalArgumentException 如果参数inputStream为null或者JDK不支持SHA-256算法。
	 */
	public static String getSHA256StringDigest(InputStream inputStream) throws IOException{
		return getStringMessageDigest(inputStream, MessageDigestAlgorithm.SHA256);
	}
	
	/**
	 * 通过SHA-384算法进行消息摘要
	 * 
	 * @param input 要进行消息摘要的数据
	 * @return
	 *      字节数组形式的消息摘要
	 * @throws IllegalArgumentException 如果参数input为null或者JDK不支持SHA-384算法。
	 */
	public static byte[] getSHA384Digest(byte[] input){
		return getMessageDigest(input, MessageDigestAlgorithm.SHA384);
	}
	
	/**
	 * 通过SHA-384算法进行消息摘要
	 * 
	 * @param inputStream 要进行消息摘要的数据流
	 * @return
	 *      字节数组形式的消息摘要
	 * @throws IOException 如果从数据流中读数据发生错误
	 * @throws IllegalArgumentException 如果参数inputStream为null或者JDK不支持SHA-384算法。
	 */
	public static byte[] getSHA384Digest(InputStream inputStream) throws IOException{
		return getMessageDigest(inputStream, MessageDigestAlgorithm.SHA384);
	}
	
	/**
	 * 通过SHA-384算法进行消息摘要
	 * 
	 * @param input 要进行消息摘要的数据
	 * @return
	 *      字符串形式的消息摘要
	 * @throws IllegalArgumentException 如果参数input为null或者JDK不支持SHA-384算法。
	 */
	public static String getSHA384StringDigest(byte[] input){
		return getStringMessageDigest(input, MessageDigestAlgorithm.SHA384);
	}
	
	/**
	 * 通过SHA-384算法进行消息摘要
	 * 
	 * @param inputStream 要进行消息摘要的数据流
	 * @return
	 *      字符串形式的消息摘要
	 * @throws IOException 如果从数据流中读数据发生错误
	 * @throws IllegalArgumentException 如果参数inputStream为null或者JDK不支持SHA-384算法。
	 */
	public static String getSHA384StringDigest(InputStream inputStream) throws IOException{
		return getStringMessageDigest(inputStream, MessageDigestAlgorithm.SHA384);
	}
	
	/**
	 * 通过SHA-512算法进行消息摘要
	 * 
	 * @param input 要进行消息摘要的数据
	 * @return
	 *      字节数组形式的消息摘要
	 * @throws IllegalArgumentException 如果参数input为null或者JDK不支持SHA-512算法。
	 */
	public static byte[] getSHA512Digest(byte[] input){
		return getMessageDigest(input, MessageDigestAlgorithm.SHA512);
	}
	
	/**
	 * 通过SHA-512算法进行消息摘要
	 * 
	 * @param inputStream 要进行消息摘要的数据流
	 * @return
	 *      字节数组形式的消息摘要
	 * @throws IOException 如果从数据流中读数据发生错误
	 * @throws IllegalArgumentException 如果参数inputStream为null或者JDK不支持SHA-512算法。
	 */
	public static byte[] getSHA512Digest(InputStream inputStream) throws IOException{
		return getMessageDigest(inputStream, MessageDigestAlgorithm.SHA512);
	}
	
	/**
	 * 通过SHA-512算法进行消息摘要
	 * 
	 * @param input 要进行消息摘要的数据
	 * @return
	 *      字符串形式的消息摘要
	 * @throws IllegalArgumentException 如果参数input为null或者JDK不支持SHA-512算法。
	 */
	public static String getSHA512StringDigest(byte[] input){
		return getStringMessageDigest(input, MessageDigestAlgorithm.SHA512);
	}
	
	/**
	 * 通过SHA-512算法进行消息摘要
	 * 
	 * @param inputStream 要进行消息摘要的数据流
	 * @return
	 *      字符串形式的消息摘要
	 * @throws IOException 如果从数据流中读数据发生错误
	 * @throws IllegalArgumentException 如果参数inputStream为null或者JDK不支持SHA-512算法。
	 */
	public static String getSHA512StringDigest(InputStream inputStream) throws IOException{
		return getStringMessageDigest(inputStream, MessageDigestAlgorithm.SHA512);
	}
	
	
	/**
	 * 获取消息摘要
	 * 
	 * @param input 要进行消息摘要的数据
	 * @param algorithm 消息摘要算法 @see {@link MessageDigestAlgorithm}
	 * @return
	 *      字节数组形式的消息摘要
	 * @throws IllegalArgumentException 如果参数input为null，或者algorithm为空或不被JDK支持。
	 */
	private static byte[] getMessageDigest(byte[] input,MessageDigestAlgorithm algorithm){
		Assert.notNull(input, "要进行消息摘要的数据不能为空!");
		Assert.notNull(algorithm, "消息摘要算法不能为空!");
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm.getName());
			return messageDigest.digest(input);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 获取消息摘要
	 * 
	 * @param input 要进行消息摘要的数据
	 * @param algorithm 消息摘要算法 @see {@link MessageDigestAlgorithm}
	 * @return
	 *      字符串形式的消息摘要
	 * @throws IllegalArgumentException 如果参数input为null，或者algorithm为空或不被JDK支持。
	 */
	private static String getStringMessageDigest(byte[] input,MessageDigestAlgorithm algorithm){
		byte[] digest = getMessageDigest(input, algorithm);
		return Hex.encodeHexString(digest);
	}
	
	/**
	 * 获取消息摘要
	 * 
	 * @param inputStream 要进行消息摘要的数据流
	 * @param algorithm 消息摘要算法 @see {@link MessageDigestAlgorithm}
	 * @return
	 *      字节数组形式的消息摘要
	 * @throws IOException 如果从数据流中读数据发生错误
	 * @throws IllegalArgumentException 如果参数inputStream为null，或者algorithm为空或不被JDK支持。
	 */
	private static byte[] getMessageDigest(InputStream inputStream,MessageDigestAlgorithm algorithm) throws IOException{
		Assert.notNull(inputStream, "要进行消息摘要的数据流不能为空!");
		Assert.notNull(algorithm, "消息摘要算法不能为空!");
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm.getName());
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int len = -1;
			while((len = inputStream.read(buffer)) > -1){
				messageDigest.update(buffer, 0, len);
			}
			return messageDigest.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 获取消息摘要
	 * 
	 * @param inputStream 要进行消息摘要的数据流
	 * @param algorithm 消息摘要算法 @see {@link MessageDigestAlgorithm}
	 * @return
	 *      字符串形式的消息摘要
	 * @throws IOException 如果从数据流中读数据发生错误
	 * @throws IllegalArgumentException 如果参数inputStream为null，或者algorithm为空或不被JDK支持。
	 */
	private static String getStringMessageDigest(InputStream inputStream,MessageDigestAlgorithm algorithm) throws IOException{
		byte[] digest = getMessageDigest(inputStream, algorithm);
		return Hex.encodeHexString(digest);
	}
	
	private MessageDigestUtils() {}
	
	/** 
	 * 消息摘要算法
	 * 
	 * 这里只列举JDK6以上(包含6)支持的算法
	 */ 
	public static enum MessageDigestAlgorithm{
		
		MD2("MD2",128),
		MD5("MD5",128),
		SHA("SHA-1",160),
		SHA256("SHA-256",256),
		SHA384("SHA-384",384),
		SHA512("SHA-512",512);
		
		/**
		 * 算法名称
		 */
		private String name;
		
		/**
		 * 摘要长度(单位：比特)
		 */
		private int digestLength;
		
		private MessageDigestAlgorithm(String name,int digestLength) {
			this.name = name;
			this.digestLength = digestLength;
		}
		
		public String getName() {
			return name;
		}

		public int getDigestLength() {
			return digestLength;
		}
		
	}

}
 
