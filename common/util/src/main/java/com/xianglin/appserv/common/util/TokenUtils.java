package com.xianglin.appserv.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Describe :
 * Created by Lister<18627416004@163.com/> on 2017/10/25 10:42.
 * Update reason :
 */
public class TokenUtils {
    private static final Logger logger = LoggerFactory.getLogger(TokenUtils.class);

    /**
     * 渠道授权
     * @return
     */
    public static String authorize(){
        String channelKey = SysConfigUtil.getStr("interest_channelKey");
        String channelNo = SysConfigUtil.getStr("interest_channelNo");
        String priKey = SysConfigUtil.getStr("interest_priKey");
        String interestPostUrl = SysConfigUtil.getStr("interest_postUrl");
        String resp= null;
        try {
            System.out.println("请求:"+"authorize");
            Map<String,String> paras = new HashMap<String,String>();
            String timestamp=timestamp();
            String nonce =getUUID();
            paras.put("trxnCode", "000001");
            paras.put("timestamp", timestamp);
            paras.put("nonce", nonce);
            paras.put("channelNo", channelNo);
            String d = RSAUtil.sign(channelKey,priKey);
            Map str=new HashMap();
            str.put("channelKey", d);
            String content= JSONObject.toJSONString(str);
            paras.put("content", content);
            System.out.println("POST参数:");
            System.out.println(content);
            String s = TranUtil.formatSignMsg(paras,false,true);
            System.out.println("排序后的URL"+s);
            String sign = SHAUtil.Sha1(s);
            System.out.println("未Rsa加密的sign:"+sign);
            String RsaSign = RSAUtil.sign(sign,priKey);
            System.out.println("Rsa加密的sign:"+RsaSign);
            String postUrl = interestPostUrl+"/oauth/authorize?sign="+RsaSign
                    +"&timestamp="+timestamp+"&nonce="+nonce+"&channelNo="+channelNo+"&trxnCode=000001";
            System.out.println(postUrl);
            resp = HttpUtils.executePost2(postUrl,content,50000);
            logger.info("authorize: "+resp);
        } catch (Exception e) {
            logger.error("authorize", e);
        }
        return resp;
    }

    /**
     *  获取授权令牌
     * @param code
     * @param phone
     * @param openId
     * @return
     */
    public static String  accessToken(String code,String phone,String openId){
        String channelKey = SysConfigUtil.getStr("interest_channelKey");
        String channelNo = SysConfigUtil.getStr("interest_channelNo");
        String priKey = SysConfigUtil.getStr("interest_priKey");
        String interestPostUrl = SysConfigUtil.getStr("interest_postUrl");
        String resp= null;
        try {
            System.out.println("请求:"+"accessToken");
            if(StringUtils.isNotEmpty(code)){
                String timestamp=timestamp();
                String nonce =getUUID();
                Map<String,String> paras = new HashMap<String,String>();
                paras.put("trxnCode", "000002");
                paras.put("timestamp", timestamp);
                paras.put("nonce", nonce);
                paras.put("channelNo", channelNo);
                paras.put("code",code);
                Map str=new HashMap();
                str.put("mobileNo", phone);
                str.put("openId",openId);
                String content= JSONObject.toJSONString(str);
                paras.put("content", content);
                System.out.println("POST参数:");
                System.out.println(content);
                String s = TranUtil.formatSignMsg(paras,false,true);
                System.out.println(s);
                String sign = SHAUtil.Sha1(s);
                System.out.println("未Rsa加密的sign:"+sign);
                String RsaSign = RSAUtil.sign(sign,priKey);
                System.out.println("Rsa加密的sign:"+RsaSign);
                String postUrl = interestPostUrl+"/oauth/accessToken?sign="+RsaSign
                        +"&timestamp="+timestamp+"&nonce="+nonce+"&channelNo="+channelNo+"&trxnCode=000002"+"&code="+code;
                System.out.println("请求URL:"+postUrl);
                resp = HttpUtils.executePost2(postUrl,content,50000);
                logger.info("accessToken: "+resp); 
            }
        } catch (Exception e) {
            logger.error("accessToken", e);
        }
        return resp;
    }

