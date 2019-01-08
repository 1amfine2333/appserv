package com.xianglin.appserv.common.service.facade.model.enums;

import javax.swing.*;
import java.util.Calendar;

/**
 * 招工枚举
 */
public class RecruitEnum {

    /**
     * 简历类型
     */
    public enum ResumeType{
        PSERSONAL("个人简历"),
        RECOMMEND("推荐简历"),
        INTENTION("求职意向"),
        ;
        public String desc;

        ResumeType(String desc) {
            this.desc = desc;
        }
    }

    /**
     * 招聘动态类型
     */
    public enum EmploymentType{
        H("已投递","成功投递简历","简历已投递成功，等待企业的好消息。"),
        F("未录用","简历被标记为未录用","未能被用人单位录用，快去申请其他岗位。"),
        U("已录用","简历被标记为录用"," 已被用人单位录用，恭喜获得心仪岗位。"),
        N("未联系","",""),
        A("已联系","",""),
        E("已联系录用","",""),
        NE("已联系不录用","",""),
        UC("佣金已发放","已发放佣金","通过用人单位审核，快去查收佣金奖励。"),
        UNC("推荐失败","未满足奖励发放规则","未满足奖励发放规则，快去推荐其他岗位。"),
        ;

        public String desc;

        public String tip;

        public String msg;

        EmploymentType(String desc, String tip, String msg) {
            this.desc = desc;
            this.tip = tip;
            this.msg = msg;
        }

        public static String getTip(String type){
            String tip = "";
            switch (type){
                case "H" :
                    tip = H.tip;
                    break;
                case "F" :
                    tip = F.tip;
                    break;
                case "U" :
                    tip = U.tip;
                    break;
                case "UC" :
                    tip = UC.tip;
                    break;
                case "UNC" :
                    tip = UNC.tip;
                    break;
            }
            return tip;
        }

        public static String getMsg(String type){
            String msg = "";
            switch (type){
                case "H" :
                    msg = H.msg;
                    break;
                case "F" :
                    msg = F.msg;
                    break;
                case "U" :
                    msg = U.msg;
                    break;
                case "UC" :
                    msg = UC.msg;
                    break;
                case "UNC" :
                    msg = UNC.msg;
                    break;
            }
            return msg;
        }
    }

    public enum JobResumeStatusType{
        RESUME("简历状态"),
        JOBRESUME("录用状态"),
        COMMISSION("有奖推荐状态"),;
        public String desc;

        JobResumeStatusType(String desc) {
            this.desc = desc;
        }
    }

    /**
     * 期望薪资类型
     */
    public enum ResumePayType{
        A("区间"),
        N("不限"),
        F("面议"),
        ;
        public String desc;

        ResumePayType(String desc) {
            this.desc = desc;
        }
    }
}
