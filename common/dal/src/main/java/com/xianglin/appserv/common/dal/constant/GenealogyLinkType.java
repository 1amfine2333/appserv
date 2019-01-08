package com.xianglin.appserv.common.dal.constant;

/**
 * @author Yungyu
 * @description Created by Yungyu on 2018/3/5 17:32.
 */

public enum GenealogyLinkType implements BaseDbEnum {
    PRIVATE_COPY("内链拷贝"),
    PRIVATE_ADD("内链添加"),
    PUBLIC_ADD("外链添加");

    private String value;

    GenealogyLinkType(String value) {

        this.value = value;
    }

    @Override
    public String getValue() {

        return value;
    }
}
