package com.xianglin.appserv.common.dal.plugin;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.xianglin.appserv.common.util.EmojiEscapeUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 拦截器，拦截名单范围内的emoji字符，进行入库转义和出库反转义
 * 仅对参数为一个DO的mapper方法进行拦截处理
 * 主要是BaseDAO中的insert，主键select，和主键update方法
 *
 * @author Yungyu
 * @description Created by Yungyu on 2018/4/3 16:44.
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {
                MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class EmojiEscapeInterceptor implements Interceptor {

    /**
     * holde palce field name
     */
    public static final String FIELD_PROP_KEY = "XIANGLIN_PACKAGE";

    private static final Logger logger = LoggerFactory.getLogger(EmojiEscapeInterceptor.class);

    /**
     * 占位key
     */
    private static final String HOLD_KEY = "holdon-holdon-XXX";

    /**
     * 自定义类的包路径
     */
    private static final String XIANGLIN_PACKAGE = "com.xianglin";

    /**
     * 缓存需要转义的mapper方法
     */
    private static Multimap<Class<?>, String> ESCAPE_METHOD_CACHE = ArrayListMultimap.create();

    /**
     * 缓存需要转义的字段
     */
    private static Multimap<Class<?>, Field> ESCAPE_FIELD_CACHE = ArrayListMultimap.create();

    /**
     * 需要拦截的mapper类
     */
    private static Set<Class<?>> INTERCEPT_STATEMENTS = Sets.newHashSet();

    public static void main(String[] args) throws NoSuchFieldException {

        Field declaredField = EmojiEscapeInterceptor.class.getDeclaredField(FIELD_PROP_KEY);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        SqlCommandType sqlCommandType = ms.getSqlCommandType();
        String statementId = ms.getId();

        int lastIndexOfDot = statementId.lastIndexOf(".");
        String mapperClassName = statementId.substring(0, lastIndexOfDot);
        String methodName = statementId.substring(lastIndexOfDot + 1);

        Class<?> mapperClass = Class.forName(mapperClassName);
        if (!INTERCEPT_STATEMENTS.contains(mapperClass)) {  //是否需要处理
            return invocation.proceed();
        }

        //不考虑方法的重载，因为一般不重载，重载的方法还是会共享同一个sql node
        if (!ESCAPE_METHOD_CACHE.get(mapperClass).contains(methodName)) {  //不需要转义的方法直接调用返回
            return invocation.proceed();
        }

        Object result;
        switch (sqlCommandType) {  //更新和查询两种情况，更新处理参数，查询处理返回值
            case INSERT:
            case UPDATE:
                result = resolveParameter(invocation);
                break;
            case SELECT:
                result = resolveResult(invocation);
                break;
            default:
                result = invocation.proceed();
        }
        return result;
    }

    /**
     * 转义参数
     *
     * @param invocation
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    private Object resolveParameter(Invocation invocation) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {

        Object[] args = invocation.getArgs();
        if (args.length < 2) {
            throw new IllegalStateException("拦截器参数错误！");
        }
        Object parameter = args[1];
        if (parameter == null) {
            return invocation.proceed();
        }
        if (parameter instanceof Map) {  //方法参数是map，包括param注解生成的map
        } else if (parameter.getClass().getName().contains(XIANGLIN_PACKAGE)) { // 方法参数是自定义对象
            Class<?> parameterClass = parameter.getClass();
            if (!ESCAPE_FIELD_CACHE.containsKey(parameterClass)) {
                checkFieldCache(parameterClass);
            }
            Collection<Field> fields = ESCAPE_FIELD_CACHE.get(parameterClass);
            if (fields.size() >= 2) {  //如果有多于1个的字段，说明有字段需要转义
                fields.stream()
                        .filter(input -> input.getDeclaringClass() != EmojiEscapeInterceptor.class)  //过滤占位字段
                        .forEach(input -> {
                            try {
                                input.setAccessible(true);
                                Object fieldValue = input.get(parameter);
                                if (fieldValue != null) {
                                    if (fieldValue instanceof String) {
                                        String escapeString = EmojiEscapeUtil.escapeEmoji2Base64String((String) fieldValue);
                                        input.set(parameter, escapeString);
                                    } else {
                                        logger.warn("===========转义注解标注在非String类型的字段上：[[类： {}，字段：{} ]]===========", parameterClass.getName(), input.getName());
                                    }
                                }
                            } catch (IllegalAccessException e) {
                                logger.error("===========表情转义错误：[[类： {}，字段：{} ]]===========", parameterClass.getName(), input.getName(), e);
                                throw new RuntimeException(e);
                            }
                        });
            }
        } else {
        }
        return invocation.proceed();
    }

    private Object resolveResult(Invocation invocation) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {

        List<?> resultList = (List) invocation.proceed();
        if (resultList.isEmpty()) {
            return resultList;
        }
        Object component = resultList.get(0); //获取DO
        Class<?> componentClass = component.getClass();
        if (!componentClass.getName().contains(XIANGLIN_PACKAGE)) {  //如果不是自定义对象，如map，string等
            return resultList;
        }
        if (!ESCAPE_FIELD_CACHE.containsKey(componentClass)) {
            checkFieldCache(componentClass);
        }
        Collection<Field> finalFields = ESCAPE_FIELD_CACHE.get(componentClass);
        if (finalFields.size() <= 1) {
            return resultList;
        }
        resultList.forEach(input -> {
            for (Field field : finalFields) {
                if (field.getDeclaringClass() == EmojiEscapeInterceptor.class) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    Object fieldValue = field.get(input);
                    if (fieldValue != null) {
                        if (fieldValue instanceof String) {
                            String escapeString = EmojiEscapeUtil.escapeBase642EmojiString((String) fieldValue);
                            field.set(input, escapeString);
                        } else {
                            logger.warn("===========转义注解标注在非String类型的字段上：[[类： {}，字段：{} ]]===========", input.getClass().getName(), field.getName());
                        }
                    }
                } catch (IllegalAccessException e) {
                    logger.error("===========表情转义错误：[[类： {}，字段：{} ]]===========", input.getClass().getName(), field.getName(), e);
                    throw new RuntimeException(e);
                }
            }
        });
        return resultList;
    }

    /**
     * 检查字段缓存
     *
     * @param parameterClass
     * @return
     * @throws NoSuchFieldException
     */
    private Collection<Field> checkFieldCache(Class<?> parameterClass) throws NoSuchFieldException {

        synchronized (this) {  //并发控制
            if (!ESCAPE_FIELD_CACHE.containsKey(parameterClass)) {  //初始化转义字段缓存
                Field[] declaredFields = parameterClass.getDeclaredFields();
                Lists.newArrayList(declaredFields)
                        .stream()
                        .filter(input -> input.isAnnotationPresent(EmojiEscapeUtil.EscapeField.class))
                        .forEach(input -> ESCAPE_FIELD_CACHE.put(parameterClass, input));
                ESCAPE_FIELD_CACHE.put(parameterClass, EmojiEscapeInterceptor.class.getDeclaredField(FIELD_PROP_KEY)); //缓存占位
            }
        }

        return ESCAPE_FIELD_CACHE.get(parameterClass);
    }

    @Override
    public Object plugin(Object target) {

        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

        if (properties != null) {
            for (Object o : properties.values()) {
                String className = (String) o;
                if (Strings.isNullOrEmpty(className)) {
                    continue;
                }
                Class<?> mapperClass;
                try {
                    mapperClass = Class.forName(className);
                    INTERCEPT_STATEMENTS.add(mapperClass);
                    Method[] methods = mapperClass.getMethods();
                    Lists.newArrayList(methods)
                            .stream()
                            .filter(input -> input.getDeclaringClass() != Object.class)  //非object的方法
                            .filter(method -> method.isAnnotationPresent(EmojiEscapeUtil.EscapeMehthod.class))  //标记了转义的接口方法
                            .forEach(input -> ESCAPE_METHOD_CACHE.put(mapperClass, input.getName()));
                    ESCAPE_METHOD_CACHE.put(mapperClass, HOLD_KEY);  //保证每个class至少有一个占位key
                } catch (ClassNotFoundException e) {
                    logger.error("===========转义插件参数错误，无法加载指定的mapperClass [[ {} ]]===========", className, e);
                    throw new RuntimeException("转义插件参数错误");
                }
            }
        }
        logger.info("===========emoji mapper转义拦截名单初始化完成 ： [[ {} ]]===========", properties);
    }

}
