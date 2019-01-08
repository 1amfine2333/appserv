/**
 * 
 */
package com.xianglin.appserv.biz.shared.quartz;

import com.xianglin.appserv.biz.shared.util.TimeTaskClusterUtil;
import com.xianglin.appserv.common.dal.daointerface.RedPacketPoolDAO;
import com.xianglin.appserv.common.dal.daointerface.TaskDAO;
import com.xianglin.appserv.common.dal.dataobject.RedPacketPool;
import com.xianglin.appserv.common.service.facade.RedPacketService;
import com.xianglin.appserv.common.service.facade.model.enums.Constant;
import com.xianglin.appserv.common.service.facade.model.vo.RedPacketVo;
import com.xianglin.appserv.common.util.*;
import com.xianglin.appserv.common.util.constant.AppservConstants;
import com.xianglin.appserv.core.service.CoreRedPacketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

/**
 * 
 * 
 * @author zhangyong 2016年10月12日上午9:40:54
 */
public class RedPacketTask {

	private static final Logger logger = LoggerFactory.getLogger(RedPacketTask.class);

	private static String INIT_TASK_ID = "20000";
	private static String START_TASK_ID = "20001";
	private static String DECREASE_TASK_ID = "20002";

	@Autowired
	private TaskDAO taskDAO;

	@Autowired
	private CoreRedPacketService coreRedPacketService;
	@Autowired
	private RedPacketPoolDAO poolDAO;

	public static final String task_decrease_step="task.decrease.step";
	/**
	 * 生成红包,每日12点执行
	 *
	 */

