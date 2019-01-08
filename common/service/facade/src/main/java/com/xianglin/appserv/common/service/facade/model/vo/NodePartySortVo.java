package com.xianglin.appserv.common.service.facade.model.vo;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/11/7
 * Time: 13:55
 */
public class NodePartySortVo extends PageResp {


    private List<Map<String, Object>> nodePartySorts;

    public List<Map<String, Object>> getNodePartySorts() {
        return nodePartySorts;
    }

    public void setNodePartySorts(List<Map<String, Object>> nodePartySorts) {
        this.nodePartySorts = nodePartySorts;
    }
}
