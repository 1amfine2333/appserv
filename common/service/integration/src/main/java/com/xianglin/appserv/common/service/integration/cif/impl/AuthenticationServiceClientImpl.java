package com.xianglin.appserv.common.service.integration.cif.impl;

import com.xianglin.appserv.common.service.integration.cif.AuthenticationServiceClient;
import com.xianglin.cif.common.service.facade.AuthService;
import com.xianglin.cif.common.service.facade.CustomersInfoService;
import com.xianglin.cif.common.service.facade.constant.BusinessConstants;
import com.xianglin.cif.common.service.facade.enums.SystemTypeEnum;
import com.xianglin.cif.common.service.facade.model.PrincipalDTO;
import com.xianglin.cif.common.service.facade.model.Response;
import com.xianglin.cif.common.service.facade.vo.PartyAttrRealnameauthVo;
import com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/12/15
 * Time: 15:30
 */
@Deprecated
public class AuthenticationServiceClientImpl implements AuthenticationServiceClient{

    @Autowired
    private CustomersInfoService customersInfoService;

//    @Override
//    public String queryAuthLevelByPartyId(Long partyId, String source) {
//
//        PrincipalDTO dto = new PrincipalDTO();
//        dto.setPartyId(partyId);
//        dto.setSourceApplication(SystemTypeEnum.XL_APP.name());
//
//        Response<PartyAttrRealnameauthVo> result  = authenticationService.queryAuthLevelByPartyId(dto);
//
//        if(FacadeEnums.OK.getCode() == result.getCode()){
//            PartyAttrRealnameauthVo vo = result.getResult();
//            if(null != vo){
//                return vo.getAuthType();
//            }
//        }
//        return BusinessConstants.PartyLevel.V0.code;
//    }

//    @Override
//    public String queryAuthLevelByCredentials(String credentials,String credentialsType, String source) {
//
//        PrincipalDTO dto = new PrincipalDTO();
//        dto.setCredentialsName(credentials);
//        dto.setSourceApplication(SystemTypeEnum.XL_APP.name());
//
//        Response<PartyAttrRealnameauthVo> result  = authenticationService.queryAuthLevelByPartyId(dto);
//
//        if(FacadeEnums.OK.getCode() == result.getCode()){
//            PartyAttrRealnameauthVo vo = result.getResult();
//            if(null != vo){
//                return vo.getAuthType();
//            }
//        }
//        return BusinessConstants.PartyLevel.V0.code;
//    }
}
