package com.xianglin.xloan.common.service.integration.impl;

import com.xianglin.xloan.common.service.integration.LoanServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/12/8
 * Time: 10:45
 */
public class LoanServiceClientImpl implements LoanServiceClient {

    private final static Logger logger = LoggerFactory.getLogger(LoanServiceClientImpl.class);

//    private LoanService loanService;

    @Override
    public Boolean getCustApplyList(String mobilePhone, String idNumber) {

//        GetCustApplyListByInfoRequest request = new GetCustApplyListByInfoRequest();
//        if (idNumber != null) {
//            request.setCredentialsNumber(idNumber);
//        } else {
//            request.setPhoneNumber(mobilePhone);
//        }
//
//        Header header = new Header();
//        header.setInterfaceVersion(121);
//        request.setHeader(header);
//
//        RSAUtil rsaUtil = new RSAUtil(RSAUtil.RSAFORPOS);
//        try {
//            rsaUtil.encryptObject(request);
//            ApplyListResponse resp = loanService.getCustApplyListByInfo(request);
//            logger.info("查询用户贷款业务数据 req:{},resp:{}", ToStringBuilder.reflectionToString(request),ToStringBuilder.reflectionToString(resp));
//            if (XLStationEnums.ResultSuccess.getCode() == resp.getBussinessCode()) {
//                List<PreApply> list = resp.getApplyList();
//                if (CollectionUtils.isNotEmpty(list)) {
//                    return true;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }finally {
//            rsaUtil.destroy();
//        }
        return false;
    }
}
