package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.dal.dataobject.User;
import com.xianglin.appserv.common.util.EmojiEscapeUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserDAO extends BaseDAO<User> {

    public List<User> query(Map<String, Object> paras);

    public int unBindDevice(@Param("deviceId") String deviceId, @Param("partyId") Long partyId);

    /**根据partyIds批量查询用户
     * @param partyIds
     * @return
     */
    List<User> selectUser(List<Long> partyIds);

    /**
     * 根据partyId集合查询昵称
     *
     * @param partyIds
     * @return
     */
    List<Map<String, Object>> getNickNameByPartyId(List<Long> partyIds);

    /**
     * 根据partyId查询
     *
     * @param partyId
     * @return
     */
    User selectByPartyId(@Param("partyId") Long partyId);

    /**
     * 根据手机号查询
     *
     * @param mobilePhone
     * @return
     */
    User selectByMobilePhone(@Param("mobilePhone") String mobilePhone);

    /**
     * 根据deviceId查询
     *
     * @param deviceId
     * @return
     */
    User selectByDeviceId(@Param("deviceId") String deviceId);

    /**
     * 年报数据查询
     *
     * @param partyId
     * @return
     */
    Map queryAnnualReport(@Param("partyId") Long partyId);

    /**
     * 查询最新发布动态的三个用户
     * @param param
     * @return
     */
    List<User> selectNewArticleUser(Map<String,Object> param);
}
