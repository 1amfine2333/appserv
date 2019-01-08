package com.xianglin.appserv.common.service.facade.model;

import java.util.List;
import java.util.Map;

/**
 * Describe :
 * Created by xingyali on 2018/3/8 17:15.
 * Update reason :
 */
public class IZyFnProductInfoRespDTO {
    private static final long serialVersionUID = -6110851481817915864L;
    private String fundName;
    private String tenThousandIncome;
    private String weekRate;
    private Map<String, String> fundAdditionalInfoMap;
    private String totalInLimit;
    private String totalOutLimit;
    private List<IZyFnSevenDayProfitDTO> product7daysList;

    public IZyFnProductInfoRespDTO() {
    }

    public String getFundName() {
        return this.fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getTenThousandIncome() {
        return this.tenThousandIncome;
    }

    public void setTenThousandIncome(String tenThousandIncome) {
        this.tenThousandIncome = tenThousandIncome;
    }

    public String getWeekRate() {
        return this.weekRate;
    }

    public void setWeekRate(String weekRate) {
        this.weekRate = weekRate;
    }

    public Map<String, String> getFundAdditionalInfoMap() {
        return this.fundAdditionalInfoMap;
    }

    public void setFundAdditionalInfoMap(Map<String, String> fundAdditionalInfoMap) {
        this.fundAdditionalInfoMap = fundAdditionalInfoMap;
    }

    public String getTotalInLimit() {
        return this.totalInLimit;
    }

    public void setTotalInLimit(String totalInLimit) {
        this.totalInLimit = totalInLimit;
    }

    public String getTotalOutLimit() {
        return this.totalOutLimit;
    }

    public void setTotalOutLimit(String totalOutLimit) {
        this.totalOutLimit = totalOutLimit;
    }

    public List<IZyFnSevenDayProfitDTO> getProduct7daysList() {
        return this.product7daysList;
    }

    public void setProduct7daysList(List<IZyFnSevenDayProfitDTO> product7daysList) {
        this.product7daysList = product7daysList;
    }
}
