/**
 * 
 */
package com.xianglin.appserv.biz.shared;

import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.dal.dataobject.AppGroup;
import com.xianglin.appserv.common.dal.dataobject.AppGroupApply;
import com.xianglin.appserv.common.dal.dataobject.AppGroupMember;
import com.xianglin.appserv.common.dal.dataobject.AppGroupNotice;
import com.xianglin.appserv.common.service.facade.model.GroupDTO;
import com.xianglin.appserv.common.service.facade.model.GroupMemberDTO;
import com.xianglin.appserv.common.service.facade.model.GroupOperationRequest;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.UserFigureIdDTO;
import com.xianglin.appserv.common.service.facade.model.vo.MemberVo;
import com.xianglin.appserv.common.service.facade.model.vo.OrganizeNoticeVo;
import com.xianglin.appserv.common.service.facade.model.vo.OrganizeVo;

/**
 * 群组处理器
 * 
 * @author Yao 2016年2月25日上午11:09:53
 */
public interface GroupManager {


    AppGroup Group(long id);

    List<AppGroupNotice> queryNotices(Map<String,Object> paras);

    List<AppGroupMember> queryMembers(Map<String,Object> paras);

    List<AppGroup> queryGroup(Map<String, Object> paras);

    AppGroupMember groupMember(long id);

    Boolean deleteMember(long id);

    Boolean insertApply(AppGroupApply appGroupApply);

    List<AppGroupApply> queryGroupApply(Map<String, Object> paras);

    AppGroupApply groupApply(Long id);

    Boolean confirmApply(AppGroupApply appGroupApply);

    Boolean create(AppGroup appGroup);

    Boolean updateGroup(AppGroup appGroup);

    AppGroupNotice GroupNotice(Long id);

    Boolean publishNotice(AppGroupNotice appGroupNotice);

    List<AppGroupNotice> queryGroupNitice(Map<String, Object> paras);

    Boolean insertGroupMeber(AppGroupMember appGroupMember);

    Boolean batchDeleteMember(Map<String, Object> paras);

    List<AppGroupMember> queryFollowAndQueryGroup(Map<String, Object> paras);

    Boolean insertExceptMember(AppGroupMember appGroupMember);

    List<AppGroup> queryGroupLikeName(Map<String, Object> paras);

    Boolean updateGroupMember(AppGroupMember build);

    int queryGroupMemberCount(Map<String, Object> paras);

    int queryGroupCount(Map<String, Object> paras);

    Boolean deleteGroupApply(AppGroupApply appGroupApply);

    List<AppGroup> queryGroupByParas(Map<String, Object> paras);

    List<AppGroupMember> queryAppMemberByParas(Map<String, Object> paras);

    int queryCountByParam(Map<String, Object> paras);

    int queryGroupMemberCountByParam(Map<String, Object> paras);

    int queryGroupApplyCountByParas(Map<String, Object> paras);

    Boolean updateGroupApply(AppGroupApply build);

    /**
     *查询群成员带删除的数据
     * @param paras
     * @return
     */
    List<AppGroupMember> queryMembersV2(Map<String, Object> paras);

    List<AppGroupApply> queryGroupApplyByParas(Map<String, Object> paras);
}
