package com.xianglin.appserv.common.service.facade.model.enums;

public enum FacadeEnums {

	OK(200000, "正常", "返回正常"),

	SURE(300000, "确定", "返回确定"), 
	UPDATE_FAIL(300001, "异常", "修改失败"),
	UPDATE_INVALID(300002, "异常", "修改无效"),
	DELETE_FAIL(300003, "异常", "删除失败"),
	INSERT_FAIL(300004, "异常", "添加失败"),
	INSERT_INVALID(300005,"异常", "添加无效"), 
	INSERT_DUPLICATE(300006, "异常", "要插入的数据已存在"), 
	UPDATE_DUPLICATE(300007, "异常", "要更新的数据已存在"),

	HAVE_REL_NODE(300008, "异常", "存在引用的站点信息"), 
	HAVE_REL_VILLAGE(300009, "异常", "存在引用的村庄信息"), 
	HAVE_REL_BANKSUBBRANCH(300010, "异常", "存在引用的支行信息"),
	HAVE_CHILD_DISTRICT(300011, "异常", "存在子行政区域信息"),
	ERROR_REPEAT(300012, "异常", "请勿重复发送内容"),

	RETURN_EMPTY(400000, "异常", "返回为空"),
	RETURN_BANKCARD_INFO(400001, "异常", "获取预约办卡详情异常"),
	RETURN_MORE_INTO(400002, "异常", "返回的数据存在多条"),

    /**
     * 用户未登录
     */
    SESSION_INVALD(500004, "用户未登录", "用户未登录"),

	/**
	 * 客户端错误，代码:100~199
	 */
	E_C_USER_INVALID(100001, "无效操作用户", "无效操作用户"),

