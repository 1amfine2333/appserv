package com.xianglin.appserv.common.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CompleteMultipartUploadResult;
import com.aliyun.oss.model.UploadFileRequest;
import com.aliyun.oss.model.UploadFileResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by wanglei on 2017/11/13.
 */
public class AliyunUtil {

    private static final Logger logger = LoggerFactory.getLogger(AliyunUtil.class);

    private static final String accessKeyId = "LTAIqBCrRyjR66Um";

    private static final String accessKeySecret = "v1RUDztVgatawBiTMOpyMHZB1VfBA6";

    public static String uploadSearchFile(File file) {
        String location = "";
        try {
            String bucketName = "xianglin001";
            String last = StringUtils.substringAfterLast(file.getName(), ".");
            String key = DigestUtils.md5Hex(new FileInputStream(file))+"-"+file.length() + "." + last;
            String endpoint = "https://oss-cn-qingdao.aliyuncs.com";
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
//            String uploadFile = "D:\\1948066ed07baa534dde70f3db6d7e61.mp4";
            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, key);
            uploadFileRequest.setUploadFile(file.getPath());
            uploadFileRequest.setTaskNum(5);
            uploadFileRequest.setEnableCheckpoint(true);
            UploadFileResult uploadResult = ossClient.uploadFile(uploadFileRequest);
            CompleteMultipartUploadResult multipartUploadResult = uploadResult.getMultipartUploadResult();
//            location = multipartUploadResult.getLocation();
            location = StringUtils.replace(multipartUploadResult.getLocation(),"xianglin001.oss-cn-qingdao.aliyuncs.com","cdn01.xianglin.cn") ;
            if(file.exists()){
                file.delete();
            }
        } catch (Throwable e) {
            logger.warn("AliyunUtil uploadImg ",e);
        }
        return location;
    }
}
