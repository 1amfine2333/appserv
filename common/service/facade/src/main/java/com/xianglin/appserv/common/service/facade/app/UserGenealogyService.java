package com.xianglin.appserv.common.service.facade.app;

import com.xianglin.appserv.common.service.facade.model.GenealogyLinkDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.GenealogyLinkVO;
import com.xianglin.appserv.common.service.facade.model.vo.UserGenealogyVo;
import com.xianglin.appserv.common.service.facade.model.vo.WechatShareInfo;

import java.util.List;

/**
 * 个人家谱
 * Created by wanglei on 2017/11/9.
 */
public interface UserGenealogyService {

    /**
     * 查询家谱个人详情
     *
     * @param id
     * @return
     */
    Response<UserGenealogyVo> query(Long id);

    /**
     * 修改家谱成员信息
     *
     * @param userGenealogyVo
     * @return
     */
    Response<Boolean> update(UserGenealogyVo userGenealogyVo);

    /**
     * 添加家谱成员
     *
     * @param userGenealogyVo
     * @return
     */
    Response<Boolean> add(UserGenealogyVo userGenealogyVo);

    /**
     * 查询我的家谱
     *
     * @return
     */
    Response<List<UserGenealogyVo>> queryUserGenealogyVo();

    /**
     * 根据partyId查询家谱
     *
     * @param partyId
     * @return
     */
    Response<List<UserGenealogyVo>> queryUserGenealogyVoByParytId(Long partyId);

    /**
     * 删除家谱成员
     */
    Response<Boolean> deleteUserGenealogyVo(Long id);

    /**
     * 获取家谱拷贝链接id
     *
     * @param toPartyId
     * @return
     */
    Response<GenealogyLinkVO> privateCopyGenealogysId(Long toPartyId);

    /**
     * 内链确认拷贝家谱
     *
     * @param linkId
     * @return
     */
    Response<Boolean> privateCopyGenealogys(Long linkId);

    /**
     * 内链获取一键添加家谱id
     *
     * @param linkDTO
     * @return
     */
    Response<GenealogyLinkVO> publicAddGenealogyId(GenealogyLinkDTO linkDTO);

    /**
     * 获取外链卡片参数
     *
     * @return
     */
    Response<WechatShareInfo> publicShareLinkParam();

    /**
     * 外链获取一键添加家谱id
     *
     * @param linkDTO
     * @return
     */
    Response<Boolean> h5GenerateGenealogyId(GenealogyLinkDTO linkDTO);


    /**
     * 确认内链一键添加家谱
     *
     * @param linkId
     * @return
     */
    Response<Boolean> privateAddGenealogy(Long linkId);

    /**
     * 外链一键添加家谱 事件处理
     */
    void finishPublicAdd();
}
