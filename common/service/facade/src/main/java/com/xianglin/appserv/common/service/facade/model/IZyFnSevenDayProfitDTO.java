package com.xianglin.appserv.common.service.facade.model;

/**
 * Describe :
 * Created by xingyali on 2018/3/8 17:16.
 * Update reason :
 */
public class IZyFnSevenDayProfitDTO {
    private static final long serialVersionUID = -885109003337585361L;
    private String updateDate;
    private String weekRate;
    private String tenThousandIncome;

    public IZyFnSevenDayProfitDTO() {
    }

    public String getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getWeekRate() {
        return this.weekRate;
    }

    public void setWeekRate(String weekRate) {
        this.weekRate = weekRate;
    }

    public String getTenThousandIncome() {
        return this.tenThousandIncome;
    }

    public void setTenThousandIncome(String tenThousandIncome) {
        this.tenThousandIncome = tenThousandIncome;
    }
}
