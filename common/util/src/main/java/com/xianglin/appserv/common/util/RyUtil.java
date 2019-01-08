package com.xianglin.appserv.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import sun.applet.Main;

import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.transform.Source;

/**
 * 融云服务接口
 */
public class RyUtil {

    private static final Logger logger = LoggerFactory.getLogger(RyUtil.class);


    private static final String APPKEY = "RC-App-Key";
    private static final String NONCE = "RC-Nonce";
    private static final String TIMESTAMP = "RC-Timestamp";
    private static final String SIGNATURE = "RC-Signature";

    private static SSLContext sslCtx = null;

    static {

        try {
            sslCtx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sslCtx.init(null, new TrustManager[]{tm}, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }

        });

        HttpsURLConnection.setDefaultSSLSocketFactory(sslCtx.getSocketFactory());

    }

    // 设置body体
    public static void setBodyParameter(StringBuilder sb, HttpURLConnection conn) throws IOException {
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.writeBytes(sb.toString());
        out.flush();
        out.close();
    }

    public static HttpURLConnection CreateGetHttpConnection(String uri) throws MalformedURLException, IOException {
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(30000);
        conn.setRequestMethod("GET");
        return conn;
    }

    public static void setBodyParameter(String str, HttpURLConnection conn) throws IOException {
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.write(str.getBytes("utf-8"));
        out.flush();
        out.close();
    }

    public static HttpURLConnection CreatePostHttpConnection(String appKey, String appSecret, String uri,
                                                             String contentType) throws MalformedURLException, IOException, ProtocolException {
        String nonce = String.valueOf(Math.random() * 1000000);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        StringBuilder toSign = new StringBuilder(appSecret).append(nonce).append(timestamp);
        String sign = hexSHA1(toSign.toString());
//		uri = hostType.getStrType() + uri;
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);

        conn.setRequestProperty(APPKEY, appKey);
        conn.setRequestProperty(NONCE, nonce);
        conn.setRequestProperty(TIMESTAMP, timestamp);
        conn.setRequestProperty(SIGNATURE, sign);
        conn.setRequestProperty("Content-Type", contentType);

        return conn;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }

    public static String returnResult(HttpURLConnection conn) throws Exception, IOException {
        InputStream input = null;
        if (conn.getResponseCode() == 200) {
            input = conn.getInputStream();
        } else {
            input = conn.getErrorStream();
        }
        String result = new String(readInputStream(input), "UTF-8");
        return result;
    }

    public static String hexSHA1(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(value.getBytes("utf-8"));
            byte[] digest = md.digest();
            return byteToHexString(digest);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String byteToHexString(byte[] bytes) {
        return String.valueOf(Hex.encodeHex(bytes));
    }

    public static String requestURL(String queryURL, Map<String, String> paras) {
        String result = null;
        try {
            StringBuilder sb = new StringBuilder();
            if (paras != null && paras.size() > 0) {
                for (Map.Entry entry : paras.entrySet()) {
                    sb.append("&").append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), "utf-8"));
                }
            }
            String body = sb.toString();
            if (body.indexOf("&") == 0) {
                body = body.substring(1, body.length());
            }
            HttpURLConnection conn = CreatePostHttpConnection(PropertiesUtil.getProperty("ry.app.key"), PropertiesUtil.getProperty("ry.app.secret"), PropertiesUtil.getProperty("ry.api.url") + queryURL, "application/x-www-form-urlencoded");
            setBodyParameter(body, conn);
            result = returnResult(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /** 获取融云token
     * @param partyId
     * @param nickName
     * @param headImg
     * @return
     */
    public static String getUserToken(Long partyId, String nickName, String headImg) {
        String token = null;
        try {
            if (StringUtils.isEmpty(nickName)) {
                nickName = "xl" + partyId;
            }
            if (StringUtils.isEmpty(headImg)) {
                headImg = SysConfigUtil.getStr("default_user_headimg");
            }
            Map<String, String> paras = new HashMap<>();
            paras.put("userId", partyId + "");
            paras.put("name", nickName);
            paras.put("portraitUri", headImg);
            String result = RyUtil.requestURL("/user/getToken.json", paras);
            if (StringUtils.isNotEmpty(result)) {
                JSONObject object = JSON.parseObject(result);
                logger.info("getUserToken "+result);
                if (object.getInteger("code") == 200) {
                    token = object.getString("token");
                }
            }
        } catch (Exception e) {
            logger.error("initRyToken", e);
        }
        return token;
    }

    /** 同步用户信息
     * @param partyId
     * @param trueName 真实姓名
     * @param nickName
     * @param headImg
     * @return
     */
    public static boolean updateUserInfo(Long partyId, String trueName, String nickName, String headImg) {
        boolean flag = false;
        try {
            String name = trueName;
            if(StringUtils.isEmpty(trueName)){
                name = nickName;
                if (StringUtils.isEmpty(nickName)) {
                    name = "xl" + partyId;
                }
            }
            if (StringUtils.isEmpty(headImg)) {
                headImg = SysConfigUtil.getStr("default_user_headimg");
            }
            Map<String, String> paras = new HashMap<>();
            paras.put("userId", partyId + "");
            paras.put("name", name);
            paras.put("portraitUri", headImg);
            String result = RyUtil.requestURL("/user/refresh.json", paras);
            if (StringUtils.isNotEmpty(result)) {
                logger.info("updateUserInfo "+result);
                JSONObject object = JSON.parseObject(result);
                if (object.getInteger("code") == 200) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            logger.error("updateUserInfo", e);
        }
        return flag;
    }

    /**
     *  创建群组方法
     */
    public static Boolean create(Long partyId,String groupId,String groupName){
        boolean flag = false;
        try {
            Map<String, String> paras = new HashMap<>();
            paras.put("userId", partyId + "");
            paras.put("groupId", groupId);
            paras.put("groupName", groupName);
            String result = RyUtil.requestURL("/group/create.json", paras);
            if (StringUtils.isNotEmpty(result)) {
                logger.info("create "+result);
                JSONObject object = JSON.parseObject(result);
                if (object.getInteger("code") == 200) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            logger.error("create", e);
        }
        return flag;
    }

    /**
     * 加入群组方法
     * @throws NoSuchAlgorithmException
     */
    public static Boolean join(Long partyId,String groupId,String groupName){
        Boolean flag=false;
        try {
            Map<String, String> paras = new HashMap<>();
            paras.put("userId", partyId + "");
            paras.put("groupId", groupId);
            paras.put("groupName", groupName+"");
            String result = RyUtil.requestURL("/group/join.json", paras);
            if (StringUtils.isNotEmpty(result)) {
                logger.info("join "+result);
                JSONObject object = JSON.parseObject(result);
                if (object.getInteger("code") == 200) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            logger.error("join", e);
        }
        return flag;
    }
    
    /*
    * 退出群组方法
    * */
    public static Boolean quit(Long partyId,String groupId){
        Boolean flag=false;
        try {
            Map<String, String> paras = new HashMap<>();
            paras.put("userId", partyId + "");
            paras.put("groupId", groupId);
            String result = RyUtil.requestURL("/group/quit.json", paras);
            if (StringUtils.isNotEmpty(result)) {
                logger.info("quit "+result);
                JSONObject object = JSON.parseObject(result);
                if (object.getInteger("code") == 200) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            logger.error("quit", e);
        }
        return flag; 
    }
    
    /**
     * 刷新群组信息方法
     * */
    public static Boolean refresh(String groupId,String groupName){
        Boolean flag=false;
        try {
            Map<String, String> paras = new HashMap<>();
            paras.put("groupId", groupId);
            paras.put("groupName", groupName+"");
            String result = RyUtil.requestURL("/group/refresh.json", paras);
            if (StringUtils.isNotEmpty(result)) {
                logger.info("refresh "+result);
                JSONObject object = JSON.parseObject(result);
                if (object.getInteger("code") == 200) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            logger.error("refresh", e);
        }
        return flag;
    }

    /*
   * 解散群
   * */
    public static Boolean dismiss(Long partyId,String groupId){
        Boolean flag=false;
        try {
            Map<String, String> paras = new HashMap<>();
            paras.put("userId", partyId + "");
            paras.put("groupId", groupId);
            String result = RyUtil.requestURL("/group/dismiss.json", paras);
            if (StringUtils.isNotEmpty(result)) {
                logger.info("quit "+result);
                JSONObject object = JSON.parseObject(result);
                if (object.getInteger("code") == 200) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            logger.error("quit", e);
        }
        return flag;
    }

    /**
     * 查询群成员方法
     */
    public static String query(String groupId){
        String partyIds = null;
        try {
            Map<String, String> paras = new HashMap<>();
            paras.put("groupId", groupId);
            String result = RyUtil.requestURL("/group/user/query.json", paras);
            if (StringUtils.isNotEmpty(result)) {
                logger.info("refresh " + result);
                JSONObject object = JSON.parseObject(result);
                if (object.getInteger("code") == 200) {
                    String users = object.getString("users");
                    JSONArray jsonArray = JSONArray.parseArray(users);
                    if (jsonArray.size() > 0) {
                        // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                        for (int i = 0; i < jsonArray.size(); i++) {
                            jsonArray.get(i).toString();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String partyId=jsonObject.get("id").toString();
                            if(StringUtils.isEmpty(partyIds)){
                                partyIds = partyId;
                            }else{
                                partyIds =partyIds + "," + partyId; 
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("refresh", e);
        }
        return partyIds;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
//		System.out.println(11);
//		Map<String,String> paras = new HashMap<>();
//		paras.put("userId","7000174");
//		paras.put("name","风格方法");
//		paras.put("portraitUri","https://appfile-dev.xianglin.cn/file/109884");
//		String result = requestURL("/user/getToken.json",paras);
//		System.out.println(result);

        String r = MD5.encode("123456");
        System.out.println(r);
        UUID uuid = UUID.randomUUID();
        System.out.println (uuid.toString());
        
       
                
    }
}
