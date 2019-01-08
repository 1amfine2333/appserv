package com.xianglin.appserv.common.service.facade.req;/**
 * Created by wanglei on 2017/4/11.
 */

import com.xianglin.appserv.common.service.facade.model.vo.BaseVo;

/**
 * @author
 * @create 2017-04-11 14:38
 **/
public class NodeAchieveReq extends BaseVo{

    /**
     *查询年份
     */
    private int year;

    /**
     * 查询月份
     */
    private int month;

    /**
     * 类型，区分余额，开卡数，年日均，月日均
     */
    private String type;

    private String category;

    public int getYear () {
        return year;
    }

    public void setYear (int year) {
        this.year = year;
    }

    public int getMonth () {
        return month;
    }

    public void setMonth (int month) {
        this.month = month;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
