package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

/**
 * @author Yungyu
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenealogyLinkVO {

    /**
     * 家谱拥有人partyId
     */
    private Long genealogyOwner;

    /**
     * 家谱拥有人姓名
     */
    private String genealogyOwnerName;

    /**
     * 链接id
     */
    private Long linkId;

    /**
     * 消息接收人
     */
    private Long fromPartyId;

    /**
     * 家谱拥有人头像
     */
    private String headImg;

    /**
     * 卡片logo
     */
    private String logo;

    /**
     * 卡片标语
     */
    private String slogan;

    /**
     * 卡片标题
     */
    private String title;


    /**
     * 链接
     */
    private String link;
}
