package com.xianglin.appserv.biz.service.implement;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.daointerface.AppPushDAO;
import com.xianglin.appserv.common.dal.dataobject.AppPush;
import com.xianglin.appserv.common.service.facade.LoanService;
import com.xianglin.appserv.common.service.facade.model.AppSessionConstants;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.cif.common.service.facade.CustomersInfoService;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.cif.common.service.facade.enums.AccountRoleTypeEnum;
import com.xianglin.cif.common.service.facade.model.CustomersDTO;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import com.xianglin.loanbiz.common.service.facade.ApiService;
import com.xianglin.loanbiz.common.service.facade.dto.RequestDTO;
import com.xianglin.loanbiz.common.service.facade.dto.ResponseDTO;
import com.xianglin.xlappfile.common.service.facade.FileDealService;
import com.xianglin.xlappfile.common.service.facade.base.CommonResp;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 现金贷业务接口
 * Created by wanglei on 2017/9/26.
 */
@Service("LoanCommonService")
@ServiceInterface
public class LoanServiceImpl implements LoanService {

    private static final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);

    @Autowired
    private FileDealService fileDealService;

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private ApiService apiService;

    @Autowired
    private CustomersInfoService customersInfoService;

    /**
     * 乡邻贷通用请求接口
     *
     * @param serviceNum 接口编号
     * @param param      请求参数，json格式
     * @return
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.LoanService.currencyApi", description = "乡邻贷通用请求接口")
    public Response<String> currencyApi(String serviceNum, String param) {
        Response<String> response = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            String deviceId = sessionHelper.getSessionProp(AppSessionConstants.DEVICE_ID, String.class);
            RequestDTO dto = new RequestDTO();
            dto.setServiceCode(serviceNum);
            dto.setDataChannel("H5");
            JSONArray array = new JSONArray();
            JSONObject content = JSON.parseObject(param);
            content.put("partyId", partyId);
            content.put("clientVersion",sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION,String.class));
            content.put("platform",sessionHelper.getSessionProp(AppSessionConstants.SYSTEM_TYPE,String.class));
            content.put("custType",getUserRole());
            dto.setClientVersion(sessionHelper.getSessionProp(SessionConstants.CLIENT_VERSION,String.class));
            dto.setPlatform(sessionHelper.getSessionProp(AppSessionConstants.SYSTEM_TYPE,String.class));
            array.add(content);
            dto.setServiceParm(array.toJSONString());
            logger.info("currencyApi para:{}", dto.toString());
            ResponseDTO<Object> loanResp = apiService.invokeService(dto);
            logger.info("currencyApi result:{}", StringUtils.substring(loanResp.toString(),0,200));
            if (StringUtils.equals(loanResp.getCode(), com.xianglin.loanbiz.common.service.facade.enums.ResponseEnum.SUCCESS.getCode())) {
                response.setResult(JSON.toJSONString(loanResp.getResult()));
            } else {
                response.setCode(Integer.valueOf(loanResp.getCode()));
                response.setMemo(loanResp.getMsg());
                response.setTips(loanResp.getTip());
                if (loanResp.getResult() != null) {
                    response.setResult(JSON.toJSONString(loanResp.getResult()));
                }
            }
        } catch (Exception e) {
            logger.error("currencyApi ", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    public String getUserRole() {
        Set<String> businessOpenInfos = sessionHelper.getSessionProp(SessionConstants.BUSINESS_OPEN_INFO, Set.class);
   //DE_MANAGER"站长管理员"   APP_USER "普通用户"
        if (CollectionUtils.isNotEmpty(businessOpenInfos)) {
            for (String role : businessOpenInfos) {
                if (AccountRoleTypeEnum.NODE_MANAGER.name().equals(role)) {
                    return AccountRoleTypeEnum.NODE_MANAGER.name();
                }
            }
        }
        return AccountRoleTypeEnum.APP_USER.name();
    }
    /**
     * 文件上传
     *
     * @param fileType   文件类型
     * @param imgCOntent 文件 base64格式
     * @return 文件md5
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.LoanService.fileUpload", description = "文件上传")
    public Response<String> fileUpload(String fileType, String imgCOntent) {
        Response<String> response = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            CommonResp<String> resp = fileDealService.uploadPrivateFile(partyId, imgCOntent);
            if (resp.isSucces()) {
                response.setResult(resp.getBody());
            }
        } catch (Exception e) {
            logger.error("fileUpload ", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    /**
     * 文件下载
     *
     * @param fileMd 文件md5名
     * @return 文件base64格式
     */
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.LoanService.fileDownload", description = "文件下载")
    public Response<String> fileDownload(String fileMd) {
        Response<String> response = ResponseUtils.successResponse();
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            CommonResp<String> resp = fileDealService.downloadPrivateFile(partyId, fileMd, null);
            if (resp.isSucces()) {
                response.setResult(resp.getBody());
            }
        } catch (Exception e) {
            logger.error("fileDownload ", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.LoanService.checkAuth", description = "校验兵同步身份证")
    public Response<Boolean> checkAuth(String trueName, String authNu) {
        Response<Boolean> response = ResponseUtils.successResponse(false);
        try {
            Long partyId = sessionHelper.getSessionProp(SessionConstants.PARTY_ID, Long.class);
            com.xianglin.cif.common.service.facade.model.Response<CustomersDTO> resp = customersInfoService.selectCustomsAlready2Auth(partyId);
            logger.info("queryAuthLevelByPartyId trueName:{},authNu:{}, result:{}",trueName,authNu,ToStringBuilder.reflectionToString(resp));
            if (resp.getResult() != null) {
                if (StringUtils.contains(resp.getResult().getAuthLevel(),"R")) {
                    String name = resp.getResult().getCustomerName();
                    String credentialsNumber = resp.getResult().getCredentialsNumber();
                    if (org.apache.commons.lang3.StringUtils.equals(trueName, name) && StringUtils.equals(authNu, credentialsNumber)) {
                        response.setResult(true);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("fileDownload ", e);
            response = ResponseUtils.toResponse(ResponseEnum.DATA_ERROR);
        }
        return response;
    }
}
