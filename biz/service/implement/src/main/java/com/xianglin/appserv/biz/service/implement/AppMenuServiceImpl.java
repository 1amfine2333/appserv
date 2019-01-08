package com.xianglin.appserv.biz.service.implement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xianglin.appserv.biz.shared.util.LoginAttrUtil;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.dal.daointerface.AppMenuModelMapper;
import com.xianglin.appserv.common.dal.dataobject.AppMenuModel;
import com.xianglin.appserv.common.service.facade.AppMenuService;
import com.xianglin.appserv.common.service.facade.model.AppMenuDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.UserType;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.appserv.core.service.AppMenuCoreService;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;

@Service("appMenuService")
@ServiceInterface
public class AppMenuServiceImpl implements AppMenuService {
	private final Logger logger = LoggerFactory.getLogger(AppMenuServiceImpl.class);
	@Autowired
	private AppMenuCoreService appMenuCoreService;
	@Autowired
	private LoginAttrUtil loginAttrUtil;
	@Autowired
	private AppMenuModelMapper appMenuModelMapper;

	@Override
	@ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.AppMenuService.getAppMenus", description = "获取App菜单资源列表")
	public Response<List<AppMenuDTO>> getAppMenus(AppMenuDTO appMenuDTO) {
		Response<List<AppMenuDTO>> response = ResponseUtils.successResponse();
		List<AppMenuDTO> appMenuDTOsResult = new LinkedList<>();
		try {
			String wightPartyId = SysConfigUtil.getStr("live.pay.party.list");
			List<AppMenuDTO> appMenuDTOs = appMenuCoreService.getAppMenus(appMenuDTO);
			for (AppMenuDTO appMenuDTODB : appMenuDTOs) {
				if ("水电煤".equals(appMenuDTODB.getMname())) {
					if (loginAttrUtil.getAccountNodeManager() != null && !UserType.nodeManager.name().equals(loginAttrUtil.getAccountNodeManager().getUserRole())) {
						continue;
					}
					if (wightPartyId.indexOf(String.valueOf(loginAttrUtil.getPartyId())) == -1) {
						continue;
					}
					appMenuDTOsResult.add(appMenuDTODB);
				} else {
					appMenuDTOsResult.add(appMenuDTODB);
				}
			}
			response.setResult(new ArrayList<>(appMenuDTOsResult));
		} catch (BusiException e) {
			response = ResponseUtils.toResponse(e.getResponseEnum());
		} catch (Exception e) {
			logger.error("查询菜单列表失败", e);
			response = ResponseUtils.toResponse(ResponseEnum.SYSTEM_EXCEPTION);
		}
		return response;
	}

	/**
	 * 服务菜单界面化配置查询列表
	 */
	@Override
	public List<AppMenuDTO> queryMenu(AppMenuDTO req) {
		List<AppMenuModel> list = new ArrayList<AppMenuModel>();
		if (req.getMname() != null) {
			list = appMenuModelMapper.selectByMname(req.getMname());
		} else {
			if (req.getMid() != null) {
				list = appMenuModelMapper.selectAllByMid(req.getMid());
			} else {
				list = appMenuModelMapper.selectAll();
			}
		}
		List<AppMenuDTO> listAppMenuDTO = new ArrayList<AppMenuDTO>();
		for (AppMenuModel appMenuModel : list) {
			AppMenuDTO amd = new AppMenuDTO();
			amd.setPname(appMenuModelMapper.selectMname(appMenuModel.getPid()));
			amd.setPid(appMenuModel.getPid());
			amd.setMname(appMenuModel.getMname());
			amd.setMid(appMenuModel.getMid());
			amd.setMlevel(appMenuModel.getMlevel());
			amd.setMorder(appMenuModel.getMorder());
			amd.setMurl(appMenuModel.getMurl());
			amd.setImgurl(appMenuModel.getImgurl());
			amd.setMstatus(appMenuModel.getMstatus());
			amd.setCreateDate(appMenuModel.getCreateTime());
			amd.setUpdateDate(appMenuModel.getUpdateTime());
			BigDecimal bd = new BigDecimal(1);
			if (appMenuModel.getBusitype().equals(bd)) {
				amd.setBusitype1("便民");
			} else {
				amd.setBusitype1("其他");
			}

			amd.setCreateDate(appMenuModel.getCreateTime());
			if (appMenuModel.getUpdateTime() != null) {
				amd.setUpdateDate(appMenuModel.getUpdateTime());
			}
			listAppMenuDTO.add(amd);
		}
		return listAppMenuDTO;
	}

	/**
	 * 修改菜单操作
	 */
	public Boolean updateMenu(AppMenuDTO vo) {
		AppMenuModel amm = new AppMenuModel();
		Date createDate = appMenuModelMapper.selectCreateDate(vo.getMid());
		amm.setCreateTime(createDate);
		Date date = new Date();
		amm.setUpdateTime(date);
		amm.setMname(vo.getMname());
		amm.setPid(vo.getPid());
		amm.setMid(vo.getMid());
		amm.setMlevel(vo.getMlevel());
		amm.setMorder(vo.getMorder());
		amm.setMstatus(vo.getMstatus());
		if (!(vo.getMurl().isEmpty())) {
			amm.setMurl(vo.getMurl());
		}
		if (!(vo.getImgurl().isEmpty())) {
			amm.setImgurl(vo.getImgurl());
		}
		amm.setBusitype(vo.getBusitype());
		Boolean flag = appMenuModelMapper.updateByPrimaryKey(amm) == 1;

		return flag;
	}
}
