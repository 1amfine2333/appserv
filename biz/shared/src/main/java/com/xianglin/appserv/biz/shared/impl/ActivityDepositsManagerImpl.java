package com.xianglin.appserv.biz.shared.impl;

import com.alibaba.fastjson.JSONObject;
import com.xianglin.appserv.biz.shared.ActivityDepositsManager;
import com.xianglin.appserv.common.dal.daointerface.ActivityDepositsDAO;
import com.xianglin.appserv.common.dal.dataobject.ActivityDeposits;
import com.xianglin.appserv.common.service.facade.model.AppPrizeUserRelDTO;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.util.*;
import com.xianglin.xlnodecore.common.service.facade.NodeService;
import com.xianglin.xlnodecore.common.service.facade.req.NodeReq;
import com.xianglin.xlnodecore.common.service.facade.resp.NodeResp;
import com.xianglin.xlnodecore.common.service.facade.vo.NodeVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Service
public class ActivityDepositsManagerImpl implements ActivityDepositsManager {

    private final Logger logger = LoggerFactory.getLogger(ActivityDepositsManagerImpl.class);

    @Autowired
    private ActivityDepositsDAO activityDepositsDAO;

    @Autowired
    private NodeService nodeService;

    @Override
    public ActivityDeposits queryInitDeposits(Long partyId) {

        //查询基业信息
        ActivityDeposits deposits = null;
        NodeReq req = new NodeReq();
        NodeVo nv = new NodeVo();
        nv.setNodeManagerPartyId(partyId);
        req.setVo(nv);
        NodeResp nodeResp = nodeService.queryNodeInfoByNodeManagerPartyId(req);
        if (nodeResp.getVo() != null) {
            BigDecimal current = nodeResp.getVo().getCurrentBalance();
            if (current == null) {
                current = BigDecimal.ZERO;
            }
            BigDecimal gaol = calculationGaol(current);

            deposits = activityDepositsDAO.queryByPairyId(partyId);
            if (deposits == null) {
                deposits = new ActivityDeposits();
                deposits.setPartyId(partyId);
                deposits.setStartAchieve(current);
                deposits.setCurrentAchieve(current);
                deposits.setGoalAchieve(gaol);
                deposits.setRewardSign(Constant.ActivityDepositsStatus.F.name());
                activityDepositsDAO.insert(deposits);
            } else {
                deposits.setCurrentAchieve(current);
                if(StringUtils.equals(deposits.getRewardSign(),Constant.ActivityDepositsStatus.F.name())){//未到达目标才会更新进度
                    if (StringUtils.equals(deposits.getRewardSign(), Constant.ActivityDepositsStatus.F.name())) {
                        if (deposits.getCurrentAchieve().compareTo(deposits.getGoalAchieve()) >= 0) {
                            deposits.setRewardSign(Constant.ActivityDepositsStatus.N.name());
                        }
                    }
                }
                activityDepositsDAO.updateByPrimaryKeySelective(deposits);
            }
        }
        return deposits;
    }

    @Override
    public ActivityDeposits rewardDeposits(Long partyId) {

        ActivityDeposits deposits = activityDepositsDAO.queryByPairyId(partyId);
        if (StringUtils.equals(deposits.getRewardSign(), Constant.ActivityDepositsStatus.N.name())) {
            try {
                // 发送奖励
                Map<String, String> param = new HashMap<>();
                String app_key = "xianglin".concat(DateUtils.formatDate(DateUtils.getNow(), DateUtils.DATE_TPT_TWO))
                        .concat("@#_$&");
                param.put("party_id", partyId + "");
                param.put("type", SysConfigUtil.getStr("activity.deposits.rewardTYpe"));
                param.put("amount", SysConfigUtil.getStr("activity.deposits.amount"));
                app_key = app_key.concat(SHAUtil.getSortString(param));
                param.put("app_key", app_key);
                param.put("sign", SHAUtil.shaEncode(MD5.encode(app_key)));
                String json = HttpUtils.executePost(PropertiesUtil.getProperty("luckwheel.coupon.url"), param, 2000);
                deposits.setRewardResult(json);
                JSONObject object = JSONObject.parseObject(json);
                if ("1".equals(object.getString("error_code"))) {
                    deposits.setRewardSign(Constant.ActivityDepositsStatus.Y.name());
                    deposits.setRewardResult("已经发放奖励");
                }
            } catch (Exception e) {
                deposits.setRewardSign(Constant.ActivityDepositsStatus.N.name());
                deposits.setRewardResult(e.getMessage());
                deposits.setRewardResult("发放奖励失败");
            }
            activityDepositsDAO.updateByPrimaryKeySelective(deposits);
        }
        return deposits;
    }

    /**
     * 计算目标业绩
     *
     * @param current
     * @return
     */
    private BigDecimal calculationGaol(BigDecimal current) {
        if (current.compareTo(new BigDecimal("100000")) <= 0) {
            return new BigDecimal("10000").add(current);
        } else {
            return current.multiply(new BigDecimal("0.1")).add(current);
        }
    }

}