	ERROR_CHAT_400003(400003, "异常", "发送红包失败"), 
	//ERROR_CHAT_400004(400004, "异常", "该数据或评论已经被删除"), 
	ERROR_CHAT_400005(400005, "异常", "该数据已经被删除"),
	ERROR_CHAT_400006(400006, "异常", "对不起，无法删除"),
	ERROR_CHAT_400007(400007, "异常", "今天已经拆了3个红包啦，明天再来吧~"),
	ERROR_CHAT_400008(400008, "异常", "来晚了，红包已经被领完"),
	ERROR_CHAT_400009(400009, "异常", "发放奖励失败，请稍后重试"),
	ERROR_CHAT_400010(400010, "异常", "没有对应的活动奖励"),
	ERROR_CHAT_400011(400011, "异常", "奖励已经发放完了，明天再来吧"),
	ERROR_CHAT_400012(400012, "异常", "每天最多只能反馈三次哦"),
	ERROR_CHAT_400013(400013, "异常", "发放奖励失败"),
	ERROR_CHAT_400014(400014, "异常", "沒有找到余額調整类别"),
	ERROR_CHAT_400015(400015, "异常", "该账户下有账户明细存在，请删除后再进行此操作~"),
	ERROR_CHAT_400016(400016, "异常", "类别已经存在"),
	ERROR_CHAT_400017(400017, "异常", "账户已经存在"),
	ERROR_CHAT_400018(400018, "异常", "新增类别已到上限"),
    ERROR_CHAT_400019(400019, "异常", "您不是管理员，无权删除其他成员"),
    ERROR_CHAT_400020(400020, "异常", "请先转让管理员"),
    ERROR_CHAT_400021(400021, "异常", "该组织不存在"),
    ERROR_CHAT_400022(400022, "异常", "该成员不存在"),
    ERROR_CHAT_400023(400023, "异常", "组织管理员无法退出该组织"),
    ERROR_CHAT_400024(400024, "异常", "您不是管理员，无权添加成员"),
    ERROR_CHAT_400025(400025, "异常", "该群不存在"),
    ERROR_CHAT_400026(400026, "异常", "您不是群主，无权删除其他成员"),
    ERROR_CHAT_400027(400027, "异常", "您不是群主，无权修改群信息"),
    ERROR_CHAT_400028(400028, "异常", "群主无法退出群"),
	ERROR_CHAT_400029(400029, "异常", "活动已经结束"),
    ERROR_CHAT_400030(400030, "异常", "您不是群主，无权转让群主"),
    ERROR_CHAT_400031(400031, "异常", "您不是管理员，无权转让管理员"),
    ERROR_CHAT_400032(400032, "异常", "该群不存在"),
    ERROR_CHAT_400033(400033, "异常", "您不是群主，无权删除其他成员"),
    ERROR_CHAT_400034(400034, "异常", "您不是群主，无权解散群"),
    ERROR_CHAT_400035(400035, "异常", "不能转给自己"),
    ERROR_CHAT_400036(400036, "异常", "请先转让群主"),
    ERROR_CHAT_400037(400037, "异常", "群名称不能超过30个字符"),
    ERROR_CHAT_400038(400038, "异常", "成员已经存在"),
    ERROR_CHAT_400039(400039, "异常", "超过组织管理员的上限"),
    ERROR_CHAT_400040(400040, "异常", "您不是组织管理员无权操作"),
    ERROR_CHAT_400041(400041, "异常", "账号异常"),
    ERROR_CHAT_400042(400042, "异常", "订单金额不能超过99999999.9999元"),
    ERROR_CHAT_400043(400043, "异常", "已有父辈，不能重复添加"),
    ERROR_CHAT_400044(400044, "异常", "该成员有子辈，请先删除其子辈"),
    ERROR_CHAT_400045(400045, "异常", "您今天已经签到了，不能重复签到"),
    ERROR_CHAT_400046(400046, "异常", "已经申请过了，不能重复申请"),
    ERROR_CHAT_400047(400047, "异常", "该群是系统创建的，不能修改群信息"),
    ERROR_CHAT_400048(400048, "异常", "该手机号已注册APP，请使用新的手机号码"),
    ERROR_CHAT_400049(400049, "异常", "修改手机号失败"),
    ERROR_CHAT_400050(400050, "异常", "该群是系统创建的，无法退群"),
    ERROR_CHAT_400051(400051, "异常", "该群是系统创建的，无法解散"),
    ERROR_CHAT_400052(400052, "异常", "该群是系统创建的，不能添加群成员"),
    ERROR_CHAT_400053(400053, "异常", "该群是系统创建的，不能删除群成员"),
    ERROR_CHAT_400054(400054, "异常", "请先去设置家乡地址"),
    ERROR_CHAT_400055(400055, "异常", "请升级至最新版本后操作"),
	ERROR_CHAT_400056(400056, "异常", "图案密码输入错误"),
    ERROR_CHAT_400057(400057, "异常", "banner位最大只能9个"),
    ERROR_CHAT_400058(400058, "异常", "已有简历或求职意向，不能重复"),
    ERROR_CHAT_400059(400059, "异常", "无法投递自己发布的职位"),
    ERROR_CHAT_400060(400060, "异常", "你已投递过此岗位"),
    ERROR_CHAT_400061(400061, "异常", "无法删除个人简历"),
    ERROR_CHAT_400062(400062, "异常", "简历不存在"),
    ERROR_CHAT_400063(400063, "异常", "您填写推荐人手机号或推荐码不存在，请修改"),
    ERROR_CHAT_400064(400064, "异常", "手机号不能超过11位"),
    ERROR_CHAT_400065(400065, "异常", "不能修改他人发布的简历"),
    ERROR_CHAT_400066(400066, "异常", "不能删除他人发布的招工"),
    ERROR_CHAT_400067(400067, "异常", "不能删除他人发布的简历"),
    ERROR_CHAT_400068(400068, "异常", "请勿重复提交"),
    ERROR_CHAT_400069(400069, "异常", "用户还没有设置家乡地址"),
    //ERROR_CHAT_400070(400070, "异常", "此微博已删除，不能重复操作"),
	ERROR_CHAT_400070(400070,"异常","收款账户信息已存在"),
    ERROR_CHAT_400071(400071,"异常","该岗位已经被删除"),
    ERROR_CHAT_400072(400072,"异常","该简历已经被删除"),
    ERROR_CHAT_400073(400072,"异常","投递记录已经被删除"),

