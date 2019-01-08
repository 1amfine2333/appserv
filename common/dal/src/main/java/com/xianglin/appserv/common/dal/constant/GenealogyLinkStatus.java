package com.xianglin.appserv.common.dal.constant;

/**
 * @author Yungyu
 * @description Created by Yungyu on 2018/3/5 20:00.
 * 家谱链接状态
 */
public enum GenealogyLinkStatus implements BaseDbEnum {
    WAIT("等待"), //等待点击
    FINISH("完成"); //完成

    private String value;

    GenealogyLinkStatus(String value) {

        this.value = value;
    }

    @Override
    public String getValue() {

        return value;
    }
}
