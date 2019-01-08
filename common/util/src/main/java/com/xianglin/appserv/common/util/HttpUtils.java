package com.xianglin.appserv.common.util;

import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    private static final int connectTimeOut = 10000;
    private static final int readTimeOut = 10000;
    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";

    public static String executeGet(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse resp = httpclient.execute(httpget);
            return EntityUtils.toString(resp.getEntity());
        } catch (Exception e) {
            logger.error("Failed to connect to url ", e);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
            }
        }
        return "";
    }

    public static String executePost2(String url,String countent,int timeout){
        String result = "";
        HttpPost post = null;
        CloseableHttpClient client = null;
        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();//设置请求和传输超时时间
            post = new HttpPost(url);
            post.setConfig(requestConfig);
            client = HttpClients.createDefault();
            StringEntity entity = new StringEntity(countent);
            post.setEntity(entity);;
            HttpResponse response = client.execute(post);
            InputStream is = response.getEntity().getContent();
            result = inStream2String(is);
        } catch (Exception e) {
            logger.error("Failed to connect to url ,{}", e, url);
        } finally{
            post.releaseConnection();
            IOUtils.closeQuietly(client);
        }
        return result;

    }

    public static String executePost(String url, Map<String, String> param, int timeout) {
        String result = "";
        HttpPost post = null;
        CloseableHttpClient client = null;
        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();//设置请求和传输超时时间
            post = new HttpPost(url);
            post.setConfig(requestConfig);
            client = HttpClients.createDefault();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Entry<String, String> entry : param.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry
                        .getValue()));
            }
            HttpEntity formEntity = new UrlEncodedFormEntity(params);
            post.setEntity(formEntity);
            HttpResponse response = client.execute(post);
            InputStream is = response.getEntity().getContent();
            result = inStream2String(is);
        } catch (Exception e) {
            logger.error("Failed to connect to url ,{}", e, url);
        } finally {
            post.releaseConnection();
            IOUtils.closeQuietly(client);
        }
        return result;
    }

    public static String executePost(String url, Map<String, ? extends Object> param) {
        String result = "";
        try {
            HttpPost post = new HttpPost(url);
            CloseableHttpClient client = HttpClients.createDefault();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Entry<String, ? extends Object> entry : param.entrySet()) {
                if (entry.getValue() != null) {
                    params.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry
                            .getValue())));
                }
            }
            HttpEntity formEntity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(formEntity);
            HttpResponse response = client.execute(post);
            InputStream is = response.getEntity().getContent();
            result = inStream2String(is);
        } catch (Exception e) {
            logger.error("Failed to connect to url ,{}", e, url);
        }
        return result;
    }

    // 将输入流转换成字符串
    private static String inStream2String(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = is.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        return new String(baos.toByteArray());
    }


    /**
     * 发送https请求
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param param     提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpsRequest(String requestUrl,
                                          String requestMethod, Map param) {
        long s1 = System.currentTimeMillis();
        JSONObject jsonObject = null;
        HttpsURLConnection conn = null;
        try {
            String params = "";
            if (!(param == null || param.size() == 0)) {
                params = JSONObject.toJSONString(param);
            }

            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }
            }};
            // 目前针对SUN JVM,如果在IBM JVM下运行会报错，需要进行相应的调整
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setConnectTimeout(connectTimeOut);
            conn.setReadTimeout(readTimeOut);
            // 当outputStr不为null时向输出流写数据
            if (!"".equals(params)) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(params.getBytes("UTF-8"));
                outputStream.close();
            }

            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = (JSONObject) JSONObject.parse((buffer).toString());
        } catch (ConnectException ce) {
            logger.info("连接超时：{}" + ce.getMessage());
        } catch (Exception e) {
            logger.info("https请求异常：{}" + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }

            } catch (Exception e) {
            }
        }
        long s2 = System.currentTimeMillis();
        logger.info("--httpsRequest time {}--", s2 - s1);
        return jsonObject;
    }

    public static byte[] httpsImgDownload(String requestUrl) {
        long s1 = System.currentTimeMillis();
        HttpsURLConnection conn = null;
        try {
            logger.debug("download https img {}", requestUrl);
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }
            }};
            // 目前针对SUN JVM,如果在IBM JVM下运行会报错，需要进行相应的调整
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(connectTimeOut);
            conn.setReadTimeout(readTimeOut);

            // 从输入流读取返回内容
            InputStream input = conn.getInputStream();

            byte[] buffer = new byte[1024];
            int len;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            while ((len = input.read(buffer, 0, 1024)) > 0) {
                bout.write(buffer, 0, len);
            }
            //得到网络资源的字节数组,并写入文件
            bout.close();
            input.close();
            // 释放资源
            conn.disconnect();
            return bout.toByteArray();
        } catch (ConnectException ce) {
            logger.info("连接超时：{}" + ce.getMessage());
        } catch (Exception e) {
            logger.info("https请求异常：{}" + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }

            } catch (Exception e) {
            }
        }
        long s2 = System.currentTimeMillis();
        logger.info("--httpsRequest time {}--", s2 - s1);
        return null;
    }

    public static byte[] httpImgDownload(String requestUrl) {
        long s1 = System.currentTimeMillis();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            logger.debug("download img {}", requestUrl);
            HttpGet httpget = new HttpGet(requestUrl);
            CloseableHttpResponse resp = httpclient.execute(httpget);
            InputStream input = resp.getEntity().getContent();
            byte[] buffer = new byte[1024];
            int len;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            while ((len = input.read(buffer, 0, 1024)) > 0) {
                bout.write(buffer, 0, len);
            }
            bout.close();
            input.close();
            return bout.toByteArray();
        } catch (Exception e) {
            logger.info("https请求异常：{}" + e.getMessage());
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
            }
        }
        long s2 = System.currentTimeMillis();
        logger.info("--httpsRequest time {}--", s2 - s1);
        return null;
    }

    /**
     * 文件上传
     *
     * @param requestUrl
     * @param file
     * @return
     */
    public static String httpImgUpload(String requestUrl, File file) {
        long s1 = System.currentTimeMillis();
        String result = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            logger.debug("httpImgUpload img {}", requestUrl);
//            File file = new File(file, ContentType.DEFAULT_BINARY);
            HttpPost post = new HttpPost(requestUrl);
            FileBody fileBody = new FileBody(file);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("upfile", fileBody);
            HttpEntity entity = builder.build();
            post.setEntity(entity);
            HttpResponse response = httpclient.execute(post);
            long s2 = System.currentTimeMillis();
            logger.info("--httpImgUpload time {}--", s2 - s1);
            result = inStream2String(response.getEntity().getContent());
        } catch (Exception e) {
            logger.warn("httpImgUpload ", e);
        }
        return result;
    }

    public static String requestPostXml(String requestUrl, String xmlData) {
        return requestData(requestUrl, METHOD_POST, "text/xml", xmlData);
    }

    public static String requestData(String requestUrl,
                                     String requestMethod, String contentType, String data) {
        long s1 = System.currentTimeMillis();
        HttpsURLConnection conn = null;
        String returnString = null;

        try {

            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom()
                    .useProtocol("TLSv1")
                    .build();
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    //  Auto-generated method stub
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    //  Auto-generated method stub

                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    // Auto-generated method stub

                }
            }};
            // 从上述SSLContext对象中得到SSLSocketFactory对象

            SSLSocketFactory ssf = sslcontext.getSocketFactory();

            URL url = new URL(requestUrl);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setConnectTimeout(connectTimeOut);
            conn.setReadTimeout(readTimeOut);
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("ContentType", "utf-8");
            // 当outputStr不为null时向输出流写数据
            if (!"".equals(data)) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(data.getBytes("UTF-8"));
                outputStream.close();
            }

            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            returnString = buffer.toString();
        } catch (ConnectException ce) {
            logger.info("连接超时：{}" + ce.getMessage());
        } catch (Exception e) {
            logger.info("https请求异常：{}" + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }

            } catch (Exception e) {
            }
        }
        long s2 = System.currentTimeMillis();
        logger.info("--httpsRequest time {}--", s2 - s1);
        return returnString;
    }

    /**
     * 发送https请求
     *
     * @param requestUrl 请求地址
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpsGetRequest(String requestUrl) {
        return httpsRequest(requestUrl, "GET", null);
    }

    /**
     * 发送https请求
     *
     * @param requestUrl 请求地址
     * @param param  提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpsPostRequest(String requestUrl, Map param) {
        return httpsRequest(requestUrl, "POST", param);
    }

    public static void main(String[] args) {
//        System.out.println(executeGet("http://www.baidu.com"));
        String jsonObj = httpImgUpload("https://appfile.xianglin.cn/file/upload",new File("D:/123.jpg"));
    }
}
