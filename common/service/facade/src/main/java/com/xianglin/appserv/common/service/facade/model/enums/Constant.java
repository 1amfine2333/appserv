/**
 *
 */
package com.xianglin.appserv.common.service.facade.model.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhangyong 2016年8月16日下午3:56:27
 */
public class Constant {

    /**
     * 统计服务游客默认PARTY_ID
     */
    public static final String COUNT_SERVICE_YOUKE_PARTYID = "YK000000";

    /**
     * 消息类型
     *
     * @author zhangyong 2016年8月16日下午4:01:02
     */
    public enum MsgType {
        NOTIFY("通知"),
        ALARM("警告"),
        ARTICLE("公告"),
        OFFLINE("下线提示"),
        DAILY_TIP("每日天气|美言推送"),
        DIALOG("弹出框提醒类"),
        REDPACK("红包"),
        NEWS("头条新闻"),
        REWARD("打赏"),
        PRESENTED_FLOWER("献花"),
        FANS("新增粉丝提醒"),
        MERCHANT_ORDER("乡邻账房订单"),
        FANS_RED_TIP("新增粉丝红点"),
        MERCHANT_PAY("乡邻账房付款正扫"),
        BROADCAST("广播"),
        ESHOP("乡邻购"),
        PRAISE_SUBJECT("赞微博"),
        COMMENT_SUBJECT("评论微博"),
        FORWARD_SUBJECT("转发微博"),
        FOLLOW("新增关注"),
        PRAISE_COMMENT("赞评论"),
        REPLY_SUBJECT("回复评论"),
        SIGN("签到提醒"),
        INTEREST_TIP("秒息宝提醒"),
        LUCKDRAW_TIP("抽奖提醒"),
        USER_GENERLOGY_TIP("家谱提醒"),
        CASHBONUS_TIP("现金红包"),
        PRAISE_SHORTVIDEO("赞短视频"),
        COMMENT_SHORTVIDEO("评评论"),
        FORWARD_SHORTVIDEO("转发短视频"),
        FEEDBACK("反馈回复")
        ;

        public String desc;

        private MsgType(String desc) {

            this.desc = desc;
        }

        /**
         * 生成名称集合
         *
         * @param except 排除的类型
         * @return
         */
        public static List<String> toList(String... except) {

            List<String> list = new ArrayList<>(MsgType.values().length);
            for (MsgType type : MsgType.values()) {
                list.add(type.name());
            }
            list.removeAll(Arrays.asList(except));
            return list;
        }
    }

    public enum MsgNewsTag {
        TTXW("头条新闻，来源中国农业网或凤凰网"),

        RMZX("热门资讯"),
        JKZX("健康资讯"),
        SSCL("时尚潮流"),
        YLXW("娱乐新闻（社会热点）"),
        XLXW("乡邻新闻"),
        VIDEO("视频"),;

        public String desc;

        private MsgNewsTag(String desc) {

            this.desc = desc;
        }
    }

    /**
     * 消息操作类型
     */
    public enum MsgOperateType {
        SHARE("分享"),
        READ("阅读（播放）"),
        TREAD("赞"),
        PRAISE("踩"),;

        public String desc;

        private MsgOperateType(String desc) {

            this.desc = desc;
        }
    }

    public enum MsgPushType {
        JPUSH("激光推送"),
        APNS("apns推送"),
        MIPUSH("小米推送"),
        HWPUSH("华为推送"),;

        public String desc;

        private MsgPushType(String desc) {

            this.desc = desc;
        }
    }

    public enum MsgPushStatus {
        CLICK("消息被点击"),
        RECEIVE("消息已收到"),;

        public String desc;

        private MsgPushStatus(String desc) {

            this.desc = desc;
        }
    }

    /**
     * 消息状态
     *
     * @author zhangyong 2016年8月16日下午4:03:22
     */
    public enum MsgStatus {
        READED("9", "已读"),
        UNREAD("1", "未读"),

