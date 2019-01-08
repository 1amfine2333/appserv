/**
 *
 */
package com.xianglin.appserv.biz.shared.push;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.notnoop.apns.PayloadBuilder;
import com.xianglin.appserv.common.dal.daointerface.AppPushDAO;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgType;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;

/**
 * JPush 实现
 *
 * @author wanglei 2016年9月26日下午3:17:02
 */
@Service("apnsPush")
public class ApnsPusherImpl extends MessagePusherAdapter implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ApnsPusherImpl.class);

    private ApnsService apnsService;

    @Value("#{config['apnsCertificate']}")
    private String apnsCertificate;

    @Value("#{config['apnsPassword']}")
    private String apnsPassword;

    @Value("#{config['MESSAGE_DETAIL_URL']}")
    private String messageDetial;

    @Value("#{config['env']}")
    private String env;

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    /**
     * @see com.xianglin.appserv.biz.shared.push.MessagePusher#init()
     */
    @Override
    public void init() {
        InputStream certFile = null;
        ApnsServiceBuilder builder = null;
        try {
            logger.debug("prop:{} pwd:{}", getClass().getClassLoader().getResource(apnsCertificate), apnsPassword);
            certFile = getClass().getClassLoader().getResourceAsStream(apnsCertificate);
            if (certFile == null) {
                throw new RuntimeException("read apns certFile fail! apnsEnviroment:" + apnsCertificate);
            }
            builder = APNS.newService().withCert(certFile, apnsPassword);
            if (StringUtils.startsWith(env, "dev")) {
                logger.debug("read pre withSandboxDestination cent");
                apnsService = builder.withSandboxDestination().build();
            } else {
                logger.debug("read prd withProductionDestination cent");
                apnsService = builder.withProductionDestination().build();
            }
            logger.info("APNS service init successfully! apnsEnviroment:{}", builder);
        } catch (Throwable e) {
            IOUtils.closeQuietly(certFile);
            logger.debug(e.getMessage());
        }
    }

    /**
     * @see com.xianglin.appserv.biz.shared.push.MessagePusher
     */
    @Override
    public void push(MsgVo msg, Set<String> tockens) {
        try {
            logger.debug("apns push msg {} tockens :{}",msg,ArrayUtils.toArray(tockens));
            String msgUrl = messageDetial + "?id=" + msg.getId() + "&type=";
            if (StringUtils.equals(msg.getMsgType(), MsgType.ARTICLE.name())) {
                msgUrl += "other";
            } else {
                msgUrl += "sys";
            }
            Map<String, String> extras = new HashMap<>();
            extras.put("type", "web");
            extras.put("url", msg.getUrl());
            extras.put("partyId", msg.getPartyId() + "");
            extras.put("title", msg.getMsgTitle());
            extras.put("msgType", msg.getMsgType());
            extras.put("content-available", "1");
            extras.put("msgTime", System.currentTimeMillis() + "");
            extras.put("expiryTime", msg.getExpiryTime() + "");//过期时间
            extras.put("loginCheck", msg.getLoginCheck());//是否需要登录
            extras.put("msgId", msg.getId() + "");
            extras.put("msgSource",msg.getMsgSource());
            PayloadBuilder builder = APNS.newPayload().customFields(extras).alertBody(StringUtils.substring(msg.getMsgTitle(), 0, 30) + "\n" + StringUtils.substring(StringUtils.replacePattern(msg.getMessage(), "<.*?>", ""), 0, 130));
            if (StringUtils.equals(msg.getMsgType(), MsgType.MERCHANT_ORDER.name())) {
                builder.sound("noticeVoice.m4a");
            } else {
                builder.sound("default");
            }
            Map<String, Date> map = apnsService.getInactiveDevices();
            if (MapUtils.isNotEmpty(map)) {
                deleteInvalidToken(map.keySet());
            }
            Collection<? extends ApnsNotification> resp = apnsService.push(tockens, builder.build());
            //清理掉多余的tokens数据
            logger.debug("apns send {}", ArrayUtils.toArray(resp));
        } catch (Throwable e) {
            logger.debug(e.getMessage());
            init();
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            InputStream certFile = new FileInputStream
                                 ("D:\\Users\\wanglei.xianglin\\IdeaProjects\\xlwork\\appserv\\biz\\shared\\src\\main\\resources\\apns\\prd.p12");
//                    ("D:\\ideaproject\\idea\\appserv\\biz\\shared\\src\\main\\resources\\apns\\pp.p12");
            ApnsServiceBuilder builder = APNS.newService().withCert(certFile, "123456");
//			ApnsService apnsService = builder.withSandboxDestination().build();
            ApnsService apnsService = builder.withProductionDestination().build();
            Map<String, String> para = new HashMap<>();
            para.put("type", "web");
            para.put("para2", "2321");
            PayloadBuilder build = APNS.newPayload().customFields(para).alertBody(StringUtils.replacePattern("ggggggg", "<.*?>", ""))
                    .sound("noticeVoice.m4a");

            Set<String> tokens = new HashSet<>();
            /*tokens.add("7c650e6ffc20234a5b95c637a7e50de86f37c3a88d31e1883a7b0c585c814988");
            tokens.add("becedf8ad5c04b9eeb220258ae667db29a230f83be75c972591b8bc5e918da37");
            tokens.add("55605c429c244ca55ed5aa0c9714b7a1e746389cdadb75777b465f526f0c4d25");
            tokens.add("db99ac9101969a31081873c5846cb65989a876ed4b3ff6e3eaf75b2658b0bc17");
            tokens.add("399b02245f331205340fedcc911216c8600fafef049bf89aa6be8b59826ee0a6");
            tokens.add("0016f2dda684b3d490282027f3c3a46139e3555c6aab51ac80ffa3734a64f57b");
            tokens.add("61895cb12ffa186a6b937333ac41da43b83a7133af9cdcdadecafd561845a0dc");
            tokens.add("02f873511ea51abee6d48c1352e6f369bdb77ac4b6d9afec646d000bed3502f4");
            tokens.add("0ac28087a79c1e59e4127804bae0d797ba4b5af8f9b4641e12d960c159f45153");
            tokens.add("668193a08eb84dffaa3da476afae5c3606ec33d7a1a6491d2e4389014e87ba7a");*/
            //tokens.add("fdb2ff3e2bf4582118df02925d798002223afbd6792f51baac9cf951da7f7b59");
            tokens.add("488bd260b9b4e52409ba2eb15e56b6e403246e8c7fb2827dd0f089d464933b70");
            Collection<? extends ApnsNotification> resp = apnsService.push(tokens, build.build());
//			Map<String, Date> indd =  apnsService.getInactiveDevices();
            logger.debug("apns send {}", ArrayUtils.toArray(resp));
//			ApnsNotification resp = apnsService.push(toke,build.build(),DateUtils.addDays(new Date(), 1));

            System.out.println(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
