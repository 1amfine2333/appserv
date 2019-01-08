package com.xianglin.appserv;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.*;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import com.xianglin.gateway.common.service.spi.util.ClassScanUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Yungyu
 * @description Created by Yungyu on 2018/3/7 19:00.
 * 生成网关sql脚本
 */

public class GateWaySqlUtil {

    private static final Logger logger = LoggerFactory.getLogger(GateWaySqlUtil.class);

    /**
     * 分隔符
     */
    private static final String SPLITOR = ".";

    /**
     * sql模板
     */
    private static final String SQL_TEMPLATE = "INSERT INTO confdb.gateway_service_config (ID, GATEWAY_NAME, ALIAS, SYSTEM_NAME, SERVICE_ID, RPC_TYPE, SPI_VERSION, PROTOCOL, TIMEOUT, GATEWAY_SERVICE_INTERFACE_NAME, NEED_LOGIN, CHANGE_SESSION, DELETE_SESSION, USE_ETAG, STATUS, CREATOR, UPDATER, CREATE_TIME, UPDATE_TIME, COMMENTS)" +
            "VALUES (NULL, 'appgw', '%s', 'appserv'," +
            "'%s', 'service', '1.0.0'," +
            "'dubbo', 4000, 'com.xianglin.gateway.common.service.spi.JSONGatewayService', 1, 0, 0, 0, 'ENABLE',\n" +
            "'system'," +
            "'system', now(), now(), '%s');";

    public static List<String> generateSql(String packages) {

        Preconditions.checkArgument(!Strings.isNullOrEmpty(packages), "参数不能为空");

        List<String> sqlLines = Lists.newArrayList();

        final List<String> packageStrs = Splitter.on(",|;")
                .omitEmptyStrings()
                .splitToList(packages);

        ArrayList<List<Class<?>>> lists = Lists.newArrayList(Lists.transform(packageStrs, new Function<String, List<Class<?>>>() {

            @Override
            public List<Class<?>> apply(String input) {

                return ClassScanUtil.getClassesFromPackage(input);

            }
        }));

        ArrayList<JSONObject> sqlParams = Lists.newArrayList();

        for (List<Class<?>> list : lists) {
            for (Class<?> aClass : list) {

                if (!aClass.isAnnotationPresent(ServiceInterface.class)) {
                    continue;
                }

                System.out.println("=================================================================================================================");
                System.out.println(aClass.getName());
                System.out.println("=================================================================================================================");
                sqlParams.clear();

                concateSql(sqlParams, aClass);
                for (JSONObject sqlParam : sqlParams) {
                    String serviceId = sqlParam.getString("serviceId");
                    String alias = sqlParam.getString("alias");
                    String desc = sqlParam.getString("desc");
                    String sql = String.format(SQL_TEMPLATE, alias, serviceId, desc);
                    sqlLines.add(sql);
                    System.out.println(sql);
                    System.out.println("#---------------------------------------------");

                }


            }
        }


        return sqlLines;
    }

    private static void concateSql(ArrayList<JSONObject> sqlParams, Class<?> aClass) {

        ServiceInterface serviceInterface = aClass.getAnnotation(ServiceInterface.class);

        Class<?> interfaceClass = getInterfaceClass(aClass, serviceInterface);

        Iterator<Method> iterator = Iterables.filter(Lists.newArrayList(aClass.getMethods()), new Predicate<Method>() {

            @Override
            public boolean apply(Method input) {

                return !(input.getAnnotation(ServiceMethod.class) == null);

            }
        }).iterator();

        while (iterator.hasNext()) {
            Method method = iterator.next();
            Type[] parameterTypes = method.getGenericParameterTypes();

            String serviceId = "";
            String alias = new StringBuilder(interfaceClass.getName()).append(SPLITOR).append(method.getName()).toString();
            String desc = method.getAnnotation(ServiceMethod.class).description();


            StringBuilder serviceIdBuilder = new StringBuilder(interfaceClass.getName()).append(SPLITOR).append(method.getName())
                    .append(SPLITOR);
            StringBuilder builder = new StringBuilder("");
            if (ArrayUtils.isNotEmpty(parameterTypes)) {
                for (Type type : parameterTypes) {
                    builder.append(type.toString());
                    // jdk 1.8 开始，Type才有getTypeName方法
                    // builder.append(type.getTypeName());
                }
            }
            String base64 = org.apache.commons.codec.digest.DigestUtils.md5Hex(builder.toString());
            serviceId = serviceIdBuilder.append(StringUtils.substring(base64, 0, 8)).toString();

            JSONObject methodInfo = new JSONObject();
            methodInfo.put("serviceId", serviceId);
            methodInfo.put("alias", alias);
            methodInfo.put("desc", desc);

            sqlParams.add(methodInfo);
        }


    }

    private static Class<?> getInterfaceClass(Class<?> aClass, ServiceInterface serviceInterface) {


        if (serviceInterface.value() == Void.class) {
            Class<?>[] interfaces = aClass.getInterfaces();
            Preconditions.checkState(interfaces.length == 1, String.format("接口多余一个时未指定接口：%s", aClass.getName()));
            return interfaces[0];
        } else {
            return serviceInterface.value();
        }
    }


    public static void main(String[] args) {

        generateSql("com.xianglin.appserv.biz.service.implement");
    }
}

