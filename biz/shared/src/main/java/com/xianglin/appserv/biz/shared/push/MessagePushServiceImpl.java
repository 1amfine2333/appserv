/**
 *
 */
package com.xianglin.appserv.biz.shared.push;

import com.xianglin.appserv.common.dal.daointerface.AppPushDAO;
import com.xianglin.appserv.common.dal.daointerface.PropExtendDAO;
import com.xianglin.appserv.common.dal.daointerface.UserMsgDAO;
import com.xianglin.appserv.common.dal.dataobject.AppPush;
import com.xianglin.appserv.common.dal.dataobject.UserMsg;
import com.xianglin.appserv.common.service.facade.model.enums.EnumKeyValue;
import com.xianglin.appserv.common.service.facade.model.enums.UserEnum;
import com.xianglin.appserv.common.service.facade.model.enums.YesNo;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.SysConfigUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 消息推送服务
 *
 * @author wanglei 2016年9月26日下午3:28:08
 */
@Service
public class MessagePushServiceImpl implements MessagePushService {

    private static final Logger logger = LoggerFactory.getLogger(MessagePushServiceImpl.class);


    @Autowired
    private AppPushDAO appPushDAO;

    @Autowired
    private UserMsgDAO userMsgDAO;

    @Resource(name = "pushMap")
    private Map<String, MessagePusher> pushMap;

    @Autowired
    private PropExtendDAO propExtendDAO;

    /**
     * @see com.xianglin.appserv.biz.shared.push.MessagePushService#push(com.xianglin.appserv.common.service.facade.model.vo.MsgVo)
     */
    @Override
    @Async("asyncExecutor")
    public void push(MsgVo msg) {
        Integer startPage = 1;
        Integer pageSize = 100;
        Map<String, Object> paras = DTOUtils.queryMap();
        paras.put("msgId", msg.getId());
        paras.put("pageSize", pageSize);
        List<UserMsg> umList = userMsgDAO.select(paras);

        List<String> dids = new ArrayList<>(pageSize);
        Map<String, Set<String>> pushs = new HashMap<>();

        Map<String, Object> pushPara = DTOUtils.queryMap();
        pushPara.put("pageSize", pageSize);
        while (umList.size() > 0) {
            dids = umList.stream().filter(v -> StringUtils.isNotEmpty(v.getDeviceId()) && !StringUtils.equals(v.getPushSign(), YesNo.Y.name()))
                    .filter(v -> {
                        return v.getPartyId() == null
                                | StringUtils.contains(SysConfigUtil.getStr("PUSH_EXCLUDE_TYPE"), msg.getMsgType())
                                | Optional.ofNullable(propExtendDAO.selectProp(v.getPartyId(), EnumKeyValue.User.class.getSimpleName(), EnumKeyValue.User.PUSH_SWITCH.name()))
                                .map(in -> UserEnum.pushType.OPEN.checkStatus(in.getValue()
                                    )).orElse(true);
                    })
                    .map(v -> v.getDeviceId()).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(dids)) {
                pushPara.put("deviceIds", dids);
                List<AppPush> pushList = appPushDAO.query(pushPara);
                if (CollectionUtils.isNotEmpty(pushList)) {
                    for (AppPush pack : pushList) {
                        if (!pushs.containsKey(pack.getPushType())) {
                            pushs.put(pack.getPushType(), new HashSet<String>());
                        }
                        if (StringUtils.isNotEmpty(pack.getPushToken())) {
                            pushs.get(pack.getPushType()).add(pack.getPushToken());
                        }
                    }
                    for (String pushType : pushs.keySet()) {
                        if (pushMap.containsKey(pushType) && CollectionUtils.isNotEmpty(pushs.get(pushType))) {
                            pushMap.get(pushType).push(msg, pushs.get(pushType));
                        } else {
                            logger.debug("unsupport push type : {},", pushType);
                        }
                    }
                    dids.clear();
                    pushs.clear();
                }
            }

            if (umList.size() < pageSize) {
                break;
            }
            paras.put("startPage", ++startPage);
            umList = userMsgDAO.select(paras);
        }
    }
}
