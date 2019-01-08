package com.xianglin.juhe.common.service.integration.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xianglin.appserv.common.util.HttpUtils;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.juhe.common.service.integration.JuheServiceClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/12/14
 * Time: 11:03
 */
public class JuheServiceClientImpl implements JuheServiceClient {
    private Logger logger = LoggerFactory.getLogger(JuheServiceClientImpl.class);

    @Override
    public JSONObject getMobileInfo(String mobile) {
        String url = SysConfigUtil.getStr("mobilePhoneLocation","http://apis.juhe.cn/mobile/get");
        Map<String, String> map = new HashMap<>();
        logger.info("======mobile:{}======",mobile);
        map.put("phone", mobile);
        map.put("key","b70df2549827f689183cb3548b9d339f");
        String resp = HttpUtils.executePost(url, map);
        if (StringUtils.isNotEmpty(resp)) {
            JSONObject object = JSON.parseObject(resp);
            if("200".equals(object.getString("resultcode"))){
                JSONObject result = JSON.parseObject(object.getString("result"));
                logger.debug("{}",result);
                return result;
            }

        }
        return null;
    }
}
