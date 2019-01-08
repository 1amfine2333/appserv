package com.xianglin.appserv.common.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.xianglin.appserv.common.util.constant.ImgInfo;

public class ImgBase64Util extends ImgUtils{
	
	/**
	 * base64图片上传
	 * @author gengchaogang
	 * @dateTime 2016年11月23日 下午5:34:03
	 * @param base64Img	base64算法压缩过的图片字符串
	 * @param fileName	图片名称
	 * @param storePath 图片存储路径（相对于总存储路径）
	 * @return
	 * @throws IOException
	 */
	public static final String uploadImg(String base64Img,String fileName,String storePath) throws IOException{
		String imgUrl = null;
		try {
			byte[] bytes = Base64.decodeBase64(base64Img);
			String fileNameSHA1 = DigestUtils.getHexSHA1(fileName);
			String filePathSHA1 = makeHashGoodDir(fileNameSHA1);
			//图片固定存储绝对路径+图片动态业务存储路径+散列
			String filePathNew = IMG_FILE_PATH + File.separator + storePath + File.separator + filePathSHA1;
			File sfilePathFile = new File(filePathNew);//目录
			if(!sfilePathFile.exists()) {  //判断服务器路径是否存储在
				if(sfilePathFile.mkdirs()) {
					LOGGER.info("创建图片保存目录成功{}",filePathNew);
				}else{
					LOGGER.info("创建图片保存目录失败{}",filePathNew);
					throw new IOException("图片上传异常");
				}
			}
			fileName = DigestUtils.getHexMd5(fileNameSHA1);
			File sfile = new File(filePathNew + File.separator + fileName+IMGTYPE);//文件=fileName+服务器图片地址
			FileUtils.writeByteArrayToFile(sfile, bytes);
			
			imgUrl = StringUtils.remove(sfile.getPath(), IMG_FILE_PATH);
			imgUrl = StringUtils.replace(imgUrl, File.separator, "/");//返回的图片URL（相对路径）
		} catch (Exception e) {
			throw new  IOException("图片上传失败");
		}
		return imgUrl;
	}
	/**
	 * 获取图片信息
	 * @author gengchaogang
	 * @dateTime 2016年11月29日 下午5:09:46
	 * @param base64Img
	 * @return
	 * @throws IOException
	 */
	public static final ImgInfo getImgName(String base64Img) throws IOException{
		ImgInfo imgInfo = new ImgInfo();
		try {
			byte[] bytes = Base64.decodeBase64(base64Img);
			imgInfo.setContent(bytes);
			String fileNameSHA1 = DigestUtils.sha1Hex(bytes);
			String fileName = DigestUtils.getHexMd5(fileNameSHA1);
			imgInfo.setFileName(fileName);
			imgInfo.setLength(bytes.length);
		} catch (Exception e) {
			throw new  IOException("图片上传失败");
		}
		return imgInfo;
	}
	
	public static void main(String[] args) throws IOException {
		File fileSrc = new File("D:\\Users\\wanglei.xianglin\\Documents\\Tencent Files\\183728136\\FileRecv\\11.txt");
		byte[] bfileSrcs = FileUtils.readFileToByteArray(fileSrc);
		System.out.println(new String(bfileSrcs));
		String base64FileSrcs = Base64.encodeBase64String(DigestUtils.sha512(new String(bfileSrcs)));
		System.out.println(base64FileSrcs);
	}
	
}
