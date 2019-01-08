package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.AppArticleTip;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * app 动态
 */
public interface AppArticleTipDAO extends BaseDAO<AppArticleTip> {

    /**按条件（分页）查询
     * @param paras
     * @return
     */
    List<AppArticleTip> selectArticleTipList(Map<String, Object> paras);

    /**按条件查询数量
     * @param paras
     * @return
     */
    Integer selectArticleTipCount(Map<String, Object> paras);

    /**
     * 按条件查询数量(包括已删除)
     * @param paras
     * @return
     */
    Integer queryArticleTipCountIsDeleted(Map<String, Object> paras);

    /**更新阅读状态为已读
     * @param toPartyId
     * @param tipType
     * @return
     */
    int updateReadStatus(@Param("toPartyId") Long toPartyId, @Param("tipType")String tipType);

    /**批量删除关联提醒
     * @param articleIds
     * @return
     */
    int batchDelete(List<Long> articleIds);

    /**
     * 查询消息提醒，排除已删除的原微博
     * @param paras
     * @return
     */
    List<AppArticleTip> queryArticleTipByParam(Map<String, Object> paras);

    /**
     * 查询列表，删除的也查出来
     * @param paras
     * @return
     */
    List<AppArticleTip> selectArticleTipListByParam(Map<String, Object> paras);
}
