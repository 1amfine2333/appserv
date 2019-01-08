package com.xianglin.appserv.core.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.xianglin.appserv.common.dal.daointerface.SystemConfigMapper;
import com.xianglin.appserv.common.dal.dataobject.SystemConfigModel;
import com.xianglin.appserv.common.util.ApplicationContextUtil;
import com.xianglin.appserv.common.util.SysConfigUtil;

@Service
@Deprecated
public class SysConfigService  {
    private final Logger Logger = LoggerFactory.getLogger(SysConfigService.class);
    @Autowired
    private SystemConfigMapper systemConfigMapper;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.setApplicationContext(applicationContext);
    }

    public void afterPropertiesSet() throws Exception {
        List<SystemConfigModel> systemConfigModels = systemConfigMapper.getSysConfig();
        for (SystemConfigModel systemConfigModel : systemConfigModels) {
            SysConfigUtil.put(systemConfigModel.getCode(), systemConfigModel.getValue());
            Logger.debug("####code:{}->value:{}", systemConfigModel.getCode(), systemConfigModel.getValue());
        }
    }

    public String getConfigValue(String key, String defaultValue) {
        String param_value = systemConfigMapper.getSysConfigValue(key);
        return param_value == null ? defaultValue : param_value;
    }

    /**
     * 默认返回为null
     *
     * @param key
     * @return
     */
    public String getConfigValue(String key) {
        return getConfigValue(key, null);
    }


//    public static void main(String[] args) {
//        try {
//            List<Area> list = new ArrayList<>(40);
//            ResultSet rs = getConn("select * from xlc_district_code_full where DISTRICT_LEVEL = 1");
//            while (rs.next()) {
//                String province = rs.getString("DISTRICT_NAME");
//                String provinceCode = rs.getString("DISTRICT_CODE");
//                Area area = new Area();
//                area.setName(province);
//                List<City> citys = new ArrayList<>(60);
//                citys.add(new City("不限"));
//                ResultSet crs = getConn("select * from xlc_district_code_full where PARENT_CODE = " + provinceCode);
//                while (crs.next()) {
//                    String city = crs.getString("DISTRICT_NAME");
//                    String cCode = crs.getString("DISTRICT_CODE");
//                    City cItem = new City();
//                    cItem.setName(city);
//                    List<String> xList = new ArrayList<>(60);
//                    xList.add("不限");
//                    System.out.println("cityi name = "+city);
//                    ResultSet xrs = getConn("select * from xlc_district_code_full where PARENT_CODE = " + cCode);
//                    while (xrs.next()) {
//                        String x = xrs.getString("DISTRICT_NAME");
//                        System.out.println("area name = "+x);
//                        xList.add(x);
//                    }
//                    cItem.setArea(xList);
//                    citys.add(cItem);
//                }
//                area.setCity(citys);
//                list.add(area);
//            }
//            System.out.println(JSON.toJSON(list));
//        } catch (Exception e) {
//            e.printStackTrace();
//            ;
//        }
//    }
//
//    public static ResultSet getConn(String sql) {
//        Connection con = null;
//        String url = "jdbc:mysql://mysql.dev.xianglin.com:3306/xlStation?useUnicode=true&amp;characterEncoding=utf-8&amp;allowMultiQueries=true";
//        String username = "xldbuser";
//        String password = "xldkfagsd";
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            con =
//                    DriverManager.getConnection(url, username, password);
//            return con.createStatement().executeQuery(sql);
//        } catch (Exception se) {
//            se.printStackTrace();
//            System.out.println("数据库连接失败！");
//        }
//        return null;
//    }
//
//    static class Area {
//        String name;
//        List<City> city;
//
//        public Area() {
//        }
//
//        public Area(String name) {
//            this.name = name;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public List<City> getCity() {
//            return city;
//        }
//
//        public void setCity(List<City> city) {
//            this.city = city;
//        }
//    }
//
//    static class City{
//        String name;
//        List<String> area;
//
//        public City() {
//        }
//
//        public City(String name) {
//            this.name = name;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public List<String> getArea() {
//            return area;
//        }
//
//        public void setArea(List<String> area) {
//            this.area = area;
//        }
//    }
}
