package com.xianglin.appserv.test;

import com.xianglin.appserv.common.service.facade.LoanService;
import com.xianglin.appserv.common.service.facade.app.PersonalService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.NodeVo;
import com.xianglin.appserv.common.service.facade.model.vo.UserVo;
import com.xianglin.appserv.common.util.Base64Utils;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.fala.session.MapSession;
import com.xianglin.fala.session.Session;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.swing.text.AbstractDocument;
import java.io.File;
import java.io.IOException;

/**
 * Created by wanglei on 2017/9/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-biz-spring.xml"})
//@Transactional
public class LoanServiceTest {

    @Autowired
    private LoanService service;

    @Autowired
    private SessionHelper sessionHelper;

    @Before
    public void initSession() {
        Session session = new MapSession();
        session.setAttribute(SessionConstants.PARTY_ID, 7000013L);
        sessionHelper.saveLocalSesson(session);
    }

    @Test
    public void fileUpload() throws IOException {
        String content = org.springframework.util.Base64Utils.encodeToString(FileUtils.readFileToByteArray(new File("D:\\2008sohu.jpg")));
        Response<String> resp = service.fileUpload("test", content);
        System.out.println(resp);
    }

    @Test
    public void fileDownload() throws IOException {
        Response<String> resp = service.fileDownload("184c9924-dc9a-4996-ab0d-2da9a60e9f19");
        if(resp.isSuccess()){
            FileUtils.writeByteArrayToFile(new File("D:\\123.jpg"), org.springframework.util.Base64Utils.decodeFromString(resp.getResult()));
        }
        System.out.println(resp);
    }
}
