package com.xianglin.xloan.common.service.integration;


/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/12/8
 * Time: 10:45
 */
public interface LoanServiceClient {

    /**
     * 是否有过借款记录
     * 根据手机号/身份证号查询
     * @param mobilePhone
     * @param idNumber
     * @return
     */
    Boolean getCustApplyList(String mobilePhone,String idNumber);


}
