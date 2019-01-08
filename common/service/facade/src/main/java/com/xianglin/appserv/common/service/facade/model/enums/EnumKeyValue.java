package com.xianglin.appserv.common.service.facade.model.enums;

/**
 * @author Yungyu
 * @description Created by Yungyu on 2018/3/7 14:20.
 */
public class EnumKeyValue {

    public enum User {
        PUSH_SWITCH("推送开关"),

        USER_RECEIPT_ACCOUNT("用户招聘收款账户"),

        USER_RECRUITJOB_DRAFT("用户招聘客户端缓存"),

        USER_USED_ADDRESS("用户招聘使用过的地址"),

        ;

        public String desc;

        private User(String desc) {

            this.desc = desc;
        }
    }

    /**
     * 招工
     */
    public enum Recruit{

        JOB_PROP_EXTEND("招工详情"),
        RESUME_PROP_EXTEND("简历详情"),;
        public String desc;

        Recruit(String desc) {
            this.desc = desc;
        }
    }
}
