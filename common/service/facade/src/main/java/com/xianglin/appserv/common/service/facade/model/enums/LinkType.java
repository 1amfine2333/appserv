package com.xianglin.appserv.common.service.facade.model.enums;

/**
 * @author Yungyu
 * @description Created by Yungyu on 2018/3/5 21:18.
 */
public enum LinkType {

    PUBLIC_ADD("PUBLIC_ADD"),  //一建添加外链
    PRIVATE_ADD("PRIVATE_ADD"); //一建添加内链

    String value;

    LinkType(String value) {

        this.value = value;
    }
}
