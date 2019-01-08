/**
 * 
 */
package com.xianglin.appserv.common.util;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.fala.session.Session;
import com.xianglin.fala.session.SessionRepository;
import com.xianglin.gateway.common.service.spi.util.GatewayRequestContext;

/**
 * Session帮助类
 * 
 * @author pengpeng 2016年2月25日下午2:38:52
 */
public class SessionHelper {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(SessionHelper.class);

	/** ThreadLocal */
	private static ThreadLocal<Session> context = new ThreadLocal<Session>() {
		public Session initialValue() {
			return null;
		}
	};

	/** sessionRepository */
	private SessionRepository<Session> sessionRepository;

	/**
	 * 清理
	 * 
	 */
	public static void clear() {
		context.remove();
	}

	/**
	 * 取得session
	 * 
	 * @return
	 */
	public Session getSession() {
		Session session = context.get();
		if (session == null) {
			String sessionId = GatewayRequestContext.getSessionId();
			if (StringUtils.isEmpty(sessionId)) {
				throw new RuntimeException("sessionId is empty!");
			}
			logger.debug("sessionIdFromGatewayRequestContext:{}", sessionId);
			session = sessionRepository.getSession(sessionId);
			if (session == null) {
				throw new RuntimeException("can not get session! sessionId:" + sessionId);
			}
			context.set(session);
		}
		return session;
	}

    /**
     * 根据sessionId取得session
     *
     * @return
     */
    public Session getSessionById(String sessionId) {
        Session session = context.get();
        if (session == null) {
            if (StringUtils.isEmpty(sessionId)) {
                throw new RuntimeException("sessionId is empty!");
            }
            logger.debug("sessionIdFromGatewayRequestContext:{}", sessionId);
            session = sessionRepository.getSession(sessionId);
            if (session == null) {
                throw new RuntimeException("can not get session! sessionId:" + sessionId);
            }
            context.set(session);
        }
        return session;
    }
	
	/**
	 * 根据值类型取值
	 * 取值，如果session为null，返回null，
	 * @param key
	 * @param type
	 * @return
	 */
	public <T> T getSessionProp(String key,Class<T> type) {
		try {
			Session session = context.get();
			if(session == null){
				String sessionId = GatewayRequestContext.getSessionId();
				logger.debug("sessionIdFromGatewayRequestContext:{}", sessionId);
				session = sessionRepository.getSession(sessionId);
			}
			if(session != null){
				context.set(session);
				Set<String> names = session.getAttributeNames();
				Object o = session.getAttribute(key);
				if(StringUtils.equals(type.getSimpleName(), "String")){
					return (T)session.getAttribute(key);
				}
				return session.getAttribute(key, type);
			}
		} catch (Exception e) {
			logger.error("get session arrt ",e);
		}
		return null;
	}

	/**取gateway参数
	 * @param key
	 * @return
	 */
	public String getGatewayProp(String key){
		return GatewayRequestContext.getRequestProperty(key,"");
	}

	/**
	 * 保存session
	 * 
	 * 
	 * @param session
	 */
	public void saveSession(Session session){
		
		sessionRepository.save(session);
	}

	/**保存本地session 进攻测试使用
	 * @param session
	 */
	public void saveLocalSesson(Session session){
		context.set(session);
	}

	public Session createRedisSession(String sessionId){
		return sessionRepository.createSession(sessionId);
	}

	public Session getSessionFromRedis(String sessionId){
		return sessionRepository.getSession(sessionId);
	}
	/**
	 * 删除过期session
	 * @param sessionId
	 */
	public void removeSession(String sessionId){
		logger.info("删除过期session，id:{}",sessionId);
		sessionRepository.delete(sessionId);
	}
	/**
	 * @param sessionRepository
	 *            the sessionRepository to set
	 */
	public void setSessionRepository(SessionRepository<Session> sessionRepository) {
		this.sessionRepository = sessionRepository;
	}
}
