package com.xianglin.appserv.common.dal.daointerface;

import com.xianglin.appserv.common.util.EmojiEscapeUtil;

/**
 * @param <T>
 * @author wanglei 2016年8月12日上午9:49:25
 */
public interface BaseDAO<T> {

    int deleteByPrimaryKey(Long id);

    int insert(T t);


    T selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(T t);

}