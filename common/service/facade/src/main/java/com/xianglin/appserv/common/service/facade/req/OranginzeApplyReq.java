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
public class OranginzeApplyReq extends PageReq {

    private String startDay;//开始时间
    
    private String endDay; //结束时间
    
    private String status; //处理状态


}
