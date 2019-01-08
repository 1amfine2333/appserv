package com.xianglin.appserv.common.service.facade.app;

import com.xianglin.appserv.common.service.facade.model.MyInfoRowDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.AccountNodeManagerVo;

import java.util.Map;

/**
 * 个人信息服务
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/10/31
 * Time: 14:47
 */
public interface PersonInfoService {

    /**
     * 是否开通资金账户
     * @param partyId
     * @return
     */
    Response<Boolean> isExistPartyAttrAccount(Long partyId);

    /**
     * 是否设置交易密码
     * @param partyId
     * @return
     */
    Response<Boolean> isSetTradePassword(Long partyId);


    /**
     * 查询个人信息
     * @param partyId
     * @return
     */
    Response<AccountNodeManagerVo> getPersonInfo(Long partyId);

    /** 查询认证级别
     * @return
     */
    Response<String> getVertifyLevel();

    /**
     * 资金账户是否显示tip
     * @param partyId
     * @return
     */
    Response<String> isShowAccountTips(Long partyId);

    /**
     * 我的页面行信息
     * 配置每行是否展示及提示信息是否展示
     * @since v1.3.0
     * @return
     */
    Response<MyInfoRowDTO> getRowInfo();

}
