/**
 * 
 */
package com.xianglin.appserv.biz.shared.quartz;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.xianglin.appserv.biz.shared.MessageManager;
import com.xianglin.appserv.common.dal.daointerface.GoodWordsDAO;
import com.xianglin.appserv.common.dal.daointerface.MsgDAO;
import com.xianglin.appserv.common.dal.daointerface.TaskDAO;
import com.xianglin.appserv.common.dal.dataobject.GoodWords;
import com.xianglin.appserv.common.dal.dataobject.Msg;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgNewsTag;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgStatus;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgType;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.YESNO;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.DateUtils;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.appserv.common.util.constant.AppservConstants;
import com.xianglin.appserv.core.service.CoreRedPacketService;


/**
 * 自动发送消息
 * 
 * @author wanglei 2016年9月29日下午9:04:44
 */
public class SendMsgTask {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(SendMsgTask.class);

	/** 任务ID */
	private static final String TASK_ID = "10000";
	/**
	 * 活动提醒任务id
	 */
	private static final String NOTICE_TASK_ID = "20004";

	@Autowired
	private TaskDAO taskDAO;

	@Autowired
	private MessageManager messageManager;

	@Autowired
	private GoodWordsDAO goodWordsDao;
	
	@Autowired
	private MsgDAO msgDAO;
	
	@Autowired
	private CoreRedPacketService coreRedPacketService;
	
	private static String MSG_TITLE ="每日一言";
	
	@Value("#{config['MESSAGE_DAILY_SEND_URL']}")
	private String DAILY_SEND_URL;
	
	void execute() {
		try{
			Calendar calendar = Calendar.getInstance();
			String executeDate = DateFormatUtils.format(calendar, "yyyyMMdd");
			logger.info("start SendMsgTask , excute date :{}", executeDate);
			TimeUnit.SECONDS.sleep(RandomUtils.nextInt(30));//任务执行前先暂停
			int result = taskDAO.updateExcutedByTaskId(AppservConstants.STATUS_EXECUTING, TASK_ID, executeDate);
			if (result == 0) {
				logger.info("other server is excuting this task !");
				return;
			}
			MsgVo vo = new MsgVo();
			//
			/* 1,查询一条每日新闻
			 * 2,更新该新闻状态为已读
			 * 3，查看新闻是否有标题图片
			 * 	4，没有：查询一条每日一言，设置每日一言的图片为该图片
			 * 5，发现消息名称为每日新闻
			 * 
			 * 5，如果查询不到新闻，则走每日一言流程，发送标题为每日一言
			 * 
			 * 统一处理，消息内容呢为完整体，图片，文字，和a标签的内容
			 */
			Map<String, Object> paras = DTOUtils.queryMap();
			paras.put("status", MsgStatus.INIT.code);
			paras.put("msgType", MsgType.NEWS.name());
			paras.put("msgTag", MsgNewsTag.TTXW.name());

			List<Msg> newsList = msgDAO.query(paras);
			if(CollectionUtils.isNotEmpty(newsList)){
				Msg msg = newsList.get(0);
				msg.setStatus(MsgStatus.USED.code);
				msgDAO.updateByPrimaryKeySelective(msg);
				
				String contentName = msg.getMsgTitle();
				String contentImg = msg.getTitleImg();
				if(StringUtils.isEmpty(contentImg)){
					GoodWords goodword = getUseGoodword();
					if(goodword != null){
						contentImg = goodword.getImgUrl();
					}
				}
				if(contentName.length() > 100){
					contentName = contentName.substring(0, 100)+"......";
				}
				String content = "<a dataId=‘%s’ href='javascript:'><img src='%s'/><p>%s</p></a>";
				// 拼接content 包含a标签，需要跳转到新闻详细页面
				vo.setMessage(String.format(content,msg.getId(),contentImg,contentName));
				vo.setMsgTitle(MsgType.NEWS.desc);
				sendDailyMsg(vo, executeDate);
			}else{
				GoodWords goodword = getUseGoodword();
				if(goodword !=null){
					String content = "<img src='%s'/><p>%s</p>";
					vo.setMsgTitle(MSG_TITLE);
					vo.setMessage(String.format(content, goodword.getImgUrl(),goodword.getWords()).toString());
					sendDailyMsg(vo, executeDate);
				}else{
					logger.info("there is no ather goodword");
				}
			}
	
			Date nextDay = DateUtils.addDate(calendar.getTime(), 1);
			calendar.setTime(nextDay);
			String nextExecuteDate = DateFormatUtils.format(calendar, "yyyyMMdd");
			taskDAO.updateEndByTaskId(AppservConstants.STATUS_UNEXECUTED, TASK_ID, executeDate, nextExecuteDate);
			logger.info("end SendMsgTask ,next excute date : {}", nextExecuteDate);
		}catch(Exception e){
			logger.error("SendMsgTask",e);
		}
	}
	
