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
public class UserFeedbackReq extends PageReq {

    /**
     * 内容
     */
   private String content;

    /**
     * 状态 N未处理  Y已处理
     */
   private String status;

    /**
     * 开始时间
     */
   private String startDate;
   

    /***
     * 结束时间
     */
   private String endDate;

   

    

}
