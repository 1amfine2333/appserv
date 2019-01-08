package com.xianglin.appserv.biz.shared.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.xianglin.appserv.biz.shared.CurrencyRateService;
import com.xianglin.appserv.common.service.facade.model.enums.CurrencyNameEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * @author Yungyu
 * @description Created by Yungyu on 2018/4/17 13:31.
 */
@Service
public class CurrencyRateServiceImpl implements CurrencyRateService {

    public static final String ACCESS_KEY = "";

    public static final String SERVER = "https://data.fixer.io";

    public static final String ENDPOINT = "api/latest";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String queryCurrecyRate(String key) {

        Map.Entry<CurrencyNameEnum, CurrencyNameEnum> currencyNames = CurrencyNameEnum.ofKey(key);
        UriComponents uriComponent = UriComponentsBuilder.fromHttpUrl(SERVER)
                .path(ENDPOINT)
                .queryParam("access_key", "8b43a43017c596fee16c3f4a23ca6dde")
                .queryParam("BASE", currencyNames.getKey().name())
                .build();

        String body = restTemplate.getForObject(uriComponent.toUriString(), String.class);
        JSONObject resultJson = JSON.parseObject(body);
        JSONObject rates = resultJson.getJSONObject("rates");
        String rate = rates.getString(currencyNames.getValue().name());
        return Strings.nullToEmpty(rate);
    }
}
