package com.xianglin.appserv.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.odps.udf.CodecCheck;
import com.xianglin.appserv.biz.shared.SysParaManager;
import com.xianglin.appserv.common.service.facade.app.InterestService;
import com.xianglin.appserv.common.service.facade.model.IZyFnProductInfoRespDTO;
import com.xianglin.appserv.common.service.facade.model.IZyFnProductQueryReqDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.util.*;
import com.xianglin.appserv.core.model.UserInterestInfo;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.fala.session.MapSession;
import com.xianglin.fala.session.Session;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

/**
 * Describe :
 * Created by Lister<18627416004@163.com/> on 2017/10/23 19:10.
 * Update reason :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-biz-spring.xml"})
public class InterestTest {
    /**
     * 签名算法 
     */
    public static final String channelKey = "875fe39d9ebf6da583b3e345106c3ba1";
    public static final String channelNo = "20171123192459453341";
    private static final String priKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKIc1EVHv2VNPrMFfpMVbYMwYHbOEXE5qEug1+0cdqH1zs5wilwoT0nDj8YRbDW29UmebpmwR7PI7RiaRHKRF+lCuePCHnBk+i7X1sH1uNUnBJfayLkO8dmYUtQL5FxGvtt6mK2t6aYvWySZWP65hK+bsBoIjYM0XSoRIGK+owPrAgMBAAECgYBIWlmbOjSAQAOV3wPpHY0tGjBTlhFO2Y71giodF7ETBCRzGuOF0i/hGjlXC3uiVbAQVXfDfFvkL+8yJdga0gs7SMCybEap6jY9UJPZTaltj0/COMzcilyZx3kvkvTSUszidZV+MhCwqtSdnVPWJmiRxhobcBQUDevhsD8OvdRVUQJBAM7VYIxp6rN3isDfGveQPOxiGkbEtVm+WzumB1ijnI2buq2ePlYQH8CeHnNcyaRZav22IE3LtgE02X0Qf4Uz0d0CQQDIpgMR6rs2u+bPETEtAthA0df7nS7B86d/FHffjYLvJRPcZmK+c7a/ocQWdrTzAwqnkeGxzJDGa+b02qp8OKRnAkEAvrztxW9jnk9OaOEWSFj+pesuWjbLGtC957sLqUVwePK066kzyUAjWAk5AZ/+4A4J6aDMF5IV7PTk4D2Xq6oHDQJATLxw4CFNiBlEpFyG2hgEbIXUfD4Y68QrgsZpMe4E7UDY36moif3nHlLXk1/CxE2dfYilMEJz++64XVxjZy6icQJBAKP0LHUWkzySRkBxzNtDLlmKdDtn94L80FlaNvxcrJUetPY+JjuRE1Qgz44pQ/yRHRIdk7gjtjP5O/P3b5A9lVI=";

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private InterestService interestService;
    
    @Autowired
    private SysParaManager sysParaManager;

    @Before
    public void initSession(){
        Session session = new MapSession();
        session.setAttribute(SessionConstants.PARTY_ID,5500065L);
        sessionHelper.saveLocalSesson(session);
        sysParaManager.queryPara().forEach(config -> {
            SysConfigUtil.put(config.getCode(),config.getValue());
        });
    }

    public static void main(String[] args) {
        Map<String,String> paras = new HashMap<String,String>();
        String timestamp=System.currentTimeMillis()+"";
        String nonce =System.currentTimeMillis()+"";
        paras.put("trxnCode", "000001");
        paras.put("timestamp", timestamp);
        paras.put("nonce", nonce);
        paras.put("channelNo", channelNo);
        String d="";
        try {
            d = RSAUtil.sign(channelKey,priKey);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Map str=new HashMap();
        str.put("channelKey", d);
        String content= JSONObject.toJSONString(str);
        paras.put("content", content);
        System.out.println("POST参数:");
        System.out.println(content);
        String s = TranUtil.formatSignMsg(paras,false,true);
        System.out.println(s);
        String sign = SHAUtil.Sha1(s);
        System.out.println("未Rsa加密的sign:"+sign);
        String RsaSign="";
        try {
            RsaSign = RSAUtil.sign(sign,priKey);
            System.out.println("Rsa加密的sign:"+RsaSign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String postUrl = "https://directbank.zybank.com.cn:10100/fop-service/oauth/authorize?sign="+RsaSign
                +"&timestamp="+timestamp+"&nonce="+nonce+"&channelNo="+channelNo+"&trxnCode=000001";
        System.out.println(postUrl);
        Map<String,String> map = new HashMap<>();
        map.put("content", content);
        String resp= HttpUtils.executePost2(postUrl,content,1000);
        System.out.println(resp);
        String code = null;
        if(StringUtils.isNotEmpty(resp)){
            JSONObject object = JSON.parseObject(resp);
            if(object.get("errorCode").equals("000000")){
                code =  object.get("code").toString();
                System.out.println("Code:"+code);
            }
        }
        System.out.println("加密code："+code);

        //获取授权令牌
        if(code != null){
            String base64Code = null;
            try {
                base64Code = RSAUtil.decryptWithBase64(priKey,code);
                System.out.println("解密code："+base64Code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String aa= getUrl(code);
        System.out.println("获取授权令牌的URL："+aa);
    }

    /**
     * 获取授权令牌的URL
     * @param code
     * @return
     */
    public static String getUrl(String code){
        String  base64Code =code;
        String url = null;
        String timestamp=System.currentTimeMillis()+"";
        /*String d="";
        try {
            d = RSAUtil.sign(channelKey,priKey);
            base64Code = RSAUtil.decryptWithBase64(priKey,code);
            System.out.println("解密code："+base64Code);
        } catch (Exception e1) {
            e1.printStackTrace();
        }*/
        Map<String,String> paras = new HashMap<String,String>();
        paras.put("trxnCode", "000002");
        paras.put("timestamp", timestamp);
        paras.put("nonce", timestamp);
        paras.put("channelNo", channelNo);
        paras.put("code",base64Code);
        Map str=new HashMap();
        str.put("mobileNo", "18622222222");
        str.put("openId","testUser");
        String content= JSONObject.toJSONString(str);
        paras.put("content", content);
        System.out.println("POST参数:");
        System.out.println(content);
        String s = TranUtil.formatSignMsg(paras,false,true);
        System.out.println(s);
        String sign = SHAUtil.Sha1(s);
        System.out.println("未Rsa加密的sign:"+sign);
        String RsaSign="";
        try {
            RsaSign = RSAUtil.sign(sign,priKey);
            System.out.println("Rsa加密的sign:"+RsaSign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String postUrl = "https://directbank.zybank.com.cn:10100/fop-service/oauth/accessToken?sign="+RsaSign
                +"&timestamp="+timestamp+"&nonce="+timestamp+"&channelNo="+channelNo+"&trxnCode=000002"+"&code="+base64Code;
        System.out.println("请求URL:"+postUrl);
        Map<String,String> map = new HashMap<>();
        map.put("content", content);
        String resp= HttpUtils.executePost2(postUrl,content,1000);
        System.out.println("获取授权令牌:"+resp);
        return resp;

    }

    public static void main5(String[] args) {
        String resp = getUrl("15917e0981514a0fc478509085fcacd3");
        String accessToken=null;
        String expiryIn=null;
        String refreshToken=null;
        String jiemikey=null;
        Set<String> set = new HashSet<String>();
        if(StringUtils.isNotEmpty(resp)){
            JSONObject object = JSON.parseObject(resp);
            if(object.get("errorCode").equals("000000")){
                try {
                    accessToken =  object.get("accessToken").toString();
                    expiryIn =  object.get("expiryIn").toString();
                    refreshToken =  object.get("refreshToken").toString();
                    jiemikey = RSAUtil.decryptWithBase64(priKey,object.get("key").toString());
                    System.out.println("解密code："+jiemikey);
                    String scope = object.getString("scope");
                    JSONArray jsonArray = JSONArray.parseArray(scope);
                    if (jsonArray.size() > 0) {
                        // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                        for (int i = 0; i < jsonArray.size(); i++) {
                            String number =  jsonArray.get(i).toString();
                            set.add(number);
                        }
                    }
                    System.out.println("Code:"+accessToken +" expiryIn:"+expiryIn +" refreshToken:"+refreshToken+" key:"+jiemikey);
                    System.out.println(set+"set");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } /*else if(object.get("errorCode").equals("100014")){  //  授权码已过期，请重新授权 ,调第00001那个接口重新获取授权码
               String code = getAuthorizationCode();
                
            }*/
        }
        System.out.println("==============获取授权令牌："+resp);
        String respRefreshToken = refreshToken(accessToken,refreshToken,"testUser");
        System.out.println("==============刷新授权令牌："+respRefreshToken);
    }

    private static String getAuthorizationCode() {
        String code = null;
        Map<String,String> paras = new HashMap<String,String>();
        String timestamp=System.currentTimeMillis()+"";
        String nonce =System.currentTimeMillis()+"";
        paras.put("trxnCode", "000001");
        paras.put("timestamp", timestamp);
        paras.put("nonce", nonce);
        paras.put("channelNo", channelNo);
        String d="";
        try {
            d = RSAUtil.sign(channelKey,priKey);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Map str=new HashMap();
        str.put("channelKey", d);
        String content= JSONObject.toJSONString(str);
        paras.put("content", content);
        System.out.println("POST参数:");
        System.out.println(content);
        String s = TranUtil.formatSignMsg(paras,false,true);
        System.out.println(s);
        String sign = SHAUtil.Sha1(s);
        System.out.println("未Rsa加密的sign:"+sign);
        String RsaSign="";
        try {
            RsaSign = RSAUtil.sign(sign,priKey);
            System.out.println("Rsa加密的sign:"+RsaSign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String postUrl = "http://1.192.127.107:8804/fop-service/oauth/authorize?sign="+RsaSign
                +"&timestamp="+timestamp+"&nonce="+nonce+"&channelNo="+channelNo+"&trxnCode=000001";
        System.out.println(postUrl);
        Map<String,String> map = new HashMap<>();
        map.put("content", content);
        String resp= HttpUtils.executePost2(postUrl,content,1000);
        System.out.println(resp);
        if(StringUtils.isNotEmpty(resp)){
            JSONObject object = JSON.parseObject(resp);
            if(object.get("errorCode").equals("000000")){
                code =  object.get("code").toString();
                System.out.println("Code:"+code);
            }
        }
        return code;
    }

    private static String refreshToken(String accessToken,String refreshToken,String openId){
        String resp = null;
        if(accessToken != null && refreshToken != null && openId != null){
            String timestamp=System.currentTimeMillis()+"";
            Map<String,String> paras = new HashMap<String,String>();
            paras.put("trxnCode", "000003");
            paras.put("timestamp", timestamp);
            paras.put("nonce", timestamp);
            paras.put("channelNo", channelNo);
            paras.put("refreshToken",refreshToken);
            Map str=new HashMap();
            str.put("refreshToken", refreshToken);
            str.put("accessToken",accessToken);
            str.put("openId","testUser");
            String content= JSONObject.toJSONString(str);
            paras.put("content", content);
            System.out.println("POST参数:");
            System.out.println(content);
            String s = TranUtil.formatSignMsg(paras,false,true);
            System.out.println(s);
            String sign = SHAUtil.Sha1(s);
            System.out.println("未Rsa加密的sign:"+sign);
            String RsaSign="";

            try {
                RsaSign = RSAUtil.sign(sign,priKey);
                System.out.println("Rsa加密的sign:"+RsaSign);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String postUrl = "http://1.192.127.107:8804/fop-service/oauth/refreshToken?sign="+RsaSign
                    +"&timestamp="+timestamp+"&nonce="+timestamp+"&channelNo="+channelNo+"&trxnCode=000003"+"&refreshToken="+refreshToken;
            System.out.println("请求URL:"+postUrl);
            Map<String,String> map = new HashMap<>();
            map.put("content", content);
            resp= HttpUtils.executePost2(postUrl,content,1000);
            System.out.println("刷新授权令牌"+resp);
        }
        return resp;
    }

    @Test
    public  void send() {
        //3.2.6 	验证码发送接口
        String paras="{\"phone\":\"13402160951\",\"verCodeType\":\"A\"}";
        Response<String> resp = interestService.currencyIntegerestLogin("100105",paras);
        System.out.println("resp : "+resp);
        System.out.println("验证码发送接口:"+resp);
        /*String paras="{\"phone\":\"13402160951\",\"verCodeType\":\"A\"}";
        String resp = tokenUtils.requestCurrency("100105",paras,"422dfcd7f909432387fbc2a76300fbc5");
        String code = tokenUtils.decryptCode(resp);
        System.out.println("验证码发送接口:"+resp);
        System.out.println("解密后的参数:"+code);*/


        //3.2.1	客户信息查询
        /*String paras="{\"queryInd\":\"0\",\"mobileNo\":\"13402160951\"}";
        Response<String> resp = interestService.currencyIntegerestLogin("100101",paras,null);
        System.out.println("resp : "+resp);
        System.out.println("客户信息查询接口 : "+resp);*/

        //5.3	客户信息接口
        /*String paras="";
        Response<String> resp = interestService.currencyIntegerestLogin("400103",paras,null);
        System.out.println("resp : "+resp);
        System.out.println("客户信息接口 : "+resp);*/

        //5.1	基金产品信息接口
        /*String paras="{\"fundCode\":\"000719\"}";
        Response<String> resp = interestService.currencyIntegerestLogin("400101",paras,null);
        System.out.println("resp : "+resp);
        System.out.println("基金产品信息接口 : "+resp);*/

        //3.2.2	证件照扫描接口
       /* String paras="{\"image\":\"\",\"imageFlag\":\"P\",\"imageFormat\":\"BMP\"}";
        Response<String> resp = interestService.currencyIntegerestLogin("100102",paras,null);
        System.out.println("resp : "+resp);
        System.out.println("证件照扫描接口 : "+resp);*/

    }

    public static void main3(String[] args) {
        JSONObject json = new JSONObject();
        //JSONArray jsonArray = new JSONArray();
        json.put("7001025", "60.637.50-2.089.36");//JSONObject对象中添加键值对
        json.put("5190133", "61.236.00-1.418.00");
        json.put("5500268", "64.638.00-1.539.00");
        System.out.println(json.toString());
        /*jsonArray.add(json);
        System.out.println(jsonArray.toString());*/
        //将jsonarray解析
        String js=json.toString();
        String partyId= "5190133";

        /*String aa = SysConfigUtil.getStr("GF_LOANAMOUNT_REPAIDAMOUNT");
        System.out.print(aa);*/
        JSONObject object = JSON.parseObject(js);
        String p =object.getString(partyId);
        if(StringUtils.isNotEmpty(p)){
            System.out.println("aaaString="+p);
            String[] ary = p.split("-");
            String s1 = ary[0];
            String s2 = ary[1];
            System.out.println("s1 = " + s1);
            System.out.println("s2 = " + s2);

        }

    }

    @Test
    public void hCurrencyIntegerestLogin(){
        String paras="{\"phone\":\"13402160951\",\"verCodeType\":\"A\"}";
        Response<String> resp =    interestService.hCurrencyIntegerestLogin("100105",paras,"53f03ead-662a-43b8-b58f-b057fdf7aef9");
        System.out.println("验证码发送接口:"+resp);
    }
    
    @Test
    public void updateCustomerMessage(){                //秒息宝修改客户信息接口
        String content = "{\"idNo\":\"410927199203271035\",\"name\":\"侯典恒\",\"phone\":\"17698888894\"}";
        Response<String> resp = interestService.currencyIntegerestLogin("100403",content);
        System.out.println("修改客户信息:"+resp);
    }


    @Test
    public void updateCustomerMessage5(){                //秒息宝修改客户信息接口
        String content = "{\"idNo\":\"410782198801030951\",\"name\":\"曹振南\",\"phone\":\"17339032111\"}";
        Response<String> resp = interestService.currencyIntegerestLogin("100403",content);
        System.out.println("修改客户信息:"+resp);
    }
    

    @Test
    public void updateCustomerMessage1(){                //秒息宝修改客户信息接口
        String content = "{\"queryInd\":\"1\",\"idNo\":\"452624198707073576\"}";
        Response<String> resp = interestService.currencyIntegerestLogin("100101",content);
        System.out.println("修改客户信息:"+resp);
    }

    @Test
    public void updateCustomerMessage2(){                //秒息宝2类账户信息
        String content = "{\"idType\":\"I\",\"idNo\":\"420821199207221025\"}";
        Response<String> resp = interestService.currencyIntegerestLogin("100203",content);
        System.out.println("修改客户信息:"+resp);
    }

    @Test
    public void updateCustomerMessage3(){                //秒息宝2类账户信息
        String content = "{\"queryMode\":\"C\"}";
        Response<String> resp = interestService.currencyIntegerestLogin("100202",content);
        System.out.println("修改客户信息:"+resp);
    }

    @Test
    public void integerestLogin(){
        String paras="{}";
        Response<String> resp = interestService.currencyIntegerestLogin("400103",paras);
        JSONObject object = JSON.parseObject(resp.getResult());
        String income = null;
        if(object.get("errorCode").toString().equals("000000")){
            income = object.get("total_income").toString();

        }
        System.out.println("验证码发送接口:"+income);
    }
    
    @Test
    public void getFnZyProductInfo(){
        IZyFnProductQueryReqDTO zyFnProductQueryReqDTO = new IZyFnProductQueryReqDTO();
        zyFnProductQueryReqDTO.setFundCode("000719");
        Response<IZyFnProductInfoRespDTO> resp =  interestService.getFnZyProductInfo(zyFnProductQueryReqDTO);
        System.out.println("验证码发送接口:"+resp);
    }
    


}