        USED("2", "已使用"),
        INIT("0", "初始状态"),;

        public String code;

        public String desc;

        private MsgStatus(String code, String desc) {

            this.code = code;
            this.desc = desc;
        }
    }

    public enum BusinessType {
        BANK("银行业务"),
        ESHOP("电商业务"),
        LOAN("贷款业务"),
        LIVE("生活缴费业务"),
        SOLAR("光伏业务"),
        MOBILERECHARGE("充值业务"),
        MERCHANT("乡邻账房"),
        MERCHANT_BUSI("账房小二"),
        INTEREST("秒喜宝"),
        XL_LOAN("乡邻助贷"),
        NODEMANAGER("小站站长"),
        VISITOR("未登陆用户"),
        USER("普通用户"),
        LIFE("相邻生活"),
        LOGIN("登陆"),;

        String desc;

        private BusinessType(String desc) {

            this.desc = desc;
        }
    }

    public enum BusinessActive {
        HTML("跳转html页面"),
        ACTIVE_LOGIN("跳转登陆"),
        ACTIVE_PROFIT("我的赚钱"),;

        String desc;

        private BusinessActive(String desc) {

            this.desc = desc;
        }
    }

    public enum BusinessStatus {
        NONE("未签订"), SIGNED("已签订试营业"), OPENING("已签订营业中"), INVOKED("已撤销");;

        String desc;

        private BusinessStatus(String desc) {

            this.desc = desc;
        }
    }

    public enum YESNO {
        YES("Y"), NO("N");

        public String code;

        private YESNO(String code) {

            this.code = code;
        }
    }

    public enum UserType {
        nodeManager("站长管理员"),
        user("普通用户"),
        visitor("游客"),
        all("全部");

        String desc;

        private UserType(String desc) {

            this.desc = desc;
        }
    }


    public enum UserStatus {
        NORMAL("正常状态");

        String desc;

        private UserStatus(String desc) {

            this.desc = desc;
        }
    }


    public enum BusiVisitKey {
        TOTAL_PROFIT_URL("TOTAL_PROFIT_URL", "总收益"),
        LIFE_PAY_PROFIT_URL("LIFE_PAY_PROFIT_URL", "生活缴费收益"),
        BANK_PROFIT_TOP_URL("BANK_PROFIT_TOP_URL", "银行收益"),
        BANK_PERFORMANCE_URL("BANK_PERFORMANCE_URL", "银行业绩"),
        BANK_BALANCE_URL("BANK_BALANCE_URL", "银行余额"),
        BANK_INCRBALANCE_URL("BANK_INCRBALANCE_URL", "余额增量"),
        BANKCARD_NUM_URL("BANKCARD_NUM_URL", "银行卡数量"),
        BANK_MANAGER_URL("BANK_MANAGER_URL", "银行管理界面"),
        LOAN_MANAGER_URL("LOAN_MANAGER_URL", "贷款"),
        LIFE_MANAGER_URL("LIFE_MANAGER_URL", "缴费"),
        MOBLE_MANAGER_URL("MOBLE_MANAGER_URL", "手机充值"),
        ESHOP_MANAGER_URL("ESHOP_MANAGER_URL", "电商收益界面"),

