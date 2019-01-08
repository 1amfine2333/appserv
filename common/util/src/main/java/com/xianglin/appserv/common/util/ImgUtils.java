package com.xianglin.appserv.common.util;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImgUtils {
	static final Logger LOGGER = LoggerFactory.getLogger(ImgUtils.class);
	
	public static final String IMGTYPE = ".png";  //图片相对路径
	static final String IMG_FILE_PATH = SysConfigUtil.getStr("IMG_FILE_PATH","D:");  //绝对路径
	
	/**
	 * 根据SHA-1后的文件名字创建3级目录路径
	 * @author gengchaogang
	 * @dateTime 2016年3月30日 下午5:06:50
	 * @param fileNameSHA
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	static String makeHashGoodDir(String fileNameSHA) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(fileNameSHA.substring(0, 2)).append(File.separator).append(fileNameSHA.substring(2, 4))
		.append(File.separator).append(fileNameSHA.substring(4, 6));
		return stringBuilder.toString();
	}
	
}
