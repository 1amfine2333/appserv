package com.xianglin.appserv.biz.service.implement;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.collect.Maps;
import com.xianglin.appserv.biz.shared.CurrencyRateService;
import com.xianglin.appserv.biz.shared.SysParaManager;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.dataobject.SystemConfigModel;
import com.xianglin.appserv.common.service.facade.app.SystemParaService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.CurrencyNameEnum;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.vo.MapVo;
import com.xianglin.appserv.common.service.facade.model.vo.SysParaVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.MethodAccessInvokable;
import com.xianglin.appserv.common.util.MethodAccessInvokableImpl;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统参数管理实现
 * <p/>
 * Created by wanglei on 2017/1/4.
 */
@Service("systemParaService")
@ServiceInterface(SystemParaService.class)
public class SystemParaServiceImpl implements SystemParaService, InitializingBean {

    private static final MethodAccess THIS_METHOD_ACCESS = MethodAccess.get(SystemParaServiceImpl.class);

    private static final Map<String, MethodAccessInvokable> METHOD_ACCESSES = Maps.newConcurrentMap();

    private static final Logger logger = LoggerFactory.getLogger(SystemParaServiceImpl.class);

    @Autowired
    private CurrencyRateService currencyRateService;

    /**
     * 初始化方法分发缓存
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        Class<SystemParaServiceImpl> clazz = SystemParaServiceImpl.class;
        Method[] methods = clazz.getDeclaredMethods();
        Arrays.asList(methods)
                .stream()
                .filter(method -> method.isAnnotationPresent(ParamMethod.class))
                .forEach(input -> {
                    ParamMethod annotation = input.getAnnotation(ParamMethod.class);
                    String preffix = annotation.preffix();
                    MethodAccessInvokableImpl methodAccessInvokable = new MethodAccessInvokableImpl();
                    methodAccessInvokable.setMethodAccess(THIS_METHOD_ACCESS);
                    methodAccessInvokable.setMethodIndex(THIS_METHOD_ACCESS.getIndex(input.getName()));
                    methodAccessInvokable.setTarget(this);
                    METHOD_ACCESSES.put(preffix, methodAccessInvokable);
                });
    }

    @Autowired()
    private SysParaManager sysParaManager;

    @Autowired
    private SessionHelper sessionHelper;

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.SystemParaService.queryPara", description = "查询参数配置信息")
    public Response<SysParaVo> queryPara(String paraName) {

        Response<SysParaVo> response = ResponseUtils.successResponse();
        try {
            final String paramKey = paraName;
            SysParaVo result = checkParamKey(paramKey);
            if (result != null) {
                return Response.ofSuccess(result);
            }
            //判断用户查询的是否是邀请好友的URL，如果是 根据版本号判断查询参数
            if (StringUtils.equals(paraName, "activity_v321_url")) {
                String version = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
                if (StringUtils.contains("3.5.0,3.5.1", version)) {
                    paraName = "activity_v351_url";
                }
            } else if (StringUtils.equals(paraName, "app.ios.support.nodecode")) {//仅针ios端指定版本
                String currentVersion = sysParaManager.queryConfigValue("iosVersion");
                String version = sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION, String.class);
                if(!StringUtils.equals(currentVersion,version)){
                    paraName="activity.fresh.red.packet.switch";
                }
            }
            Map<String, Object> paras = DTOUtils.queryMap();
            paras.put("code", paraName);
            List<SystemConfigModel> list = sysParaManager.queryPara(paras);
            if (CollectionUtils.isNotEmpty(list)) {
                response.setResult(DTOUtils.map(list.get(0), SysParaVo.class));
            } else {
                response.setFacade(FacadeEnums.RETURN_EMPTY);
            }
            String value = response.getResult().getValue();
            if (StringUtils.contains(value, "PARTYID")) {
                Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
                response.getResult().setValue(StringUtils.replace(value, "PARTYID", partyId + ""));
            }
        } catch (Exception e) {
            logger.error("queryPara error", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

    @Override
    public Response<List<SysParaVo>> queryPara(PageReq req) {

        Response<List<SysParaVo>> response = ResponseUtils.successResponse();
        try {
            Map<String, Object> paras = DTOUtils.beanToMap(req);
            List<SystemConfigModel> list = sysParaManager.queryPara(paras);
            if (CollectionUtils.isNotEmpty(list)) {
                response.setResult(DTOUtils.map(list, SysParaVo.class));
            } else {
                response.setFacade(FacadeEnums.RETURN_EMPTY);
            }
        } catch (Exception e) {
            logger.error("queryPara error", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

    @Override
    public Response<Boolean> updatePara(SysParaVo para) {

        Response<Boolean> response = ResponseUtils.successResponse(false);
        try {
            Boolean flag = sysParaManager.updatePara(DTOUtils.map(para, SystemConfigModel.class));
            if (flag) {
                response.setResult(true);
            }
        } catch (Exception e) {
            logger.error("updatePara error", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

    @Override
    public Response<Boolean> addPara(SysParaVo para) {

        Response<Boolean> response = ResponseUtils.successResponse(false);
        try {
            Boolean flag = sysParaManager.addPara(DTOUtils.map(para, SystemConfigModel.class));
            response.setResult(flag);
        } catch (Exception e) {
            logger.error("addPara error", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.app.SystemParaService.queryAppParas", description = "批量返回app配置参数")
    public Response<List<MapVo>> queryAppParas() {

        Response<List<MapVo>> response = new Response<>(null);
        try {
            String config = sysParaManager.queryConfigValue("app_batch_paras");
            if (StringUtils.isNotEmpty(config)) {
                List<MapVo> list = sysParaManager.queryPara(null).stream().filter(systemConfigModel -> {
                    return StringUtils.contains(config, systemConfigModel.getCode());
                }).map(s -> {
                    MapVo vo = MapVo.builder().key(s.getCode()).value(s.getValue()).build();
                    if (StringUtils.contains(vo.getValue(), "PARTYID")) {
                        Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
                        vo.setValue(StringUtils.replace(vo.getValue(), "PARTYID", partyId + ""));
                    }
                    return vo;
                }).collect(Collectors.toList());
                response.setResult(list);
            }
        } catch (Exception e) {
            logger.info("queryAppParas error", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }
        return response;
    }

    /**
     * 处理CURRENCY_RATE_前缀的查询请求
     * 用于查询当日汇率
     * * @param key
     *
     * @return
     */
    @ParamMethod(preffix = CurrencyNameEnum.PREFFIX)
    public SysParaVo getValue(String key) throws Exception {

        Map<String, Object> paras = DTOUtils.queryMap();
        paras.put("code", key);
        List<SystemConfigModel> list = sysParaManager.queryPara(paras);
        SystemConfigModel DO = null;
        if (!list.isEmpty()) {
            DO = list.get(0);
        }
        Date updateTime = null;
        if (DO != null) {
            updateTime = DO.getUpdateTime();
        }
        if (updateTime != null) {
            Instant instant = updateTime.toInstant();
            LocalDate updateLocalDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
            if (updateLocalDate.compareTo(LocalDate.now()) == 0) {  //今天创建的
                return DTOUtils.map(DO, SysParaVo.class);
            }
        }
        String rate = currencyRateService.queryCurrecyRate(key);
        Date now = new Date();
        SystemConfigModel model = new SystemConfigModel();
        model.setCode(key);
        model.setValue(rate);
        model.setUpdateTime(now);
        if (DO == null) {  //新增或者更新
            model.setCreateTime(now);
            model.setDescription("汇率");
            sysParaManager.addPara(model);
        } else {
            sysParaManager.updateByName(model);
        }
        return DTOUtils.map(model, SysParaVo.class);
    }

    /**
     * 特定前缀请求的分发
     *
     * @param paraName
     * @return
     */
    private SysParaVo checkParamKey(String paraName) {

        List<String> preffixSets = METHOD_ACCESSES.keySet()
                .stream()
                .filter(input -> paraName.startsWith(input))
                .collect(Collectors.toList());

        if (preffixSets == null || preffixSets.isEmpty()) {
            return null;
        }

        if (preffixSets.size() > 1) {
            throw new IllegalStateException("系统参数查询错误：前缀不唯一");
        }
        String preffix = preffixSets.get(0);
        MethodAccessInvokable invokable = METHOD_ACCESSES.get(preffix);
        return (SysParaVo) invokable.invoke(paraName);
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    private static @interface ParamMethod {

        String preffix();

    }
}
