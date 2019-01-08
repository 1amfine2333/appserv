package com.xianglin.appserv.common.service.facade;

import com.xianglin.appserv.common.service.facade.model.Response;

/**
 * Created by wanglei on 2017/9/26.
 */
public interface LoanService {

    /** 乡邻贷通用请求接口
     * @param serviceNum 接口编号
     * @param param 请求参数，json格式
     * @return
     */
    Response<String> currencyApi(String serviceNum,String param);

    /**文件上传
     * @param fileType 文件类型
     * @param imgCOntent 文件 base64格式
     * @return 文件md5
     */
    Response<String> fileUpload(String fileType,String imgCOntent);

    /**文件下载
     * @param fileMd 文件md5名
     * @return 文件base64格式
     */
    Response<String> fileDownload(String fileMd);

    /**校验兵同步身份证
     * @param trueName 身份证名
     * @param authNu 身份证号
     * @return
     */
    Response<Boolean> checkAuth(String trueName,String authNu);
}
