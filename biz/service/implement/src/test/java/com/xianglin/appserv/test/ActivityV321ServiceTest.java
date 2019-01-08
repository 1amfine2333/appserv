package com.xianglin.appserv.test;

import com.xianglin.appserv.biz.shared.quartz.ActivityInviteTask;
import com.xianglin.appserv.common.service.facade.app.ActivityV321Service;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.vo.MsgVo;
import com.xianglin.appserv.common.util.SessionHelper;
import com.xianglin.cif.common.service.facade.constant.SessionConstants;
import com.xianglin.fala.session.MapSession;
import com.xianglin.fala.session.Session;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by wanglei on 2017/9/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-biz-spring.xml"})
//@Transactional
public class ActivityV321ServiceTest {

    @Autowired
    private ActivityV321Service service;

    @Autowired
    private SessionHelper sessionHelper;

    @Autowired
    private ActivityInviteTask activityInviteTask;

    @Before
    public void initSession() {
        Session session = new MapSession();
        session.setAttribute(SessionConstants.PARTY_ID, 1000000000002121L);
        sessionHelper.saveLocalSesson(session);
    }

    @Test
    public void createInvite() {
        Response<Integer> resp = service.createInvite(1000000000002000L,"18212271231");
        System.out.println(resp);
    }

    @Test
    public void queryInviteCount() {
        Response<Integer> resp = service.queryInviteCount(1000000000002000L);
        System.out.println(resp);
    }

    @Test
    public void shareReward() {
        Response<Boolean> resp = service.shareReward(1000000000002000L);
        System.out.println(resp);
    }

    @Test
    public void rewardInvite() {
        activityInviteTask.rewardInvite();
    }

    public static void main(String[] args) throws IOException, InvocationTargetException, IllegalAccessException {
        List<String> list = FileUtils.readLines(new File("d:\\Users\\wanglei\\Desktop\\gold.txt"));
        String sql = "update cif_goldcoin_account set AMOUNT = AMOUNT - {0},UPDATE_TIME = NOW(),COMMENTS = ''DM-1119'' where PARTY_ID = {1};\n" +
                "update cif_goldcoin_account set AMOUNT = AMOUNT + {2},UPDATE_TIME = NOW(),COMMENTS = ''DM-1119'' where PARTY_ID = 10000;\n" +
                "INSERT INTO `xlstation`.`cif_goldcoin_record` (`ID`, `REQUEST_ID`, `SYSTEM`, `BATCH_ID`, `FRON_PARTY_ID`, `TO_PARTY_ID`, `AMOUNT`, `BALANCE`, `TYPE`, `REMARK`, `STATUS`, `IS_DELETED`, `CREATE_TIME`, `UPDATE_TIME`, `COMMENTS`) \n" +
                "VALUES (NULL, ''{3}'', ''app'', NULL, ''10000'', {4}, -{5}, NULL, ''CLEAR_ACCOUNT'', ''封号'', ''S'', ''N'', NOW(), NOW(), ''DM-1119'');\n" ;
//                "update xlstation.cif_goldcoin_record set IS_DELETED = ''Y'',UPDATE_TIME = NOW(),COMMENTS = ''DM-1044'' WHERE REQUEST_ID = ''{6}'';\n" +
//                "UPDATE xlstation.te_transaction set IS_DELETED = 1,UPDATE_DATE = NOW(),COMMENTS = ''DM-1044'' WHERE TRANSACTION_NUMBER = ''{7}'';\n" +
//                "update xlacdb.xla_acct_balance set BALANCE = BALANCE - {8},DEBIT_BALANCE = DEBIT_BALANCE - {9},UPDATE_DATE = NOW(),COMMENTS = ''DM-1044'' WHERE ACCT_BAL_CODE = ''{10}'';\n";


        List<String> result = new ArrayList<>();
        for(String s:list){
//            System.out.println(s);
            String[] strs = s.split("-");
            if(strs.length > 1){
                String partyId = strs[0];
                String amount = strs[1];
//                String balance = strs[4];
//                String orderNum = strs[5];
//                String tranNum = strs[6];
//                String actCode = strs[9];
                String tranNo = createTranNo(partyId);

//                System.out.println(partyId+" = "+amount+" = "+balance+" = "+orderNum+" = "+tranNum+" = "+actCode+" = "+tranNo);

//                System.out.println(strs[4] + " -- " +strs[5]+" --  "+strs[9]);
//                System.out.println( MessageFormat.format(sql,amount,partyId,amount,tranNo,partyId,amount));
                result.add(MessageFormat.format(sql,amount,partyId,amount,tranNo,partyId,amount));
            }
        }
//        FileUtils.writeLines(new File("d:\\gold.sql"),result);
//        System.out.println(StringUtils.substring("18621898968",0,3)+"***"+StringUtils.substring("18621898968",7));
//        System.out.println(Arrays.stream("MERCHANT,NOTIFY,ALARM,ARTICLE".split(",")).anyMatch("ARTICLE"::equalsIgnoreCase));

        Map<String,String> map = new HashMap<>();
        map.put("msgTitle","2321321");
        map.put("msgType","2421adas");
        map.put("msgTag","sfasdasds");
        map.put("titleImg","dsadsad");
        map.put("message","dsdadssad");
        map.put("status","dsasdsafasd");
        MsgVo vo = new MsgVo();
        org.apache.commons.beanutils.BeanUtils.copyProperties(vo,map);
        System.out.println(vo);
    }

    private static String createTranNo(String partyId){
        StringBuilder sb = new StringBuilder();
        sb.append(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
        for (int i = 0; i < 10; i++) {
            if (partyId.length() > i) {
                sb.append(partyId.charAt(i));
            } else {
                sb.append(RandomUtils.nextInt(0,10));
            }
        }
        sb.append(RandomUtils.nextLong(1000000000000L,1000000000000000L));
        return sb.substring(0, 30);
    }

}
