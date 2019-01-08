package com.xianglin.appserv.core.service.impl;

import com.xianglin.appserv.common.dal.daointerface.*;
import com.xianglin.appserv.common.dal.dataobject.*;
import com.xianglin.appserv.common.service.facade.model.UserInfoDTO;
import com.xianglin.appserv.common.service.facade.model.enums.DataStatusEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.service.facade.model.vo.AppCommuseMenuDTO;
import com.xianglin.appserv.common.service.facade.model.vo.AppCommuseWordDTO;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.EmojiCharacterUtil;
import com.xianglin.appserv.core.service.UserCoreService;
import com.xianglin.cif.common.service.facade.req.PersonReq;
import com.xianglin.cif.common.service.facade.resp.PersonResp;
import com.xianglin.cif.common.service.facade.vo.PersonVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserCoreServiceImpl implements UserCoreService {
	private final static Logger LOGGER = LoggerFactory.getLogger(UserCoreServiceImpl.class);
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private AppCommuseMenuMapper appCommuseMenuMapper;
	@Autowired
	private AppCommuseWordMapper appCommuseWordMapper;
	@Autowired
	private AppCommuseMenuDAO appCommuseMenuDAO;
	@Autowired
	private AppCommuseWordDAO appCommuseWordDAO;

	@Override
	/*单表更新，放弃事物*/
	public void updateUserInfo(UserInfoDTO userInfoDTO) throws BusiException {
		try {
			//更新CIF信息
			PersonVo personVo = new PersonVo();
			personVo.setPartyId(userInfoDTO.getPartyId());
			personVo.setGender(userInfoDTO.getSex());
			personVo.setNickName(userInfoDTO.getNikerName());
			
			PersonReq personReq = new PersonReq();
			personReq.setVo(personVo);
			
//			PersonResp personResp = personServiceClient.updatePerson(personReq);
////			if(!ResponseConstants.FacadeEnums.OK.code.equals(personResp.getCode())){
////				throw new BusiException(ResponseEnum.BUSI_INVALD,"CIF更新个人信息失败");
////			}
			
			userInfoDTO.setPartyId(userInfoDTO.getPartyId());
			User user = new User();
			BeanUtils.copyProperties(user, userInfoDTO);
            if(user != null && user.getPartyId() != null){
                User u = userDAO.selectByPartyId(user.getPartyId());
                if(u != null){
                    user.setId(u.getId());
                    userDAO.updateByPrimaryKeySelective(user);
                }
            }
		}
//		catch (BusiException e) {
//			LOGGER.error("用户信息更新失败",e);
//			throw e;
//		}
		catch (Exception e) {
			LOGGER.error("用户信息更新失败",e);
			throw new BusiException(ResponseEnum.SYSTEM_EXCEPTION);
		}
	}

	@Override
	public void saveOrUpdateUserCommMenu(AppCommuseMenuDTO appCommuseMenuDTO) throws BusiException {
		try {
			AppCommuseMenuExample appCommuseMenuExample = new AppCommuseMenuExample();
			appCommuseMenuExample.createCriteria().andPartyidEqualTo(appCommuseMenuDTO.getPartyid())
				.andDataStatusEqualTo(DataStatusEnums.Y.getCode()).andMenuIdEqualTo(appCommuseMenuDTO.getMenuId());
			
			List<AppCommuseMenu> appCommuseMenus = appCommuseMenuMapper.selectByExample(appCommuseMenuExample);
			AppCommuseMenu appCommuseMenu = new AppCommuseMenu();
			if(CollectionUtils.isEmpty(appCommuseMenus)){//插入
				BeanUtils.copyProperties(appCommuseMenu, appCommuseMenuDTO);
				appCommuseMenu.setUseCount(1l);
				appCommuseMenu.setCreateTime(new Date());
				appCommuseMenuMapper.insertSelective(appCommuseMenu);
			}else{//更新
				appCommuseMenu.setUpdateTime(new Date());
				appCommuseMenu.setUseCount(appCommuseMenus.get(0).getUseCount() + 1l);
				appCommuseMenuMapper.updateByExampleSelective(appCommuseMenu, appCommuseMenuExample);
			}
		} catch (Exception e) {
			LOGGER.error("用户常用服务更新失败",e);
			throw new BusiException(ResponseEnum.SYSTEM_EXCEPTION);
		}
	}

	@Override
	public void saveOrUpdateUserCommWord(AppCommuseWordDTO appCommuseWordDTO) throws BusiException {
		try {
			AppCommuseWordExample appCommuseMenuExample = new AppCommuseWordExample();
			appCommuseMenuExample.createCriteria().andPartyidEqualTo(appCommuseWordDTO.getPartyid())
				.andDataStatusEqualTo(DataStatusEnums.Y.getCode()).andKeywordEqualTo(appCommuseWordDTO.getKeyword());
			
			List<AppCommuseWord> appCommuseWords = appCommuseWordMapper.selectByExample(appCommuseMenuExample);
			AppCommuseWord appCommuseWord = new AppCommuseWord();
			if(CollectionUtils.isEmpty(appCommuseWords)){//插入
				BeanUtils.copyProperties(appCommuseWord, appCommuseWordDTO);
				appCommuseWord.setUseCount(1l);
				appCommuseWord.setCreateTime(new Date());
				appCommuseWord.setKeyword(EmojiCharacterUtil.escape(appCommuseWord.getKeyword(),true,""));
				appCommuseWordMapper.insertSelective(appCommuseWord);
			}else{//更新
				appCommuseWord.setUpdateTime(new Date());
				appCommuseWord.setUseCount(appCommuseWords.get(0).getUseCount() + 1l);
				appCommuseWordMapper.updateByExampleSelective(appCommuseWord, appCommuseMenuExample);
			}
		} catch (Exception e) {
			LOGGER.error("用户常用词更新失败",e);
			throw new BusiException(ResponseEnum.SYSTEM_EXCEPTION);
		}
	}

	@Override
	public List<AppCommuseMenuDTO> queryTopUserCommMenu(AppCommuseMenuDTO appCommuseMenuDTO) throws BusiException {
		AppCommuseMenuModel appCommuseMenuModel = new AppCommuseMenuModel();
		try {
			BeanUtils.copyProperties(appCommuseMenuModel, appCommuseMenuDTO);
			List<AppCommuseMenuModel> appCommuseMenuModels = appCommuseMenuDAO.queryTopUserCommMenu(appCommuseMenuModel);
			
			return DTOUtils.map(appCommuseMenuModels, AppCommuseMenuDTO.class);
		} catch (Exception e) {
			LOGGER.error("获取前100条个人常用服务失败",e);
			throw new BusiException(ResponseEnum.SYSTEM_EXCEPTION);
		}
	}

	@Override
	public List<AppCommuseMenuDTO> queryTopCommMenu(AppCommuseMenuDTO appCommuseMenuDTO) throws BusiException {
		try {
			List<AppCommuseMenuModel> appCommuseMenuModels = appCommuseMenuDAO.queryTopCommMenu(null);
			
			return DTOUtils.map(appCommuseMenuModels, AppCommuseMenuDTO.class);
		} catch (Exception e) {
			LOGGER.error("获取前100条常用服务失败",e);
			throw new BusiException(ResponseEnum.SYSTEM_EXCEPTION);
		}
		
	}

	@Override
	public List<AppCommuseWordDTO> queryTopUserCommWord(AppCommuseWordDTO appCommuseWordDTO) throws BusiException {
		AppCommuseWord appCommuseWord = new AppCommuseWord();
		try {
			BeanUtils.copyProperties(appCommuseWord, appCommuseWordDTO);
			List<AppCommuseWord> appCommuseWords = appCommuseWordDAO.queryTopUserCommWord(appCommuseWord);
			
			return DTOUtils.map(appCommuseWords, AppCommuseWordDTO.class);
		} catch (Exception e) {
			LOGGER.error("获取前100条个人常用关键词失败",e);
			throw new BusiException(ResponseEnum.SYSTEM_EXCEPTION);
		}
	}

	@Override
	public List<AppCommuseWordDTO> queryTopCommWord(AppCommuseWordDTO appCommuseMenuDTO) throws BusiException {
		try {
			List<AppCommuseWord> appCommuseWords = appCommuseWordDAO.queryTopCommWord(null);
			
			return DTOUtils.map(appCommuseWords, AppCommuseWordDTO.class);
		} catch (Exception e) {
			LOGGER.error("获取前100条常用服务失败",e);
			throw new BusiException(ResponseEnum.SYSTEM_EXCEPTION);
		}
	}

}
