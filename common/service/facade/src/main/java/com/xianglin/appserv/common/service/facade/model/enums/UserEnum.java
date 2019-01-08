package com.xianglin.appserv.common.service.facade.model.enums;

import java.util.Calendar;

/**
 * 用户相关枚举
 */
public class UserEnum {

    public enum pushType {

        OPEN("打开"),
        CLOSE("关闭"),
        DAY("仅在白天打开"),
        ;

        public String desc;

        private pushType(String desc) {

            this.desc = desc;
        }

        /** 校验是否发推送
         * @param type
         * @return
         */
        public boolean checkStatus(String type) {
            boolean result = true;
            switch (type) {
                case "OPEN":
                    break;
                case "CLOSE":
                    result = false;
                    break;
                case "DAY":
                    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    result = (hour >= 8 && hour < 22);
                    break;
            }
            return result;
        }
    }
}
