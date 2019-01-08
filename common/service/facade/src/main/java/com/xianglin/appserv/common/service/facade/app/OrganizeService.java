package com.xianglin.appserv.common.service.facade.app;

import com.xianglin.appserv.common.service.facade.model.GroupMemberDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.*;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import com.xianglin.appserv.common.service.facade.req.MemberReq;
import com.xianglin.appserv.common.service.facade.req.OranginzeApplyReq;
import com.xianglin.appserv.common.service.facade.req.OranginzeReq;

import java.util.List;

/** 组织管理相关接口
 * Created by wanglei on 2017/9/18.
 */
public interface OrganizeService {

    /** 组织明细
     * @param id
     * @return
     */
    Response<OrganizeVo> organizeDetail(long id);

    /**删除组织成员
     * @param memberId
     * @return
     */
    Response<Boolean> deleteMember(long memberId,long id);

    /**个人组织列表查询
     * @return
     */
    Response<List<OrganizeVo>> listOrganize();

    /**查询当前用户默认组织
     * @return
     */
    Response<OrganizeVo> defaultOrganize();

    /** 群搜索，排除个人已经参加的群
     * @param keyword
     * @return
     */
    Response<List<OrganizeVo>> queryOrganize(String keyword);

    /**申请加入群
     * @param id
     * @return
     */
    Response<Boolean> apply(Long id);

    /**组织加入申请列表
     * @param id
     * @return
     */
    Response<List<OrganizeApplyVo>> listApply(Long id);

    /**确认申请
     * @param id
     * @return
     */
    Response<Boolean> confirmApply(Long id);

    /**添加单个组织成员
     * @param member
     * @return
     */
    Response<Boolean> addMember(MemberVo member);

    /**批量添加组织成员
     * @param members
     * @return
     */
    Response<Boolean> batchAddmember(List<MemberVo> members);

    /**群公告列表查询
     * @param orgnId
     * @return
     */
    Response<List<OrganizeNoticeVo>> queryNoticeList(Long orgnId,PageReq req);

    /** 发布群公告
     * @param vo
     * @return
     */
    Response<Boolean> publishNotice(OrganizeNoticeVo vo);

    /**公告明细
     * @param id
     * @return
     */
    Response<OrganizeNoticeVo> queryNotice(Long id);

    /**转让管理员
     * @param partyId
     * @return
     */
    Response<Boolean> assignManager(Long partyId,Long id);

    /**退出组织
     * @param id
     * @return
     */
    Response<Boolean> exit(Long id);

    /**创建组织
     * @param vo
     * @return
     */
    Response<Boolean> create(OrganizeVo vo);

    /**
     * 发短信邀请好友
     */
    Response<Boolean> sendMessage(String phone);

    /**
     * 单个删除组织申请
     */
    Response<Boolean> deleteApply(Long id);


    /**
     * 查所有的村，按照条件查询
     */
    Response<List<OrganizeVo>> queryOrganizeParas(OranginzeReq oranginzeReq);

    /**
     * 查某个村的成员，按条件查询
     */
    Response<List<MemberVo>>  queryMemberByParas(MemberReq memberReq);

    /**
     * 查某个村的成员，按条件查询
     */
    Response<Integer>  queryMemberCountByParas(MemberVo memberVo);

    /***
     * 设置或取消村的管理员
     */
    
    Response<Boolean> updateVillageUserType(MemberVo memberVo);

    /**
     * 根据村ID查询村管理员
     * @param id
     * @return
     */
    Response<List<String>>  queryVillageManage(Long id);


    /***
     * 查询个人的组织列表V2
     */

    Response<List<OrganizeVo>> listOrganizeV2();

    /***
     * 查询个人的组织数
     */
    Response<Integer> queryVillageCountByParas(OrganizeVo organizeVo);

    /**
     * 返回是否申请试点村
     * @param 
     * @return
     */
    Response<Boolean> isApplyVillage();

    /***
     * 申请试点村
     * @param 
     * @return
     */
    Response<Boolean> ApplyVillage();

    /**
     * 根据条件查询村申请记录 top
     * @param oranginzeApplyReq
     * @return
     */
    Response<List<OrganizeApplyVo>> queryApplyVillageByParas(OranginzeApplyReq oranginzeApplyReq);

    /**
     * 根据条件查询村申请数 top
     * @param oranginzeApplyReq
     * @return
     */
    Response<Integer> queryApplyVillageCountByParas(OranginzeApplyReq oranginzeApplyReq);

    /**
     * 修改申请试点村状态
     * @param organizeApplyVo
     * @return
     */
    Response<Boolean> updateApplyVillage(OrganizeApplyVo organizeApplyVo);

    /**
     * 返回村开通状态
     * @return
     */
    Response<OrganizeApplyVo> openVillageState();

    /**
     * 返回是否是村和群管理员
     * @return
     */
    Response<VillageManagerVo> isVillageAndGroupManager();
}
