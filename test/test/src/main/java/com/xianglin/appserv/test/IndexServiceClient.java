
package com.xianglin.appserv.test;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xianglin.appserv.common.service.facade.app.IndexService;
import com.xianglin.appserv.common.service.facade.model.Request;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.BusinessVo;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.MsgQuery;




public class IndexServiceClient {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath:consumer.xml" });
		context.start();
		
		IndexService service = (IndexService) context.getBean("indexService");
//		Response<List<BannerVo>> resp = service.indexBanners();
//		Response<List<BusinessVo>> resp = service.indexBusiness();
		Request<MsgQuery> req = new Request<MsgQuery>();
		MsgQuery query = new MsgQuery();
		req.setReq(query);
		Response<List<MsgVo>> resp = service.indexNewsMsg(req);
		System.out.println(resp);
	}
}
