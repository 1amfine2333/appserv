package com.xianglin.appserv.common.dal.daointerface;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xianglin.appserv.common.dal.dataobject.AppArticleTopic;
import com.xianglin.appserv.common.dal.dataobject.AppClientLogLogin;
import org.springframework.stereotype.Repository;


/**
 * 动态主题
 */
@Repository
public interface AppArticleTopicDAO extends BaseMapper<AppArticleTopic> {

}
