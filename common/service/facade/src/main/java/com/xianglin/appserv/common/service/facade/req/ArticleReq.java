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
public class ArticleReq extends PageReq {

    private Long articleId;

    private Long partyId;

    private Long groupId;

    private String articleType;

    private String nickName;
    
    private String showName;

    private String orderBy;

    private String queryKey;

    private String startDate;

    private String endDate;

    private String province;

    private String city;

    private String county;

    private String town;

    private String village;
    
    private Long lastId;
    
    private String followType;//关注类型 FOLLOW 关注  CITY 本市 COUNTY 县 TOWN 镇
    
    private Boolean isExcludeShareUrl=true;//是否排除分享的数据，仅电商需要排除
    
    private String type;//应用或微博 应用：ICON 微博：ARTICLE 全部ALL

    /**
     * 话题关键字
     */
    private String topic;

    private String recSign;//是否推荐
    
    private String phone;//发布人的手机号
}
