/**
 * 
 */
package com.xianglin.appserv.common.service.facade.app;

import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.GroupVo;
import com.xianglin.appserv.common.service.facade.model.vo.MemberVo;

import java.util.List;

/**
 * 群管理接口
 */
public interface GroupService {

	/** 创建群可拉入群用户查询
	 * 合并组织和联系人
	 * @return
	 */
	Response<List<MemberVo>> createMembers(long id);

	/**创建群时本地联系人分析
	 * @param list
	 * @return
	 */
	Response<List<MemberVo>> createNativeMembers(List<MemberVo> list);

	/**创建群
	 * @param list
	 * @return
	 */
	Response<GroupVo> create(List<MemberVo> list);

	/**查询当前用户所有群
	 * @return
	 */
	Response<List<GroupVo>> list();

	/**更新群信息
	 * @param vo
	 * @return
	 */
	Response<Boolean> update(GroupVo vo);

	/**添加群成员
	 * 从createMembers结果中排除本群成员
	 * @param id
	 * @return
	 */
	Response<List<MemberVo>> addMembers(long id);

	/**批量添加群成员时
	 * 从createNativeMembers
	 * @param list
	 * @return
	 */
	Response<List<MemberVo>> addNativeMembers(List<MemberVo> list, long id);

	/**返回所有群成员
	 * @param id
	 * @return
	 */
	Response<List<MemberVo>> listMembers(long id);

	/**转让群管理
	 * @param partyId
	 * @return
	 */
	Response<Boolean> assignManager(long partyId, long id);

	/** 退出群
	 * @param id
	 * @return
	 */
	Response<Boolean> exit(long id);

	/** 删除群成员
	 * @param memberId
	 * @return
	 */
	Response<Boolean> deleteMember(long memberId, long id);

	/** 批量删除群成员
	 * @param memberIds
	 * @return
	 */
	Response<Boolean> batchDeleteMember(List<Long> memberIds, long id);

    /***
     * 查询群信息
     */
    Response<GroupVo> queryGroup(long id);

    /***
     * 查询群信息
     */
    Response<GroupVo> queryGroupByRUId(String rUId);

    /**
     *解散群 
     */
    Response<Boolean> dismiss(long id);
    
    /**
     * 查询当前用户是否是管理员
     * 支持村、群、组织查询
     * @return
     */
    Response<Boolean> isGroupManager(long id);

    /**
     * 当前用户加入群
     * @param id
     * @return
     */
    Response<Boolean> joinGroup(long id);
}