	E_C_OPERATOR_INVALID(100002, "无相应操作权限", "客户端异常"), 
	E_C_INPUT_INVALID(100003, "无效的输入数据", "客户端异常"),

	// 客户端数据校验错误
	E_C_VALDATE_PROPERTY(104000, "无效的输入数据", "属性值已经被使用"), 
	E_C_VALDATE_CNUMBER(104001, "无效的输入数据", "身份证已经被使用"), 
	E_C_VALDATE_MOBILEPHONE(104002, "无效的输入数据", "手机号已经被使用"),
	E_C_VALDATE_CARDNUMBER(104003,"无效的输入数据", "银行卡号已被使用"), 
	E_C_VALDATE_BANKRECEIPT(104004, "无效的输入数据", "存单号已被使用"), 
	E_C_VALDATE_BANKTYPE(104005, "无效的输入数据", "银行类型代码已被使用"), 
	E_C_VALDATE_BANK_SUBBRANCH(104006, "无效的输入数据","银行编号已被使用"), 
	E_C_VALDATE_OBJECT_VO(104007, "无效的请求对象", "请求对象未初始化"),
	E_C_VALDATE_NODE_PARTY_ID(104008, "无效的请求对象", "网点ID不存在"),
	E_C_VALDATE_CREDENTIAL_NUMBER(104009, "无效的输入数据", "身份证号格式不正确"),
	E_C_VALDATE_NODEMANAGER_MOBILEPHONE(104010, "无效的输入数据", "该手机号属于站长手机号, 不能使用"), 
	E_C_VALDATE_MOBILEPHONE_ERROR(104011, "无效的输入数据", "短信验证码验证输入有误"), 
	E_C_VALDATE_MOBILEPHONE_COUNT(104012, "无效的输入数据", "短信验证码验证超限"), 
	E_C_VALDATE_WECHAT(104013, "无效的输入数据", "微信号已被使用"), 
	E_C_VALDATE_POS_LOGIN_SMSERROR_COUNT(104014, "无效的输入数据", "短信验证码错误次数超过3次，账户冻结1个小时"), 
	E_C_VALDATE_POS_LOGIN_PWDERROR(104015, "无效的输入数据", "手机号或密码输入错误"),
	E_C_VALDATE_MANAGER_MOBILE_NOTEXIST(104016, "无效的输入数据", "手机号不存在"),
	E_C_VALDATE_SMS_CODE_EXIST(104017, "无效的输入数据", "短信验证码不存在"),
	E_C_VALDATE_CREDENTIAL_FARMAT(104018, "无效的输入数据", "身份证格式不正确"),
	E_C_VALDATE_PROPERTY_VAL(104999,"无效的输入数据", ""),

	E_C_VALDATE_NODE_STATUS(114000, "无效的数据", "网点状态异常"), E_C_VALDATE_NODE_BUSINESS_STATUS(114001, "无效的数据", "业务状态异常"),

	E_C_VALDATE_NODEMANAGER_BASIC(105000, "网点经理基本信息缺失", "网点经理基本信息缺失，请补充"), E_C_VALDATE_NODEMANAGER_ID(105001, "网点经理身份信息缺失", "网点经理身份信息缺失，请补充"), E_C_VALDATE_NODEMANAGER_CONTACT(105002, "网点经理联系信息缺失",
			"网点经理联系信息缺失，请补充手机号"), E_C_VALDATE_NODEMANAGER_ISCOMMIT(105003, "网点经理通讯录信息修改不成功,请重试!", "网点经理通讯录信息修改不成功,请重试!"),
    

