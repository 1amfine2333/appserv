package com.xianglin.appserv.common.service.facade.model.enums;


import org.apache.commons.lang3.StringUtils;

/**
 * 热门微博搜索类型
 */
public enum ArticlePopularType {

    ALL("热门"),
    TODAY("24小时榜"),
    YESTERDAY("昨日"),
    BEFOREYESTERDAY("前日"),
    WEEK("周榜"),;
    private String desc;

    ArticlePopularType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static ArticlePopularType getType(String type) {
        if (StringUtils.isEmpty(type)) {
            return ArticlePopularType.ALL;
        } else {
            return ArticlePopularType.valueOf(type);
        }
    }
}
