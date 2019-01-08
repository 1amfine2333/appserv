package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

/**
 * Created by wanglei on 2017/9/19.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VillageManagerVo extends BaseVo{

   private Boolean isVilageManager; //是否是村务管理员
   
   private Boolean isGroupManager; //是否是群管理员
   
   private Long groupId; //群id

   private String ryGroupId; //融云群id

   private Long villageId; //村务id
}
