package com.xianglin.appserv.common.service.facade.model.vo;

/**
 * @author Yungyu
 * @description Created by Yungyu on 2018/4/2 15:08.
 */
public class LearningPPtVO extends ArticleVo {

    private String pptFileUrl;

    private String pptFileName;

    private String imageName;

    public String getPptFileUrl() {

        return pptFileUrl;
    }

    public void setPptFileUrl(String pptFileUrl) {

        this.pptFileUrl = pptFileUrl;
    }

    public String getPptFileName() {

        return pptFileName;
    }

    public void setPptFileName(String pptFileName) {

        this.pptFileName = pptFileName;
    }

    public String getImageName() {

        return imageName;
    }

    public void setImageName(String imageName) {

        this.imageName = imageName;
    }
}