	public void execute() {
		boolean open = coreRedPacketService.isActivityOpen();
		if(!open){
			logger.info("红包活动已停止");
			return;
		}
		Date now = DateUtils.getNow();
		String executeDate = DateUtils.formatDate(now, DateUtils.DATE_TPT_TWO);
		logger.info("start init RedPacketTask , excute date :{}", executeDate);
		int result = taskDAO.updateExcutedByTaskId(AppservConstants.STATUS_EXECUTING, INIT_TASK_ID, executeDate);

		if (result == 0) {
			logger.info("other server is excuting this task !");
			return;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("effectiveDate", DateUtils.formatDate(now, DateUtils.DATE_TPT_TWO));
		params.put("type", Constant.RedPacketType.CASH.name());
		int count = poolDAO.selectCount(params);
		String total_num = SysConfigUtil.getStr(Constant.BusiVisitKey.red_packet_total_num.code,"0");
		logger.info(total_num);
		int init_num = Integer.valueOf(total_num);
		if(init_num  == 0){
			logger.warn("初始化红包数量配置为0，不进行红包活动");
			return;
		}
		if (count == 0) {
			int cashNum = addCash(init_num, now);
			logger.info("日期:{}计划初始化现金红包数量:{},实际初始化现金红包数量：{}", DateUtils.formatDate(now, DateUtils.DATE_FMT), init_num,
					cashNum);
			// 现金红包比例
			/*
			 * Double cash_scale = RandomProbability.getValue(0.00, 1.00); int
			 * cash_num = (int) (init_num * cash_scale);//现金红包个数 int coupon_num
			 * = init_num - cash_num;//优惠券个数
			 */
			// 已开通电商站长可获得红包（60%）或电商券（40%）（概率可调整）
			// 有可能全是未开通电商的，全部是红包券，如果生成红包券少于最大，则可能在抢的过程中，显示有红包，但是点击时提示已抢光
			/*
			 * Date startDayOfMonth = DateUtils.getFirstDayOfCurrentMonth();
			 * Date endDayOfMonth =
			 * DateUtils.getLastTimeOfLastDayOfCurrentMonth(); int days =
			 * DateUtils.daysOfTwo(startDayOfMonth, endDayOfMonth);
			 * 
			 * for (int i = 0; i <= days; i++) { Date effectiveDate =
			 * DateUtils.addDate(startDayOfMonth, i); int cashNum =
			 * addCash(init_num,effectiveDate); // int couponNum =
			 * addCoupon(init_num,effectiveDate);
			 * logger.info("日期:{}计划初始化现金红包数量:{},实际初始化现金红包数量：{}",DateUtils.
			 * formatDate(effectiveDate, DateUtils.DATE_FMT),init_num,cashNum);
			 * }
			 */
		}

		RedPacketVo queryVo = new RedPacketVo();
		queryVo.setEffectiveDate(now);
		queryVo.setStatus(Constant.RedPacketStatus.EFFECTIVE.name());

		List<RedPacketVo> list = coreRedPacketService.getRedPacketList(queryVo);
		int seconds =	DateUtils.getIntervalSeconds(now, DateUtils.getLastestTimeOfDay(now));

		coreRedPacketService.add(RedPacketService.RED_PACKET_TOTAL_NUM , executeDate, String.valueOf(init_num), seconds);

		Map<String, Object> redisMap = new HashMap<>();
		List<String> cashList = new ArrayList<String>();
		List<String> couponList = new ArrayList<String>();
		for (RedPacketVo redPacketVo : list) {
			redisMap.put(redPacketVo.getId().toString(), redPacketVo);
			if (Constant.RedPacketType.CASH.name().equals(redPacketVo.getType())) {
				cashList.add(redPacketVo.getId().toString());
			} else if (Constant.RedPacketType.COUPON.equals(redPacketVo.getType())) {
				couponList.add(redPacketVo.getId().toString());
			}
		}
		coreRedPacketService.add(RedPacketService.RED_ACTIVITY_FLAG, executeDate,  Constant.ActivityStatus.READY.name(), seconds);
//		coreRedPacketService.add(RedPacketService.RED_ACTIVITY_DESC,executeDate,Constant.ActivityResultDesc.TODAY.getDesc(),seconds);
		coreRedPacketService.add(RedPacketService.RED_ACTIVITY_DESC,executeDate,getValue(Constant.BusiVisitKey.activity_today_desc.code,Constant.ActivityResultDesc.TODAY.getDesc()),seconds);
		coreRedPacketService.hmset(RedPacketService.CASH_RED_PACKET_INFO_KEY,executeDate, redisMap, seconds);// 保存所有的红包信息，field为主键id，value为对象
		coreRedPacketService.push(RedPacketService.RED_CASH_ID_KEY,executeDate, cashList,seconds);// 保存所有现金券的id
//		redisUtil.push(RedPacketService.RED_COUPON_ID_KEY, couponList.toArray(new String[couponList.size()]));// 保存所有优惠券的id
		updateTask(executeDate, INIT_TASK_ID);

	}


	/**
	 * 减少红包
	 * 这个要一直运行 ，不能使用更新时间的方法，更新到明天
	 *
	 */
	public void decrease() throws Exception{
		boolean open = coreRedPacketService.isActivityOpen();
		if(!open){
			logger.info("红包活动已停止");
			return;
		}
		if(TimeTaskClusterUtil.isHasClusterExceute(taskDAO, DECREASE_TASK_ID, 5)){
			logger.info("减少红包任务不运行");
			return;
		}
		Date now = DateUtils.getNow();
		String executeDate = DateUtils.formatDate(now, DateUtils.DATE_TPT_TWO);
		String flag = coreRedPacketService.get(RedPacketService.RED_ACTIVITY_FLAG, executeDate);
		
		if(Constant.ActivityStatus.RUNNING.name().equals(flag)){
			int seconds = DateUtils.getIntervalSeconds(now, DateUtils.getLastestTimeOfDay(now));
			logger.info("start decrease , excute date :{}", executeDate);
			// 如果值每秒减少1个，值变为负数时，满足条件，更新红包活动的状态
			Boolean b =coreRedPacketService.isReady(RedPacketService.RED_PACKET_TOTAL_NUM,executeDate,(-1)*Integer.valueOf(getValue(Constant.BusiVisitKey.task_decrease_step.code,"0")), 0, -1);

			if (!b) {// 如果红包数量为0时满足条件，删除活动标识，活动未开始
				coreRedPacketService.deleteCache(executeDate);
				coreRedPacketService.add(RedPacketService.RED_ACTIVITY_FLAG, executeDate,  Constant.ActivityStatus.END.name(), seconds);
//				coreRedPacketService.add(RedPacketService.RED_ACTIVITY_DESC, executeDate,  Constant.ActivityResultDesc.TOMORROR.getDesc(), seconds);
				coreRedPacketService.add(RedPacketService.RED_ACTIVITY_DESC, executeDate,getValue(Constant.BusiVisitKey.activity_tomorror_desc.code,Constant.ActivityResultDesc.TOMORROR.getDesc()), seconds);
			}
		}else{
			logger.info("活动未开始，减少红包任务不执行，当前活动状态:{}",flag);
		}
	
	}

	
	/**
	 * 活动开始
	 * 
	 *
	 */
	public void startActivity() throws Exception{
		boolean open = coreRedPacketService.isActivityOpen();
		if(!open){
			logger.info("红包活动已停止");
			return;
		}
		Date now = DateUtils.getNow();
		String executeDate = DateUtils.formatDate(now, DateUtils.DATE_TPT_TWO);
		logger.info("start startActivity , excute date :{}", executeDate);
		int result = taskDAO.updateExcutedByTaskId(AppservConstants.STATUS_EXECUTING, START_TASK_ID, executeDate);

		if (result == 0) {
			logger.info("other server is excuting this task !");
			return;
		}
		//如果0点执行初始化数量存在，则置为开始
		// 活动时长配置
		if(coreRedPacketService.isExist(RedPacketService.RED_PACKET_TOTAL_NUM, executeDate)){
			String timeout = SysConfigUtil.getStr(Constant.BusiVisitKey.activity_timeout.code,"0");
			logger.info("====startActivity===={},{}",coreRedPacketService.get(RedPacketService.RED_ACTIVITY_DESC, executeDate),coreRedPacketService.get(RedPacketService.RED_ACTIVITY_FLAG, executeDate));
			coreRedPacketService.add(RedPacketService.RED_ACTIVITY_FLAG,executeDate, Constant.ActivityStatus.RUNNING.name(),Integer.valueOf(timeout));// 活动开始，结束为15分钟
//			coreRedPacketService.add(RedPacketService.RED_ACTIVITY_DESC,executeDate, Constant.ActivityResultDesc.RUNNING.getDesc(), 15 * 60);
			coreRedPacketService.add(RedPacketService.RED_ACTIVITY_DESC,executeDate,SysConfigUtil.getStr(Constant.BusiVisitKey.activity_running_desc.code),Integer.valueOf(timeout));
		}
		updateTask(executeDate, START_TASK_ID);
		
	}

	private int addCash(int cashNum, Date effectiveDate) {
		int count = 1;
		int total = 0;
		List<RedPacketVo> list = new ArrayList<>(cashNum);

		while (count <= cashNum) {
			list.add(addRedPacket(Constant.RedPacketType.CASH.name(), BigDecimal.ZERO, "", "", effectiveDate, null));
			if (list.size() == 50) {
				total += insertRedPacketPool(list);
				list.clear();
			}
			count++;
		}
		if (list.size() > 0) {
			total += insertRedPacketPool(list);
		}
		return total;
	}
	
	private void updateTask(String executeDate, String taskId) {
		Date nextDay = DateUtils.addDate(DateUtils.getNow(), 1);
		String nextExecuteDate = DateUtils.formatDate(nextDay, "yyyyMMdd");
		taskDAO.updateEndByTaskId(AppservConstants.STATUS_UNEXECUTED, taskId, executeDate, nextExecuteDate);
		logger.info("end redpacket task ,next excute date : {}", nextExecuteDate);
	}

	private RedPacketVo addRedPacket(String type, BigDecimal amount, String source, String desc, Date effectiveDate,
			Date expiredDate) {
		RedPacketVo vo = null;
		Date now = DateUtils.getNow();
		vo = new RedPacketVo();
		vo.setCreateDate(now);
		vo.setUpdateDate(now);
		vo.setEffectiveDate(effectiveDate);
		if (!Constant.RedPacketType.CASH.name().equals(type)) {
			vo.setExpiredDate(now);
		} else {
			vo.setExpiredDate(expiredDate);
		}
		vo.setIsDeleted("0");
		vo.setSource(source);
		vo.setStatus(Constant.RedPacketStatus.EFFECTIVE.name());
		vo.setType(type);
		vo.setDescription(desc);
		if (Constant.RedPacketType.CASH.name().equals(type)) {
			vo.setAmount(BigDecimal.valueOf(RandomProbability.randomPercent()));// 设置
		} else {
			vo.setAmount(amount);
		}
		return vo;
	}

	private int insertRedPacketPool(List<RedPacketVo> list) {
		int i = 0;
		try {
			List<RedPacketPool> poolList;
			poolList = DTOUtils.map(list, RedPacketPool.class);
			i = poolDAO.batchInsert(poolList);
		} catch (Exception e) {
			logger.error("批量初始化红包池出现问题", e);
		}
		return i;
	}

	public static void main(String[] args) {
		Date startDayOfMonth = DateUtils.getFirstDayOfCurrentMonth();
		Date endDayOfMonth = DateUtils.getLastTimeOfLastDayOfCurrentMonth();
		int days = DateUtils.daysOfTwo(startDayOfMonth, endDayOfMonth);
		System.out.println(days);
		for (int i = 0; i <= days + 1; i++) {
			Date effectiveDate = DateUtils.addDate(startDayOfMonth, i);
			logger.info("date:{}", DateUtils.formatDate(effectiveDate, DateUtils.DATE_TPT_TWO));
			// int cashNum = addCash(init_num,effectiveDate);
			// int couponNum = addCoupon(init_num,effectiveDate);
			// logger.info("计划初始化现金红包数量:{},优惠券数量:{},实际初始化现金红包数量：{},优惠券数量：{}",init_num,init_num,cashNum,couponNum);
		}

	}

	private String getValue(String key,String defaultValue){
		return SysConfigUtil.getStr(key,defaultValue);
	}
}
