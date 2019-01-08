/**
 * 
 */
package com.xianglin.appserv.biz.service.implement;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.appserv.biz.shared.QRCodeManager;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.service.facade.QRCodeService;
import com.xianglin.appserv.common.service.facade.model.GroupDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.UserFigureDTO;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;

/**
 * 二维码相关服务实现类
 * 
 * @author pengpeng 2016年3月14日下午1:59:18
 */
//@ServiceInterface
public class QRCodeServiceImpl implements QRCodeService {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(QRCodeServiceImpl.class);


}
