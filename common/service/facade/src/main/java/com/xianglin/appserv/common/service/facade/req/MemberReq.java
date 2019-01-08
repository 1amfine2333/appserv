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
public class MemberReq extends PageReq {

    private Long id;

    /**
     *人名称
     */
    private String name;

    /**
     *人名称
     */
    private Long groupId;

   

    

}