	private void sendDailyMsg(MsgVo vo,String executeDate) throws Exception{
		vo.setMsgType(Constant.MsgType.DAILY_TIP.name());
		vo.setUrl(DAILY_SEND_URL+"?day="+executeDate);
		// 拼接content 不好汉a标签
		vo.setCreator("SendMsgTask");
		vo.setIsSave(YESNO.YES);
		vo.setMsgStatus(Constant.MsgStatus.UNREAD.code);
		messageManager.sendMsg(vo);
	}
	
	/**
	 * 红包活动
	 * 
	 *
	 */
	public void sendRedActivityNotice(){
		boolean open = coreRedPacketService.isActivityOpen();
		if(!open){
			logger.info("红包活动已停止");
			return;
		}
		Calendar calendar = Calendar.getInstance();
		String executeDate = DateFormatUtils.format(calendar, "yyyyMMdd");
		logger.info("start sendRedActivityNotice , excute date :{}", executeDate);
		try {
			int result = taskDAO.updateExcutedByTaskId(AppservConstants.STATUS_EXECUTING, NOTICE_TASK_ID, executeDate);
		if (result == 0) {
			logger.info("sendRedActivityNotice other server is excuting this task !");
			return;
		}
		MsgVo vo = new MsgVo();
		vo.setIsDeleted(YESNO.YES.code);
		vo.setMsgStatus(MsgStatus.READED.name());
		vo.setIsSave(YESNO.NO);
		vo.setMsgType(MsgType.REDPACK.name());
		vo.setMsgTitle("活动提醒");
		vo.setUrl(SysConfigUtil.getStr("RED_PACKET_URL")+"?");
		vo.setMessage("活动即将开始，红包、优惠券等你来抢！");
		vo.setExpiryTime(20*60);//8：55分开始，9：15结束
		vo.setLoginCheck(YESNO.YES.code);
		messageManager.sendMsg(vo);
		Date nextDay = DateUtils.addDate(calendar.getTime(), 1);
		calendar.setTime(nextDay);
		String nextExecuteDate = DateFormatUtils.format(calendar, "yyyyMMdd");
		taskDAO.updateEndByTaskId(AppservConstants.STATUS_UNEXECUTED, NOTICE_TASK_ID, executeDate, nextExecuteDate);
		logger.info("end split task ,next excute date : {}", nextExecuteDate);
		} catch (Exception e) {
			logger.error("活动提醒消息发送失败",e);
		}
	}
	
	private GoodWords getUseGoodword() {
		Map<String, Object> param = new HashMap<>();
		param.put("isDeleted", "0");
		param.put("wordsState", "N");
		param.put("startPage", 0);
		param.put("pageSize", 1);
		List<GoodWords> dbList = goodWordsDao.getGoodWords(param);
		if (CollectionUtils.isNotEmpty(dbList)) {
			GoodWords good = dbList.get(0);
			// 更新为已读：
			good.setWordsState("Y");
			good.setUpdateDate(DateUtils.getNow());
			good.setUpdator("SendMsgTask");
			good.setUseDate(DateUtils.formatDate(DateUtils.getNow(), DateUtils.DATE_TPT_TWO));
			goodWordsDao.updateByPrimaryKeySelective(good);
			return good;
		}
		return null;
	}
	
	public static void main(String[] args) {
		String s = "https://appfile.xianglin.cn/file/8";
		String content = "<img src='%s'/><p>%s</p>";
		
		System.out.println(String.format(content, s,"dafdasfsdaf").toString());
	}
}
