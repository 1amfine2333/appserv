package com.xianglin.appserv.common.util;

import com.esotericsoftware.reflectasm.MethodAccess;

/**
 * @author Yungyu
 * @description Created by Yungyu on 2018/4/17 12:53.
 */
public class MethodAccessInvokableImpl implements MethodAccessInvokable {

    private MethodAccess methodAccess;

    private Object target;

    private int methodIndex;

    @Override
    public Object invoke(Object... args) {

        return methodAccess.invoke(target, methodIndex, args);
    }

    public MethodAccess getMethodAccess() {

        return methodAccess;
    }

    public void setMethodAccess(MethodAccess methodAccess) {

        this.methodAccess = methodAccess;
    }

    public Object getTarget() {

        return target;
    }

    public void setTarget(Object target) {

        this.target = target;
    }

    public int getMethodIndex() {

        return methodIndex;
    }

    public void setMethodIndex(int methodIndex) {

        this.methodIndex = methodIndex;
    }
}
