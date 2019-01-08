/**
 * 
 */
package com.xianglin.appserv;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.google.gson.JsonObject;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.service.facade.model.EarningDTO;
import com.xianglin.appserv.common.service.facade.model.ProfitDTO;
import com.xianglin.appserv.common.service.facade.model.ProfitDetailDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.util.PropertiesUtil;
import com.xianglin.gateway.common.service.spi.model.ServiceResponse;
import com.xianglin.xlappserver.common.service.facade.vo.JsonBody;

/**
 * 
 * 
 * @author zhangyong 2016年8月16日下午5:24:17
 */
public class MainTest {

	public static void main(String[] args) {
	/*	System.out.println(PropertiesUtil.getProperty("LIVE_H5_URL"));
		
		String month = "14";
		System.out.println(StringUtils.leftPad(month, 2, "0"));*/
	
		EarningDTO dto= new EarningDTO();
		Response<EarningDTO> resp = ResponseUtils.successResponse();
		
		ProfitDTO profitDto = new ProfitDTO();
		profitDto.setBusiType("busitype");
		ProfitDetailDTO detailDto = new ProfitDetailDTO();
		detailDto.setBankProfit(BigDecimal.ONE);
//		detailDto.setTotalProfit(BigDecimal.ONE);
		dto.setProfitDetailDto(detailDto);
		dto.setProfitDto(profitDto);
		resp.setResult(dto);
		System.out.println(JSON.toJSONString(resp));
		System.out.println(JSON.toJSON(resp).toString());
		System.out.println(JSON.toJSONString(JSON.toJSON(resp).toString()));
		ServiceResponse<?> re =new ServiceResponse<>(JSON.toJSONString(resp));
		SingleResponseBody sb = SingleResponseBody.valueOf(re);
		String json ="{\"code\":1000,\"result\":{\"profitDetailDto\":{\"bankProfit\":1,\"total\":1},\"profitDto\":{\"busiType\":\"busitype\"}},\"success\":true}";
		
	/*	Object o =re.getResult();
		
		String ss = "1212";
		System.out.println(JSON.toJSONString(ss));
		
		
		System.out.println(JSON.parse(ss)	);	
		System.out.println(sb.getResult());
		
		System.out.println(JSON.toJSONString(SingleResponseBody.valueOf(re)));
		JSONObject jsonObject =	(JSONObject) JSON.toJSON(resp);
		System.out.println(jsonObject);
		Response<ProfitDTO> re1 = JSON.toJavaObject(jsonObject, Response.class);

		System.out.println(re1.toString());*/
		//		String json = JSON.toJSONString(SingleResponseBody.valueOf(serviceResponse));
//		System.out.println(Respon);
//		String ss ="123";
//		JSONObject jsonObject =	(JSONObject) JSON.toJSON(ss);
//		System.out.println("!!"+jsonObject);
		
		
//		System.out.println("--"+JSON.toJSONString(jsonObject));
		String s ="1000036389";
//		System.out.println(Long.valueOf(s));
		
		tests1();
		
		Long id = null;
		Long ddd = 1212l;
//		System.out.println(null ==(id = ddd));
//		System.out.println(id);
	}
	
	public static void tests1(){
		
		//默认起始年月
				Integer startYear = 2016;
				Integer startMonth = 5;
				//截止年月
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, 2017);
				cal.set(Calendar.MONTH, 2);
				cal.add(Calendar.MONTH, -1);
				System.out.println(cal.getTime());
				Integer endYear = cal.get(Calendar.YEAR);
				Integer endMonth = cal.get(Calendar.MONTH)+1;
				
				while(startMonth.intValue() <= endMonth && startYear.intValue() <= endYear){
					//循环同步数据
					System.out.println("startYear:"+startYear);
					System.out.println("startMonth:"+startMonth);
					startMonth ++;
					if(startMonth == 13){
						startYear ++;
						startMonth = 1;
					}
				}
				
/*		Date d  = new Date(1472266982429L);
		System.out.println("=="+d);
		long now = System.currentTimeMillis();
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(now);
		date.clear(Calendar.SECOND);
		date.clear(Calendar.MILLISECOND);
		System.out.println(date.getTime());
		System.out.println(date.getTimeInMillis());
		Calendar date1 = Calendar.getInstance();
		date1.setTimeInMillis(now);
		date1.add(Calendar.MINUTE, 1);
		date1.clear(Calendar.SECOND);
		date1.clear(Calendar.MILLISECOND);
		System.out.println(date1.getTime());
		System.out.println(date1.getTimeInMillis());*/
	}
}