        OPERATION_MANAGER_URL("", "经营管理"),
        INCOME_PROFIT_URL("", "银行业绩收益"),
        MY_RANK_URL("", "我的排名"),
        REWARD_PARAM_URL("REWARD_PARAM_URL", "打赏支付中间页"),
        RED_PACKET_URL("RED_PACKET_URL", "红包活动url"),
        MESSAGE_DETAIL_URL("MESSAGE_DETAIL_URL", "消息详情"),
        MESSAGE_DAILY_SEND_URL("MESSAGE_DAILY_SEND_URL", "每日一言详情"),
        APP_FILESERVER_URL("APP_FILESERVER_URL", "appfile服务器"),
        SOLAR_H5_URL("SOLAR_H5_URL", "光伏访问url"),
        BANER_IMG_URL("", ""),
        BANK_CLOSE_H5_URL("BANK_CLOSE_H5_URL", "银行未开通"),
        BANK_H5_URL("BANK_H5_URL", "银行业务"),
        ESHOP_CLOSE_H5_URL("ESHOP_CLOSE_H5_URL", "电商未开通"),
        ESHOP_H5_URL("ESHOP_H5_URL", ""),
        H5CAU_URL("H5CAU_URL", ""),
        H5WECHAT_URL("H5WECHAT_URL", ""),
        LIVE_H5_URL("LIVE_H5_URL", "生活缴费页面"),
        LOAN_H5_URL("LOAN_H5_URL", "贷款页面"),
        MESSAGE_NEWS_DETAIL("MESSAGE_NEWS_DETAIL", "每日新闻页面"),
        MOBILERECHARGE_H5_URL("MOBILERECHARGE_H5_URL", "手机充值页面"),

        androidDesc("androidDesc", "安卓更新描述"),
        androidURL("androidURL", "安卓下载url"),
        androidUpdate("androidUpdate", "安卓是否强制更新"),
        androidVersion("androidVersion", "安卓版本"),
        androidVersionCode("androidVersionCode", "安卓版本版本的另一个版本，整型，方便客户端比较"),
        apns_certificate("apns.certificate", "APNS推送证书路径"),
        apns_password("apns.password", "APNS推送密码"),
        cash_coupon_packet_scale("cash.coupon.packet.scale", "优惠券比例"),
        cash_decrease_step("cash.decrease.step", "现金券减少步长"),

        activity_timeout("activity.timeout", "活动时长"),
        activity_fresh_red_packet_switch("activity.fresh.red.packet.switch", "新人红包开关，true为开，其他的为关闭"),
        activity_running_desc("activity.running.desc", "活动进行中描述"),
        activity_today_desc("activity.today.desc", "活动开始前描述"),
        activity_tomorror_desc("activity.tomorror.desc", "活动结束后描述"),
        cash_red_packet_scale("cash.red.packet.scale", "现金红包比例"),
        coupon_decrease_step("coupon.decrease.step", "优惠券减少步长"),
        fresh_redpacket_amount("fresh.redpacket.amount", "新人红包金额"),
        iosDesc("iosDesc", "IOS更新描述"),
        iosURL("iosURL", "IOS下载url"),
        iosUpdate("iosUpdate", "IOS是否强制更新"),
        iosVersion("iosVersion", "IOS版本"),
        iosVersionCode("iosVersionCode", "安卓版本版本的另一个版本，整型，方便客户端比较"),
        jpush_app_key("jpush.app.key", "极光推送key"),
        jpush_master_secret("jpush.master.secret", "极光推送密钥"),
        mi_app_id("mi.app.id", "小米推送appid"),
        mi_app_key("mi.app.key", "小米推送key"),
        mi_app_package("mi.app.package", "小米推送对应包名"),
        mi_master_secret("mi.master.secret", "小米推送密钥"),
        offlineMessage("offlineMessage", "下线信息"),
        red_activity_switch("red.activity.switch", "红包活动开关"),
        red_packet_total_num("red.packet.total.num", "红包初始化数量"),
        task_decrease_step("task.decrease.step", "JOB运行减少频率5秒"),
        app_ios_support_nodecode("app.ios.support.nodecode", "IOS是否支持网点编号登陆"),
        app_ios_xl_admin_account("app.ios.xl.admin.account", "IOS测试手机号"),
        app_ios_xl_admin_vertifyCode("app.ios.xl.admin.vertifyCode", "IOS测试验证码"),

        live_pay_party_list("live.pay.party.list", "可操作水电煤缴费白名单"),;

        public String desc;

        public String code;

