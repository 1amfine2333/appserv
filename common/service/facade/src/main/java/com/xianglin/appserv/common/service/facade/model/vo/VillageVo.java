package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.util.List;

/**
 * Created by wanglei on 2017/9/19.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VillageVo extends BaseVo{

   private GroupVo groupVo; //村信息
   
   private ArticleVo articleVo; //村微博
   
   private ArticleVo broadcast; //村广播

 
}
