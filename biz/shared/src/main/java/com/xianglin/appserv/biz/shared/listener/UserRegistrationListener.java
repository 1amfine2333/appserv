package com.xianglin.appserv.biz.shared.listener;

import com.alibaba.fastjson.JSON;
import com.xianglin.appserv.biz.shared.listener.event.UserRegistrationEvent;
import com.xianglin.appserv.common.service.facade.app.UserGenealogyService;
import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Yungyu
 * @description Created by Yungyu on 2018/3/5 21:41.
 */
@Component
public class UserRegistrationListener implements ApplicationListener<UserRegistrationEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationListener.class);

    @Autowired
    UserGenealogyService userGenealogyService;

    @Override
    public void onApplicationEvent(UserRegistrationEvent event) {

        try {
            String mobilePhone = event.getUser().getLoginName();
            if (Strings.isNullOrEmpty(mobilePhone)) {
                logger.error("登录用户手机号码为空");
            }
            //处理外链一键添加
            userGenealogyService.finishPublicAdd();
        } catch (Exception e) {
            logger.warn("===========处理登录用户  外链添加家谱成员异常 [[ {} ]]===========", JSON.toJSONString(event.getUser(), true), e);
        }
    }
}