	/**
	 * 服务端错误
	 */
	E_S_INTEGRATION_ERROR(500001, "服务集成错误", "服务端异常"), 
	E_S_PERSISTENT_ERROR(500002, "数据持久化错误", "服务端异常"), 
	E_S_DATA_ERROR(500003, "数据错误", "服务端异常"), 
	E_S_INVALID_SESSION(500004, "无效会话", "服务端异常"), 
	E_S_INVALID_SESSION_EMPLOYEE(500005, "会话中员工数据异常", "服务端异常"), 
	E_S_DATA_ERROR_NOT_ONESELF(500006, "数据错误", "非创建者操作"), 
	E_S_DATA_PARAM_ERROR(500007, "前端传递参数错误", "前端传递参数错误"),
	E_S_WEIXIN_API_GETUSER(500008, "调用微信api查询员工信息","返回结果code码不为0"),
	E_S_NODE_OPERATIONSTATUS(500009, "需先加盟", "站点状态不是已加盟"), 
	E_S_NODEMANAGER_FOLLOWQW(500010, "需先关注企业号", "站长未关注企业号"),
	E_S_DISTRICT_CODE_INVALID(500011,"五级行政位置信息有缺失，请联系客户经理，在乡邻站点管理系统完善。", "行政位置信息不全"), 
	E_S_NODEWORK_FINISHED(500012, "任务已完成", "任务已完成"),
	E_S_EMPLOYEEWORK(500013, "任务不能保存", "任务不能保存"),
	E_S_NODEWORK_FINISHED_ERROR(500015,"任务因未满足条件不能完成", "任务因未满足条件不能完成"),
	E_S_BANK_UNOPEN(500014, "需先签约银行业务", "尚未开通银行业务，暂不可查看。"), 
	E_S_POS_UNBINDNODE(500016, "该设备尚未与站点建立绑定关系", "该设备尚未与站点建立绑定关系，请联系技术人员。"),
	

	/**
	 * 电商
	 */
	FIRST_ORDER_PAY(610000, "完成首笔订单支付", "完成首笔订单支付"), 
	OTHER_PAY(610001, "站长代付", "站长代付"), 
	WILL_24TIMEOUT(610002, "24小时尚未支付", "24小时尚未支付"), 
	PAYED(610003, "已支付", "已支付"), 
	SENDED(610004, "已发货", "已发货"), 
	CONFIRMED_RECEIVED(610005, "已确认收货", "已确认收货"),
	FIRST_ORDER_7(610007, "完成首单后，若7个自然日内无新订单", "完成首单后，若7个自然日内无新订单"), 
	EXPRESS_MESSAGE(610010, "所在站点，电商订单状态变更为“已支付，24小时未发货，则发送应用消息","所在站点，电商订单状态变更为“已支付，24小时未发货，则发送应用消息"), 
	ACCEPTANCE_MESSAGE(610011, "物流信息，签收后，发送消息告知。", "物流信息，签收后，发送消息告知。"), 
	REFUND_MESSAGE(610012, "在EC后台操作退款通知", "在EC后台操作退款通知"),
	EXPRESS_SEND(610013,"发货进展", "当系统查询到物流单号对应产生第一条物流信息数据时，发送物流已发货消息"), 
	VERIFY_SIGN_FAIL(600001, "验证签名失败", "验证签名失败"), 
	NOTIFY_SUCCESS(0, "通知成功", "通知成功"),

	/* 贷款 */
	LOAN_BUSINESS_NOT_OPEN(7000, "站点未开通贷款业务", "站点未开通贷款业务"),
	NODE_NOT_EXIST(7001, "站点不存在", "站点不存在"),

	/* CIF密码校验 */
	PermissionDeny(1001, "拒绝访问", "拒绝访问。"),
	PWD_EMPTY(1002, "拒绝访问", "密码不能为空"),
	

	/* xlStation短信校验 */
	NotFoundSms(5025, "没有发现查找的短信");
	public int code;

	public String msg;

	public String tip;

	private FacadeEnums(int code, String msg) {

		this.code = code;
		this.msg = msg;
	}

	private FacadeEnums(int code, String msg, String tip) {

		this.code = code;
		this.msg = msg;
		this.tip = tip;
	}

	public int getCode() {

		return code;
	}

	public void setCode(int code) {

		this.code = code;
	}

	public String getMsg() {

		return msg;
	}

	public void setMsg(String msg) {

		this.msg = msg;
	}

	public String getTip() {

		return tip;
	}

	public void setTip(String tip) {

		this.tip = tip;
	}

}