        BusiVisitKey(String code, String desc) {

            this.desc = desc;
            this.code = code;
        }
    }

    public enum DeviceType {
        ANDROID, IOS;
    }

    public enum RedPacketType {
        CASH("现金券"),
        COUPON("优惠券"),
        FRESH_RED_PACKET("新人红包"),
        LUCKWHEEL_RED_PACKET("大转盘红包"),
        CHAT_USER_REDPACKET("聊天个人红包"),;

        String desc;

        /**
         *
         */
        RedPacketType(String desc) {

            this.desc = desc;
        }

        public String getDesc() {

            return desc;
        }

        public void setDesc(String desc) {

            this.desc = desc;
        }

    }

    public enum RedPacketStatus {
        EFFECTIVE("生效"),
        EXPIRED("失效"),
        UNUSED("未使用"),
        USED("已使用"),;

        String desc;

        /**
         *
         */
        RedPacketStatus(String desc) {

            this.desc = desc;
        }
    }

    public enum TransactionStatus {
        SUCCESS("成功"),
        FAIL("失败"),
        PROCESS("处理中"),;

        String desc;

        /**
         *
         */
        TransactionStatus(String desc) {

            this.desc = desc;
        }
    }

    public enum ActivityStatus {
        END("已结束"),
        RUNNING("运行中"),
        READY("准备中"),;

        String desc;

        ActivityStatus(String desc) {

            this.desc = desc;
        }
    }

    public enum ActivityResultDesc {
        TOMORROR("明日9：00开始"),
        RUNNING("正在抢购中"),
        TODAY("今日9：00开始");

        String desc;

        ActivityResultDesc(String desc) {

            this.desc = desc;
        }

        public String getDesc() {

            return desc;
        }

        public void setDesc(String desc) {

            this.desc = desc;
        }
    }

    public enum ActivityInviteSource {
        WX("微信分享"),
        PYQ("朋友圈"),
        QR("二维码"),;

        String desc;

        ActivityInviteSource(String desc) {

            this.desc = desc;
        }
    }

    public enum ActivityInviteStatus {
        I("初始状态"),
        D("处理中"),
        F("失败"),
        S("成功"),
        U("已被邀请"),;

        String desc;

        ActivityInviteStatus(String desc) {

            this.desc = desc;
        }
    }

    /**
     * 揽储活动进度
     */
    public enum ActivityDepositsStatus {
        F("未达到目标"),
        N("已达到目标但未领取奖励"),
        Y("已领取奖励"),;

        String desc;

        ActivityDepositsStatus(String desc) {

            this.desc = desc;
        }
    }

    /**
     * 控制app客户端跳转
     *
     * @author wanglei 2016年12月16日下午8:36:19
     */
    public enum NativeActivity {
        HTML("http", "进入网页"),
        RED_PACKET("native:RED_PACKET", "进入抢红包"),
        ACTIVITY_INVITE("native:ACTIVITY_INVITE", "进入我的邀请"),
        ACTIVE_PROFIT("native:ACTIVE_PROFIT", "进入赚钱"),
        ACTIVE_BANK("native:ACTIVE_BANK", "进入银行页面"),
        ACTIVE_LOGIN("native:ACTIVE_LOGIN", "登陆"),
        CALXULATOR("native:CALXULATOR", "赚钱计算器"),
        MONTHLY_REPORT("native:MONTHLY_REPORT", "站长月报"),
        FILOFAX("native:FILOFAX", "乡邻账本"),
        ACTIVE_MERCHANT("native:MERCHANT", "乡邻账房"),
        ACTIVE_MERC_BUSI("native:MERC_BUSI", "乡邻账房小二"),
        ACTIVE_XL_LOAN("native:XL_LOAN", "乡邻贷"),
        ACTIVE_INTEREST("native:INTEREST", "秒息宝"),
        ACTIVE_BROADCAST("native:BROADCAST", "广播"),
        ACTIVE_GOVERN("native:GOVERNMENT", "政务公开"),
        ACTIVE_FINANCE("native:FINANCE", "财务公开"),
        ACTIVE_CONTACTS("native:CONTACTS", "村通讯录"),
        ACTIVE_GAME("native:GAME", "玩游戏"),
        ACTIVE_RECRUIT("native:RECRUITMENT", "招工"),
        ACTIVE_FINAN_ZBB("native:FINAN_ZBB", "众邦宝"),
        ACTIVE_HELPER("native:LITTLE_HELPER", "赚钱小帮手");

