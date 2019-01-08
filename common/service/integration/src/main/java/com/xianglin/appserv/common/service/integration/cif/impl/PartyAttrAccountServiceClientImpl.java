/**
 * 
 */
package com.xianglin.appserv.common.service.integration.cif.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xianglin.appserv.common.service.integration.cif.PartyAttrAccountServiceClient;
import com.xianglin.appserv.common.util.DES;
import com.xianglin.appserv.common.util.HttpUtils;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.cif.common.service.facade.PartyAttrAccountService;
import com.xianglin.cif.common.service.facade.PartyAttrPasswordService;
import com.xianglin.cif.common.service.facade.model.Response;
import com.xianglin.cif.common.service.facade.req.PartyAttrPasswordReq;
import com.xianglin.cif.common.service.facade.resp.PartyAttrAccountResp;
import com.xianglin.cif.common.service.facade.resp.PartyAttrPasswordResp;
import com.xianglin.cif.common.service.facade.vo.PartyAttrPasswordVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @author zhangyong 2016年8月26日上午10:37:05
 */
@Service
public class PartyAttrAccountServiceClientImpl implements PartyAttrAccountServiceClient {

	private static final Logger logger = LoggerFactory.getLogger(PartyAttrAccountServiceClientImpl.class);

	@Autowired
	private PartyAttrAccountService partyAttrAccountService;

	@Autowired
	private PartyAttrPasswordService partyAttrPasswordService;

	private static final String ENCRYPT_KEY = "XL_SESSION_1207";
	/**
	 * @see com.xianglin.appserv.common.service.integration.cif.PartyAttrAccountServiceClient#getPartyAttrAccount(long)
	 */
	@Override
	public PartyAttrAccountResp getPartyAttrAccount(long partyId) {
		return partyAttrAccountService.getPartyAttrAccount(partyId);
	}

	@Override
	public PartyAttrPasswordResp selectTradePwd(Long partyId) {
		PartyAttrPasswordReq req  = new PartyAttrPasswordReq();
		PartyAttrPasswordVo passwordVo = new PartyAttrPasswordVo();
		passwordVo.setPartyId(partyId);
		req.setPartyAttrPasswordVo(passwordVo);
		return partyAttrPasswordService.selectTradePwd(req);
	}

	@Override
	public Response<Boolean> checkTradePwd(String pwd,String sessionId) {
		Response<Boolean> response = new Response<>();
		try{
			response.setResult(false);
			Map<String,String> paras = new HashMap<>();
			paras.put("XLSESSIONID",DES.des_encrypt(ENCRYPT_KEY,sessionId));
			paras.put("passwordHash",pwd);
			String resp = HttpUtils.executePost(SysConfigUtil.getStr("redpacket_checkpwd_url"),paras,1000);
			JSONObject json = JSON.parseObject(resp);
			if(StringUtils.equals(json.getString("code"),"200000")){
				response.setResult(true);
			}else{
				String content = json.getString("content");
				if(StringUtils.isNotEmpty(content)){
					PartyAttrPasswordVo vo = JSON.parseObject(content,PartyAttrPasswordVo.class);
					response.setTips("密码错误，还有"+vo.getExistsResetCount()+"次机会！");
					response.setCode(200013);
				}else{
					response.setTips("密码错误次数超过5次, 请半个小时后重试！");
					response.setCode(json.getInteger("code"));
				}
			}
		}catch (Exception e){
			logger.error("checkTradePwd error",e);
			response.setTips("服务器请求异常，请稍后再试");
			response.setCode(200014);
		}
		return response;
	}
//e222d9b2-6b41-4a99-8e4c-4da3216c5f41
	public static void main(String[] args) {
//		String resp = HttpUtils.executeGet("https://h5cau-dev.xianglin.cn/xl/checkTradePwd?passwordHash=123456&XLSESSIONID="+DES.des_encrypt("XL_SESSION_1207","e222d9b2-6b41-4a99-8e4c-4da3216c5f41"));

		try {
			Map<String,String> paras = new HashMap<>();
			paras.put("XLSESSIONID", DES.des_encrypt("XL_SESSION_1207","e3e27389-cc2b-462f-9eb9-30f4742f6964"));
			paras.put("passwordHash","123456");
//			paras.put("PasswordType", URLEncoder.encode("文本密码"));
			String resp = HttpUtils.executePost("https://h5cau-dev.xianglin.cn/xl/checkTradePwd",paras,1000);
			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
