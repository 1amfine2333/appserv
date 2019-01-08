/**
 * 
 */
package com.xianglin.appserv.common.service.facade;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.ContactsDTO;
import com.xianglin.appserv.common.service.facade.model.ContactsRelationRequest;
import com.xianglin.appserv.common.service.facade.model.Response;

/**
 * 联系人相关服务
 * 
 * @author pengpeng 2016年2月18日下午6:24:45
 */
public interface ContactsService {

	/**
	 * 添加联系人请求
	 * 
	 * @param contactsReationRequest
	 *            联系人关系请求
	 * @return
	 */
	Response<Boolean> add(ContactsRelationRequest contactsReationRequest);

	/**
	 * 二维码添加联系人请求
	 * 
	 * @param contactsQRCodeRequest
	 * @return
	 */
	Response<String> addByQRCode(String figureId, String token);

	/**
	 * 修改联系人信息
	 * 
	 * @param figureId
	 *            当前用户所使用的身份角色唯一标识
	 * @param contactsUserId
	 *            要修改的联系人的用户唯一标识
	 * @param contactsFigureId
	 *            要修改的联系人的身份角色唯一标识
	 * @param remarkName
	 *            联系人备注名
	 * @return
	 */
	Response<Boolean> update(String figureId, String contactsUserId, String contactsFigureId, String remarkName);

	/**
	 * 查询指定的联系人信息
	 * 
	 * @param contactsUserId
	 * @param contactsFigureId
	 * @return
	 */
	Response<List<ContactsDTO>> getByContacts(String contactsUserId, String contactsFigureId);

	/**
	 * 获取当前用户的所有身份角色的联系人列表
	 * 
	 * @return
	 */
	Response<List<ContactsDTO>> list();

	/**
	 * 获取当前用户的指定身份角色的联系人列表
	 * 
	 * @param figureId
	 *            当前用户身份角色唯一标识
	 * @return
	 */
	Response<List<ContactsDTO>> list(String figureId);

	/**
	 * 移入黑名单
	 * 
	 * @param figureId
	 *            当前用户身份角色唯一标识
	 * @param contactsUserId
	 *            联系人用户唯一标识
	 * @param contactsFigureId
	 *            联系人身份角色唯一标识
	 * @return
	 */
	Response<Boolean> moveIntoBlacklist(String figureId, String contactsUserId, String contactsFigureId);

	/**
	 * 移出黑名单
	 * 
	 * @param figureId
	 *            当前用户身份角色唯一标识
	 * @param contactsUserId
	 *            联系人用户唯一标识
	 * @param contactsFigureId
	 *            联系人身份角色唯一标识
	 * @return
	 */
	Response<Boolean> moveOutofBlacklist(String figureId, String contactsUserId, String contactsFigureId);

	/**
	 * 查找指定角色的共同联系人
	 * 
	 * @param figureId
	 * @param otherFigureId
	 * @return
	 */
	Response<List<String>> sameContactsByFigureId(String figureId, String otherFigureId);

	/**
	 * 查找共同联系人
	 * 
	 * @param figureId
	 * @param otherFigureId
	 * @return
	 */
	Response<List<String>> sameContacts(String otherFigureId);
}