        public String code;

        String desc;

        NativeActivity(String code, String desc) {

            this.code = code;
            this.desc = desc;
        }
    }

    public enum Delete_Y_N {

        Y("Y", "删除"),
        N("N", "未删除"),;

        public String code;

        public String desc;

        Delete_Y_N(String code, String desc) {

            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 必须取code
     */
    public enum Delete_1_0 {

        Y("1", "删除"),
        N("0", "未删除"),;

        public String code;

        public String desc;

        Delete_1_0(String code, String desc) {

            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 关注状态
     */
    public enum RelationStatus {

        FOLLOW("FOLLOW", "已关注"),
        FANS("FANS", "粉丝"),//查询参数，不会保存
        BOTH("BOTH", "互粉"),
        UNFOLLOW("UNFOLLOW", "已取消关注"),;

        public String code;

        public String desc;

        RelationStatus(String code, String desc) {

            this.code = code;
            this.desc = desc;
        }
    }

    public enum ThemeType {
        DEFAULT("默认主题"),
        NEWYEAR("新年主题");

        public String desc;

        ThemeType(String desc) {

            this.desc = desc;
        }
    }

    public enum ArticleType {
        SUBJECT("动态"),
        COMMENT("评论"),
        SUBCOMMENT("子评论"),
        BROADCAST("广播"),
        FINANCE("财务"),
        GOVERNMENT("政务"),
        RECRUITMENT("招工"),
        LEARNING_PPT("学习ppt"),
        SHORT_VIDEO("短视频");

        public String desc;

        ArticleType(String desc) {

            this.desc = desc;
        }
    }

    /**
     * 内容格式
     */
    public enum ArticleContent {
        TOPIC("<span type='topic'>","</span>");

        public String start;

        public String end;

        ArticleContent(String start, String end) {
            this.start = start;
            this.end = end;
        }
    }

    public enum ArticleTipType {
        COMMENT("评论提醒"),
        PRAISE("点赞提醒"),
        REPORT("举报"),
        COLLET("收藏"),
        SHARE("分享"),
        FOLLOW("关注"),
        ACT("活动提醒"),
        SHORT_VIDEO("短视频"),
        ;

        public String desc;

        ArticleTipType(String desc) {

            this.desc = desc;
        }

        /**
         * 生成名称集合
         *
         * @param except 排除的类型
         * @return
         */
        public static List<String> toList(String... except) {

            List<String> list = new ArrayList<>(ArticleTipType.values().length);
            for (ArticleTipType type : ArticleTipType.values()) {
                list.add(type.name());
            }
            list.removeAll(Arrays.asList(except));
            return list;
        }
    }

    /**
     * 红包雨活动奖励类型(活动奖励类型)
     */
    public enum ActivityShareRewardType {
        CASH("现金"),
        CALL_BILL("话费券"),
        VOUCHER("抵用券"),
        VIVO("vivo手机"),
        GOLDCOIN("金币"),;

        public String desc;

        ActivityShareRewardType(String desc) {

            this.desc = desc;
        }
    }

    /**
     * 红包雨活动奖励类型
     */
    public enum NodeAchieveType {
        B("余额"),
        C("银行卡数"),
        Y("年日均"),
        M("月日均"),;

        public String desc;

        NodeAchieveType(String desc) {

            this.desc = desc;
        }
    }

    /**
     * 活动类型
     * code为三位字符，顺序编排
     */
    public enum ActivityType {
        LUCKY("101", "幸运go"),
        NEW_GIFT("102", "新人有礼"),;

        public String code;

        public String desc;

        ActivityType(String code, String desc) {

            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 活动任务类型
     * 名称不能超过8个字符，否则数据库报错
     */
    public enum ActivityTaskType {
        SHARE("每日分享"),
        COMMENT("评论"),
        ANSWER("每日答题"),
        ALERT_GO("每日答题提示框"),
        NEWgIFT("新人有礼礼包"),
        POPUP("活动弹窗"),
        NEVER("永不弹窗"),
        INVITE("邀请好友"),
        REGISTER("新用户注册"),
        INVITE20("邀请满20额外奖励"),
        SIGN("签到"),
        ANNUAL_POPUP("年报弹窗"),
        READ_NEWS("阅读新闻"),
        COMMENT_NEWS("评论新闻"),
        PUBLISH_ARTICLE("发微博"),
        COMMENT_ARTICLE("评论微博"),
        PRAISE_ARTICLE("赞微博"),
        SHARE_NEWS("分享新闻"),
        HOT_ARTICLE("微博上热门"),
        SHORT_VODIE_REC("短视频被推荐"),
        SHORT_VODIE_HOT("短视频上热门"),
        DEL_SUBJECT("违规微博被删除"),
        DEL_SHORT_VIDEO("短视频被删除"),
        SHARE_INCOME("分享晒收入"),
        CLEAR_ACCOUNT("封号"),
        SHARE_AREPORT("分享年报"),
        COUNTDOWN_POPUP("倒计时弹窗"),
        INVITE351("邀请好友"),
        REGISTER351("新用户注册"),
        REALNAME_AUTH("实名认证"),
        F_REALNAME_AUTH("好友实名认证"),
        PERFECT_DATA("完善资料"),
        F_PERFECT_DATA("好友完善资料");

        public String desc;

        ActivityTaskType(String desc) {

            this.desc = desc;
        }
    }

    public enum TransStatus {
        S("成功"),
        F("失败"),
        I("初始状态"),;

        public String desc;

        TransStatus(String desc) {

            this.desc = desc;
        }
    }

    public enum TransType {
        ACTIVITY("活动"),
        REDPACKET("红包"),;

        public String desc;

        TransType(String desc) {

            this.desc = desc;
        }
    }

    public enum ConvenientToPersonType {
        MYAPPS("我的应用"),
        LIFE("乡邻生活"),
        PERSONQUERY("便民查询"),
        PERSONSERVICE("便民服务"),
        PERSONLIFE("便民生活"),
        FIRSTMODULES("首页四大模块"),;

        public String desc;

        ConvenientToPersonType(String desc) {

            this.desc = desc;
        }
    }

    public enum FilofaxCategory {
        PUBLIC("公共类别"),
        PRIVATE("个人类别"),
        SPECIAL("特殊类别"),;

        public String desc;

        FilofaxCategory(String desc) {

            this.desc = desc;
        }
    }

    public enum FilofaxMode {
        IN("收入"),
        OUT("支出"),;

        public String desc;

        FilofaxMode(String desc) {

            this.desc = desc;
        }
    }

    public enum GroupType {
        G("群聊"),
        O("组织"),
        V("村"),;

        public String desc;

        GroupType(String desc) {

            this.desc = desc;
        }
    }

    public enum CreateType {
        SYSTEM("系统创建"),
        PERSONAL("个人创建"),;

        public String desc;

        CreateType(String desc) {

            this.desc = desc;
        }
    }

    public enum MemberType {
        MEMBER("成员"),
        MANAGER("管理员"),;

        public String desc;

        MemberType(String desc) {

            this.desc = desc;
        }
    }

    public enum Log {
        REQ("请求"),
        RESP("应答"),;

        public String desc;

        Log(String desc) {

            this.desc = desc;
        }
    }

    public enum VillageInvestigation {
        A("有必要"),
        B("没必要"),
        C("不在意"),;

        public String desc;

        VillageInvestigation(String desc) {

            this.desc = desc;
        }
    }


    /**
     * 登陆状态
     */
    public enum LoginResult {
        NN("出错错误，登陆失败，返回登陆页面"),
        NP("登陆成功没有设置密码，进入设置密码"),
        NA("没有进行实名认证，进入实名认证"),
        ND("没有设置家乡地址"),
        SS("注册登陆成功，进入首页"),
        NL("自动登陆失败，进入未登陆模式");

        private String value;

        LoginResult(String value) {

            this.value = value;
        }

    }

    public enum ActivityCategory {
        ACTIVITY("活动"),
        GAME("游戏"),;

        public String desc;

        ActivityCategory(String desc) {

            this.desc = desc;
        }
    }

    public enum ChannelCode {
        CHANNEL_CODE("000001"),;

        public String value;

        ChannelCode(String value) {

            this.value = value;
        }
    }

    public enum BannerType{
        INDEX("首页"),
        DISCOVER("发现"),
        START("启动页"),
        OPER_UD("默认运营位上"),
        OPER_LD("默认运营位下"),
        OPER_U("运营位上"),
        OPER_L("运营位下"),
        XL_SQ("乡邻广场"),
        XL_SHOP("商超便利"),
        XL_LIFE("便民生活"),
        XL_TRAVE("票务出行"),
        XL_OPE_U("乡邻运营位上"),
        XL_OPE_L("乡邻运营位下"),
        RECRUIT("乡邻就业"),;
        public String desc;

        BannerType(String desc) {
            this.desc = desc;
        }

        /**
         * 生成名称集合
         *
         * @param type 添加的类型
         * @return
         */
        public static List<String> toList(String... type) {

            List<String> list = new ArrayList<>();
            /*for (BannerType type : BannerType.values()) {
                list.add(type.name());
            }*/
            list.addAll(Arrays.asList(type));
            return list;
        }
    }

    /**
     * 登录类型
     */
    public enum LoginType{
        SMS("短信验证码"),
        PWD("密码"),
        PATT_PWD("图案密码"),
        PATT_PWD_STATUS("图案密码状态"),
        ;
        public String desc;

        LoginType(String desc) {
            this.desc = desc;
        }
    }

    /**
     * 招聘动态类型
     */
    public enum RAType{
        RECRUIT("招聘"),
        ARTICLE("动态"),;
        public String desc;

        RAType(String desc) {
            this.desc = desc;
        }
    }

    /**
     * 招聘动态类型
     */
    public enum RecruitPayType{
        F("面议"),
        A("薪资区间"),;
        public String desc;

        RecruitPayType(String desc) {
            this.desc = desc;
        }
    }

    /**
     * 关注类型
     */
    public enum FollowType{
        FOLLOW("关注"),
        CITY("本市"),
        COUNTY("县"),
        TOWN("镇"),;
        public String desc;

        FollowType(String desc) {
            this.desc = desc;
        }
    }

    /**
     * 应用或微博
     */
    public enum BusinessOrArticle{
        ICON("应用"),
        ARTICLE("微博"),
        ALL("所有"),;
        public String desc;

        BusinessOrArticle(String desc) {
            this.desc = desc;
        }
    }
    
    public enum SearchType{
        SEARCH("搜索"), 
        SCREEN("筛选");
        public String desc;

        SearchType(String desc) {
            this.desc = desc;
        }
    }

    /**
     * 业务支持的类型
     */
    public enum BusiSupportUser{
        COMMON("普通"),
        VOICE("语音搜索");
        public String desc;

        BusiSupportUser(String desc) {
            this.desc = desc;
        }
    }
}
