package com.xianglin.appserv.biz.service.implement.filter;

import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;


/**
 * 打印输入输出参数
 */
public class LogParameterFilter {

	private Logger logger = LoggerFactory.getLogger(LogParameterFilter.class);

    @Autowired
    private SessionHelper sessionHelper;

    /**
     * 所有biz的通用method方法
     * 
     * @param point
     * @return
     * @throws Throwable 
     */
    public Object doArount(ProceedingJoinPoint point) throws Throwable {
        //返回对象
        Object ret = null;
        //获得返回对象类型
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Class returnClz = method.getReturnType();
        try {
            //交易前检查
            doBefore(point.getArgs(), method);
            //method invoke
            ret = point.proceed();
        }  catch (Exception e) {
        	logger.warn("do method ", e);
        	throw e;
        } finally {
            //无论如何打印返回值
            doAfter(ret, method);
        }
        return ret;
    }

    /**校验入参并打印
     * @param args
     * @param method
     * @throws Exception
     */
    protected void doBefore(Object args[], Method method) throws Exception {
        //如果是没有入参的方法，则不做检查
        if (args == null || args.length == 0) {
            return;
        }
        for (int i = 0; i < args.length; i++) {
            Object tmp = args[i];
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            logger.info("request :{} Method:{} LoginUser:{} Args Value:{}", method.getDeclaringClass(), method.getName(),partyId,ToStringBuilder.reflectionToString(tmp));
        }
    }

    /**方法结束后打印返回对象
     * @param ret
     * @param method
     */
    protected void doAfter(Object ret, Method method) {
        if (ret != null) {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            logger.info("response:{} Method:{} LoginUser:{} Result object:{}", method.getDeclaringClass(), method.getName(),partyId,ToStringBuilder.reflectionToString(ret));
        }
    }

}
