package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by wanglei on 2017/7/21.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AreaVo extends BaseVo {

    /**
     * 省
     */
    protected DistrictVo province;

    /**
     * 市
     */
    protected DistrictVo city;

    /**
     * 县
     */
    protected DistrictVo county;

    /**
     * 镇
     */
    protected DistrictVo town;

    /**
     * 村
     */
    protected DistrictVo village;

    protected String district;


    /**
     * 取省名
     *
     * @return
     */
    public String getProvinceName() {

        if (province != null) {
            return province.getName();
        }
        return null;
    }

    /**
     * 取市名
     *
     * @return
     */
    public String getCityName() {

        if (city != null) {
            return city.getName();
        }
        return null;
    }

    /**
     * 取县名
     *
     * @return
     */
    public String getCountyName() {

        if (county != null) {
            return county.getName();
        }
        return null;
    }

    /**
     * 取镇名
     *
     * @return
     */
    public String getTownName() {

        if (town != null) {
            return town.getName();
        }
        return null;
    }

    /**
     * 取村名
     *
     * @return
     */
    public String getVillageName() {

        if (village != null) {
            return village.getName();
        }
        return null;
    }

    //取最低一级的code
    public String selectCode(AreaVo area) {

        String code = null;
        if (area.getProvince() != null && StringUtils.isNotEmpty(area.getProvince().getCode())) {
            code = area.getProvince().getCode();
        }
        if (area.getCity() != null && StringUtils.isNotEmpty(area.getCity().getCode())) {
            code = area.getCity().getCode();
        }
        if (area.getCounty() != null && StringUtils.isNotEmpty(area.getCounty().getCode())) {
            code = area.getCounty().getCode();
        }
        if (area.getTown() != null && StringUtils.isNotEmpty(area.getTown().getCode())) {
            code = area.getTown().getCode();
        }
        if (area.getVillage() != null && StringUtils.isNotEmpty(area.getVillage().getCode())) {
            code = area.getVillage().getCode();
        }
        return code;
    }

    //取最低一级的code
    public String selectCode() {

        String code = null;
        if (this.getProvince() != null && StringUtils.isNotEmpty(this.getProvince().getCode())) {
            code = this.getProvince().getCode();
        }
        if (this.getCity() != null && StringUtils.isNotEmpty(this.getCity().getCode())) {
            code = this.getCity().getCode();
        }
        if (this.getCounty() != null && StringUtils.isNotEmpty(this.getCounty().getCode())) {
            code = this.getCounty().getCode();
        }
        if (this.getTown() != null && StringUtils.isNotEmpty(this.getTown().getCode())) {
            code = this.getTown().getCode();
        }
        if (this.getVillage() != null && StringUtils.isNotEmpty(this.getVillage().getCode())) {
            code = this.getVillage().getCode();
        }
        return code;
    }

    //取最低一级的name
    public String selectLowName(AreaVo area) {

        String name = null;
        if (area.getProvince() != null && StringUtils.isNotEmpty(area.getProvince().getCode())) {
            name = area.getProvince().getName();
        }
        if (area.getCity() != null && StringUtils.isNotEmpty(area.getCity().getCode())) {
            name = area.getCity().getName();
        }
        if (area.getCounty() != null && StringUtils.isNotEmpty(area.getCounty().getCode())) {
            name = area.getCounty().getName();
        }
        if (area.getTown() != null && StringUtils.isNotEmpty(area.getTown().getCode())) {
            name = area.getTown().getName();
        }
        if (area.getVillage() != null && StringUtils.isNotEmpty(area.getVillage().getCode())) {
            name = area.getVillage().getName();
        }
        return name;
    }

    //取最低一级的name
    public String selectLowName() {

        String name = null;
        if (this.getProvince() != null && StringUtils.isNotEmpty(this.getProvince().getCode())) {
            name = this.getProvince().getName();
        }
        if (this.getCity() != null && StringUtils.isNotEmpty(this.getCity().getCode())) {
            name = this.getCity().getName();
        }
        if (this.getCounty() != null && StringUtils.isNotEmpty(this.getCounty().getCode())) {
            name = this.getCounty().getName();
        }
        if (this.getTown() != null && StringUtils.isNotEmpty(this.getTown().getCode())) {
            name = this.getTown().getName();
        }
        if (this.getVillage() != null && StringUtils.isNotEmpty(this.getVillage().getCode())) {
            name = this.getVillage().getName();
        }
        return name;
    }

    //拼接地址
    public String selectName(AreaVo area) {

        StringBuffer district = new StringBuffer();
        if (area.getProvince() != null && StringUtils.isNotEmpty(area.getProvince().getCode())) {
            district.append(area.getProvince().getName());
        }
        if (area.getCity() != null && StringUtils.isNotEmpty(area.getCity().getCode())) {
            district.append("·" + area.getCity().getName());
        }
        if (area.getCounty() != null && StringUtils.isNotEmpty(area.getCounty().getCode())) {
            district.append("·" + area.getCounty().getName());
        }
        if (area.getTown() != null && StringUtils.isNotEmpty(area.getTown().getCode())) {
            district.append("·" + area.getTown().getName());
        }
        if (area.getVillage() != null && StringUtils.isNotEmpty(area.getVillage().getCode())) {
            district.append("·" + area.getVillage().getName());
        }
        return district.toString();
    }    //拼接地址

    public String selectName() {

        StringBuffer district = new StringBuffer();
        if (this.getProvince() != null && StringUtils.isNotEmpty(this.getProvince().getCode())) {
            district.append(this.getProvince().getName());
        }
        if (this.getCity() != null && StringUtils.isNotEmpty(this.getCity().getCode())) {
            district.append("·" + this.getCity().getName());
        }
        if (this.getCounty() != null && StringUtils.isNotEmpty(this.getCounty().getCode())) {
            district.append("·" + this.getCounty().getName());
        }
        if (this.getTown() != null && StringUtils.isNotEmpty(this.getTown().getCode())) {
            district.append("·" + this.getTown().getName());
        }
        if (this.getVillage() != null && StringUtils.isNotEmpty(this.getVillage().getCode())) {
            district.append("·" + this.getVillage().getName());
        }
        return district.toString();
    }

    //拼接code
    public String selectAllCode(AreaVo area) {

        StringBuffer district = new StringBuffer();
        if (area.getProvince() != null && StringUtils.isNotEmpty(area.getProvince().getCode())) {
            district.append(area.getProvince().getCode());
        }
        if (area.getCity() != null && StringUtils.isNotEmpty(area.getCity().getCode())) {
            district.append("·" + area.getCity().getCode());
        }
        if (area.getCounty() != null && StringUtils.isNotEmpty(area.getCounty().getCode())) {
            district.append("·" + area.getCounty().getCode());
        }
        if (area.getTown() != null && StringUtils.isNotEmpty(area.getTown().getCode())) {
            district.append("·" + area.getTown().getCode());
        }
        if (area.getVillage() != null && StringUtils.isNotEmpty(area.getVillage().getCode())) {
            district.append("·" + area.getVillage().getCode());
        }
        return district.toString();
    }

    //拼接code
    public String selectAllCode() {

        StringBuffer district = new StringBuffer();
        if (this.getProvince() != null && StringUtils.isNotEmpty(this.getProvince().getCode())) {
            district.append(this.getProvince().getCode());
        }
        if (this.getCity() != null && StringUtils.isNotEmpty(this.getCity().getCode())) {
            district.append("·" + this.getCity().getCode());
        }
        if (this.getCounty() != null && StringUtils.isNotEmpty(this.getCounty().getCode())) {
            district.append("·" + this.getCounty().getCode());
        }
        if (this.getTown() != null && StringUtils.isNotEmpty(this.getTown().getCode())) {
            district.append("·" + this.getTown().getCode());
        }
        if (this.getVillage() != null && StringUtils.isNotEmpty(this.getVillage().getCode())) {
            district.append("·" + this.getVillage().getCode());
        }
        return district.toString();
    }

}
