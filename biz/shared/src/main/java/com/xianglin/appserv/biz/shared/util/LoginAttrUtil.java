package com.xianglin.appserv.biz.shared.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.appserv.common.service.facade.model.vo.AccountNodeManagerVo;
import com.xianglin.appserv.common.service.facade.model.vo.NodeVo;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.cif.common.service.facade.vo.PersonVo;
import com.xianglin.fala.session.Session;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/10/31
 * Time: 16:05
 */
public class LoginAttrUtil {

    private static Logger logger = LoggerFactory.getLogger(LoginAttrUtil.class);

    private SessionHelper sessionHelper;
/*    private void initSessionHelper(){
        logger.info("=====================");
        LoginAttrUtil.sessionHelperStatic = sessionHelper;l
    }*/

    public  Long getPartyId(){

        //Session session = sessionHelperStatic.getSession();
        Session session = sessionHelper.getSession();


        String userId = session.getAttribute(SessionConstants.PARTY_ID);

        if(userId != null){
            return  Long.valueOf(userId);
        }
        return null;
    }


    public Long getNodePartyId(){
        Session session = sessionHelper.getSession();
        String upId = session.getAttribute(SessionConstants.node_party_id);

        if(upId != null){
            return  Long.valueOf(upId);
        }
        logger.warn("get nodePartyId from session is empty !sessionId:{} , userId :{} ", session.getId(), upId);

        return null;
    }


//    public PersonVo getPersonVo(){
//        Session session = sessionHelper.getSession();
//        return session.getAttribute(SessionConstants.CIF_PERSON_INFO,PersonVo.class);
//    }
    
    public <T> void setSessionVo(String sessionKey ,T sessionVo){
    	Session session = sessionHelper.getSession();
    	session.setAttribute(sessionKey,sessionVo);
        sessionHelper.saveSession(session);
    }

    public String getSessionStr(String key){
        return sessionHelper.getSession().getAttribute(key);
    }

    public AccountNodeManagerVo getAccountNodeManager(){
     return   sessionHelper.getSession().getAttribute(SessionConstants.XL_QY_USER,AccountNodeManagerVo.class);
    }
    
    public void setAccountNodeManager(AccountNodeManagerVo accountNodeManagerVo){
    	Session session = sessionHelper.getSession();
    	session.setAttribute(SessionConstants.XL_QY_USER,accountNodeManagerVo);
        sessionHelper.saveSession(session);
    }

    public NodeVo getNodeInfo(){
        return sessionHelper.getSession().getAttribute(SessionConstants.NODE_INFO,NodeVo.class);
    }

    public String getClientVersion(){
        return sessionHelper.getSession().getAttribute(SessionConstants.CLIENT_VERSION);
    }
    public SessionHelper getSessionHelper() {
        return sessionHelper;
    }

    public void setSessionHelper(SessionHelper sessionHelper) {
        this.sessionHelper = sessionHelper;
    }
}