    /**
     * 刷新授权令牌
     * @param accessToken
     * @param refreshToken
     * @param openId
     * @return
     */
    public static String refreshToken(String accessToken,String refreshToken,String openId){
        String channelKey = SysConfigUtil.getStr("interest_channelKey");
        String channelNo = SysConfigUtil.getStr("interest_channelNo");
        String priKey = SysConfigUtil.getStr("interest_priKey");
        String interestPostUrl = SysConfigUtil.getStr("interest_postUrl");
        String resp = null;
        try {
            System.out.println("请求:"+"refreshToken");
            if(StringUtils.isNotEmpty(accessToken) && StringUtils.isNotEmpty(refreshToken) && StringUtils.isNotEmpty(openId)){
                String timestamp=timestamp();
                String nonce =getUUID();
                Map<String,String> paras = new HashMap<String,String>();
                paras.put("trxnCode", "000003");
                paras.put("timestamp", timestamp);
                paras.put("nonce", nonce);
                paras.put("channelNo", channelNo);
                paras.put("refreshToken",refreshToken);
                Map str=new HashMap();
                str.put("refreshToken", refreshToken);
                str.put("accessToken",accessToken);
                str.put("openId",openId);
                String content= JSONObject.toJSONString(str);
                paras.put("content", content);
                System.out.println("POST参数:");
                System.out.println(content);
                String s = TranUtil.formatSignMsg(paras,false,true);
                System.out.println(s);
                String sign = SHAUtil.Sha1(s);
                System.out.println("未Rsa加密的sign:"+sign);
                String RsaSign = RSAUtil.sign(sign,priKey);
                System.out.println("Rsa加密的sign:"+RsaSign);
                String postUrl = interestPostUrl+"/oauth/refreshToken?sign="+RsaSign
                        +"&timestamp="+timestamp+"&nonce="+nonce+"&channelNo="+channelNo+"&trxnCode=000003"+"&refreshToken="+refreshToken;
                System.out.println("请求URL:"+postUrl);
                resp= HttpUtils.executePost2(postUrl,content,50000);
                logger.info("refreshToken: "+resp);
            }
        } catch (Exception e) {
            logger.error("refreshToken", e);
        }
        return resp;
    }
    
    public static String requestCurrency(String tranCode, String pas,String accessToken,String key){
        String channelKey = SysConfigUtil.getStr("interest_channelKey");
        String channelNo = SysConfigUtil.getStr("interest_channelNo");
        String priKey = SysConfigUtil.getStr("interest_priKey");
        String interestPostUrl = SysConfigUtil.getStr("interest_postUrl");
        String resp = null;
        try {
            System.out.println("请求:"+"requestCurrency");
            if(StringUtils.isNotEmpty(tranCode) && StringUtils.isNotEmpty(key)){
                Map<String,String> paras = new HashMap<String,String>();
                String timestamp=timestamp();
                String nonce =getUUID();
                paras.put("trxnCode", tranCode);
                paras.put("timestamp", timestamp);
                paras.put("nonce", nonce);
                paras.put("channelNo", channelNo);
                paras.put("accessToken",accessToken);
                paras.put("content", pas);
                System.out.println("POST参数:");
                System.out.println(pas);
                String s = TranUtil.formatSignMsg(paras,false,true);
                System.out.println("排序后的URL"+s);
                String sign = SHAUtil.Sha1(s);
                System.out.println("未Rsa加密的sign:"+sign);
                String RsaSign = RSAUtil.sign(sign,priKey);
                System.out.println("Rsa加密的sign:"+RsaSign);
                String postUrl = interestPostUrl+"/api/business?sign="+RsaSign
                        +"&timestamp="+timestamp+"&nonce="+nonce+"&channelNo="+channelNo+"&trxnCode="+tranCode+"&accessToken="+accessToken;
                System.out.println(postUrl);
                String postBody = null;
                if(tranCode.equals("100102") || tranCode.equals("100106")){
                    postBody = pas;
                }else{
                    postBody=AESUtils.encrypt(pas, key);  
                }
                System.out.println("postBody: "+postBody);
                resp = HttpUtils.executePost2(postUrl,postBody,50000);
            }
            logger.info("requestCurrency: "+resp);
        } catch (Exception e) {
            logger.error("requestCurrency", e);
        }
        return resp;
    }
    
    
    public static String decryptCode(String code){
        String channelKey = SysConfigUtil.getStr("interest_channelKey");
        String channelNo = SysConfigUtil.getStr("interest_channelNo");
        String priKey = SysConfigUtil.getStr("interest_priKey");
        String interestPostUrl = SysConfigUtil.getStr("interest_postUrl");
        String decryptCode = null ;
        if(code != null){
            try {
                decryptCode = RSAUtil.decryptWithBase64(priKey,code);
                System.out.println("解密code："+decryptCode);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return decryptCode;
    }
    private static String timestamp(){
        String timestamp=(System.currentTimeMillis()+(18*60+39)*1000)+"";
        return  timestamp;
    }
    
    
    private static String getUUID(){
        String s = UUID.randomUUID().toString().replace("-","");
        s = s.substring(0,20);
        System.out.println(s);
        return s;
    }
    
}
