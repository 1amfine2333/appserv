package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;
import java.util.Map;

import com.xianglin.appserv.common.dal.dataobject.Msg;
import com.xianglin.appserv.common.dal.dataobject.Page;
import com.xianglin.appserv.common.dal.dataobject.UserMsg;
import org.apache.ibatis.annotations.Param;

public interface MsgDAO extends BaseDAO<Msg> {

    /**
     * @param paras
     * @return
     */
    public Integer queryCount(Map<String, Object> paras);

    /**
     * @param paras
     * @return
     */
    public List<Msg> query(Map<String, Object> paras);

    /**
     * 查询消息，
     *
     * @param paras
     * @return
     */
    public List<Map<String, Object>> queryMap(Map<String, Object> paras);

    /**
     * 插入新闻，根据MSG_SOURCE_URL不重复
     * 仅用于爬取新闻
     * @param msg
     * @return
     */
    public int insertExceptUrl(Msg msg);

    /**
     * 查询用户消息
     *
     * @param paras
     * @param page
     * @return
     */
    List<Msg> queryNews(@Param("paras") UserMsg paras, @Param("page") Page page);

    /**
     * 查询推荐消息
     *
     * @param deviceId
     * @param msgTags，新闻类型合集
     * @param pageSize
     * @return
     */
    List<Msg> queryRecommenNews(@Param("deviceId")String deviceId,@Param("msgTags")List<String> msgTags,@Param("pageSize") int pageSize);

    /**
     * 推荐视频查近三天6条按销量
     * @param paras
     * @return
     */
    List<Msg> queryMsgByparam(Map<String, Object> paras);

    /**同步消息的操作数
     * @param id
     * @return
     */
    Integer updateOperateNum(Long id);
}
