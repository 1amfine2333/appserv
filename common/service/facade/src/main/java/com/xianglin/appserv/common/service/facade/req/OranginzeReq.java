package com.xianglin.appserv.common.service.facade.req;/**
 * Created by wanglei on 2017/2/20.
 */

import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import lombok.*;

/**
 * @author
 * @create 2017-02-20 16:06
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OranginzeReq extends PageReq {

    private Long id;

    /**
     *组织名称
     */
    private String name;


    /**
     *组织地址信息
     */
    private String district;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 状态
     * Y开通
     * N未开通
     */
    private String status;


    /***
     * 是否有管理员
     */
    
    private Boolean isManager = false;


}
