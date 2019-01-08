package com.xianglin.appserv.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xianglin.appserv.common.util.HttpUtils;
import com.xianglin.appserv.common.util.RSAUtil;
import com.xianglin.appserv.common.util.SHAUtil;
import com.xianglin.appserv.common.util.TranUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanglei on 2017/10/24.
 */
public class Test {

    /**
     * 签名算法
     */
    public static final String channelKey = "82d4fb75221bba9e3537e239218e09da";
    public static final String channelNo = "20171020160344008344";
    private static final String priKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK0mSa6KpLE07u5oJYZXIBJRBkQUp6NaScafdwMsrYL5sTvGoDic8Xp7JkQL1U4GLFU/WFBrNpV1C/8s/Uyh9fyjOcL+27QVlZEiLczJ/WvVkcwG80yc6uwU4cTcYFGv7LjCVjcpSYDo9iO85XzkA0mqooIwpuKJdCB7lYdcPFAjAgMBAAECgYAkFE5WKF2Y70a6Nsla7Nyp9Ggx13v29eFZmfDaoHynRhBAPGs2YL2QoAihNTYFRBQIz2I/n5eIHROa4G0HgtgWrHgpJF7deuxKaMnP7ZXgZDWj9otpEIOVdNc7O9AA8Rv9MH300TCY3UVWztEvRMn0qY9vo0VgZkjLOG2SJhOLQQJBAOXWfj6/PyUahruIgbaW3YlsjLU4CnB1/qR4gPY4hxoIHtqgfTK/Vtk8A/xMuiLeOozv4LfsEGe54EYXPNZA09UCQQDA2+PIzy/rWTJ/djBkbQriTZYS586fZask41cJyaursj4wYv43Ob3SkprzSXZOsbRRXGAIm47gUWShAerGECgXAkAJbw5rgOoUmcwh8m5wkiyxsVTtM0mqaA11rsSMVrGk7eRmUOQSqyvjasU3G+cf+0Mn+cidhAEAYO7Fsbp7iNmpAkB5AX7KYDUQ1cZ5cw6p9ED//O5MrmVWXdQkMQBLg93SvOOIOJSI0hHoLVRLkaxJc/ZPlkYrIG9lD6yz2SAVZc3BAkEAyUsvFABH1zWZcbwAhQdd6XqFldMKhkMFaOLsb3AshTTA6d1e/cSom6rsXJgdUmN3+AoxgtvSQP6zN2vDdaBzHg==";
    private static final String code = "b0efb71ee680a45712addacdb1399359";
    public static void main(String[] args) {
        Map<String,String> paras = new HashMap<String,String>();
        String timestamp=System.currentTimeMillis()+"";
        String nonce =System.currentTimeMillis()+"";
        paras.put("trxnCode", "000002");
        paras.put("timestamp", timestamp);
        paras.put("nonce", nonce);
        paras.put("channelNo", channelNo);
//        paras.put("code",code);
        Map str=new HashMap();
        str.put("openId", "100001111");
        str.put("mobileNo", "18622222222");
        String content= JSONObject.toJSONString(str);
        String d="";
        try {
            d = RSAUtil.sign(content,priKey);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        paras.put("content", d);
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
                +"&timestamp="+timestamp+"&nonce="+nonce+"&channelNo="+channelNo+"&trxnCode=000001&code="+code;
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

        //获取授权令牌
        if(code != null){
            String base64Code = null;
            try {
                //私钥解密过程
                /*String filepath="G:/tmp/";
                byte[] res=decrypt(loadPrivateKeyByStr(loadPrivateKeyByFile(filepath)), Base64.decode(code));
                String restr=new String(res);
                System.out.println("解密："+restr);
                System.out.println();*/
                base64Code = RSAUtil.decryptWithBase64(priKey,code);
                System.out.println("解密code："+base64Code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
