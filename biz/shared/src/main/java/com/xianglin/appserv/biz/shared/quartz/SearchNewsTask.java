/**
 *
 */
package com.xianglin.appserv.biz.shared.quartz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.TimeUtil;
import com.xianglin.appserv.common.dal.daointerface.MsgDAO;
import com.xianglin.appserv.common.dal.daointerface.SystemConfigMapper;
import com.xianglin.appserv.common.dal.daointerface.TaskDAO;
import com.xianglin.appserv.common.dal.dataobject.Msg;
import com.xianglin.appserv.common.dal.dataobject.SystemConfigModel;
import com.xianglin.appserv.common.dal.dataobject.Task;
import com.xianglin.appserv.common.dal.dataobject.UserMsg;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgNewsTag;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgStatus;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.MsgType;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.YESNO;
import com.xianglin.appserv.common.util.*;
import com.xianglin.appserv.common.util.constant.AppservConstants;
import com.xianglin.xlappfile.common.service.facade.FileDealService;
import com.xianglin.xlappfile.common.service.facade.base.CommonReq;
import com.xianglin.xlappfile.common.service.facade.base.CommonResp;
import com.xianglin.xlappfile.common.service.facade.vo.FileReqVo;
import com.xianglin.xlappfile.common.service.facade.vo.FileRespVo;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.File;
import java.io.IOException;
import java.lang.ref.Reference;
import java.net.URI;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 爬去新闻
 *
 * @author wanglei 2016年11月3日下午10:20:54
 */
public class SearchNewsTask {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(SearchNewsTask.class);

    /**
     * 任务ID
     */
    private static final String TASK_ID = "30001";

    @Autowired
    private TaskDAO taskDAO;

    @Autowired
    private MsgDAO msgDAO;

    @Autowired
    private FileDealService appFileService;

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Value("#{config['APP_FILESERVER_URL']}")
    private String appFileURL;//

    void execute() {
        try {
            Calendar calendar = Calendar.getInstance();
            String executeDate = DateFormatUtils.format(calendar, "yyyyMMdd");
            logger.info("start SearchNewsTask , excute date :{}", executeDate);
            TimeUnit.SECONDS.sleep(RandomUtils.nextInt(30));//任务执行前先暂停
            int result = taskDAO.updateExcutedByTaskId(AppservConstants.STATUS_EXECUTING, TASK_ID, executeDate);
            if (result == 0) {
                logger.info("other server is excuting this task !");
                return;
            }
            //搜索中国农业新闻网，去图片文字信息
//            Spider.create(this.new SearchCNFarmNews()).addUrl("http://www.farmer.com.cn/xwpd/btxw/").thread(1). run();

//			Spider.create(this.new SearchNews()).addUrl("http://www.farmer.com.cn/xwpd/btxw/201610/t20161028_1249480.htm").thread(5).run();
//            Spider.create(this.new SearchIfengNews()).addUrl("http://fashion.ifeng.com/health/").thread(1).run();

            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String nextExecuteDate = DateFormatUtils.format(calendar, "yyyyMMdd");
            taskDAO.updateEndByTaskId(AppservConstants.STATUS_UNEXECUTED, TASK_ID, executeDate, nextExecuteDate);
            logger.info("end SearchNewsTask task ,next excute date : {}", nextExecuteDate);
        } catch (Exception e) {
            logger.info("SendMsgTask", e);
        }
    }

    /**
     * 爬取中国农业新闻网
     *
     * @author wanglei 2016年11月16日下午5:28:24
     */
    class SearchCNFarmNews implements PageProcessor {

        private Site site = Site.me().setRetryTimes(3).setSleepTime(500);

        /**
         * @see us.codecraft.webmagic.processor.PageProcessor#getSite()
         */
        @Override
        public Site getSite() {
            return site;
        }

        /**
         * @see us.codecraft.webmagic.processor.PageProcessor#process(us.codecraft.webmagic.Page)
         */
        @Override
        public void process(Page page) {
            try {
                page.addTargetRequests(checkURL(page.getHtml().xpath("//div[@class='list-list']/div[1]").links().regex("(http://www\\.farmer\\.com\\.cn/xwpd/btxw/\\w+/\\w+\\.htm)").all()));
                String content = page.getHtml().xpath("//div[@class='TRS_Editor']").toString();
                String title = page.getHtml().xpath("//h1[@class='wtitle']").toString();
                String url = page.getUrl().toString();
                String titleImg = "";
                List<String> imgList = page.getHtml().xpath("//div[@class='TRS_Editor']").xpath("//img/@src").all();
                logger.debug("url:{},title:{},content:{}", url, title, content);
                saveMsg(title, content, titleImg, "中国农业新闻网", url, MsgNewsTag.TTXW.name(), imgList);
            } catch (Exception e) {
                logger.info("exception", e);
            }

        }
    }

    /**
     * 爬取凤凰网健康今日推荐
     *
     * @author wanglei 2016年11月16日下午5:33:07
     */
    class SearchIfengNews implements PageProcessor {

        private Site site = Site.me().setRetryTimes(3).setSleepTime(500);

        /**
         * @see us.codecraft.webmagic.processor.PageProcessor#getSite()
         */

        @Override
        public Site getSite() {
            return site;
        }

        /**
         * @see us.codecraft.webmagic.processor.PageProcessor#process(us.codecraft.webmagic.Page) // http://fashion.ifeng.com/a/20161116/40184969_0.shtml
         */
        @Override
        public void process(Page page) {
            try {
                page.addTargetRequests(checkURL(page.getHtml().xpath("//div[@class='box_wm']/div[1]").links().regex("(http://fashion\\.ifeng\\.com/a/\\w+/\\w+\\.shtml)").all()));
                String content = page.getHtml().xpath("//div[@id='main_content']").toString();
                String title = page.getHtml().xpath("//h1[@id='artical_topic']").toString();
                String url = page.getUrl().toString();
                String titleImg = "";
                List<String> imgList = page.getHtml().xpath("//div[@id='main_content']").replace("<a.*?>", "").xpath("//img/@src").all();
                logger.debug("url:{},title:{},content:{}", url, title, StringUtils.substring(content, 0, 30));
                saveMsg(title, content, titleImg, "凤凰健康", url, MsgNewsTag.TTXW.name(), imgList);
            } catch (Exception e) {
                logger.info("exception", e);
            }
        }
    }

    /**
     * 查询东方头条新闻
     */
    void executeSearchEastDay() {
        SystemConfigModel model = null;
        try {
            TimeUnit.SECONDS.sleep(RandomUtils.nextInt(30));//任务执行前先暂停
            logger.info("begin to executeSearchEastDay ");
            int result = systemConfigMapper.updateSyn("red.activity.switch","0","1");
            if (result == 0) {
                return;
            }
            HttpClientDownloader downloader = new HttpClientDownloader();
            Spider.create(this.new SearchEastDayNews()).addUrl("http://mini.eastday.com/").setDownloader(downloader).thread(1).runAsync();
            //爬取乡邻新闻
            Spider.create(this.new SearchXlNews()).addUrl("https://www.xianglin.cn/news.php?cid=1").setDownloader(downloader).thread(1).run();
            //爬取凤凰头条视频
            Spider.create(new SearchIfengVideo()).addUrl("http://v.ifeng.com/vlist/channel/105/showData/first_more.js").setDownloader(downloader).thread(1).run();
//			SearchJuhe();//取消爬取聚合数据
        } catch (Exception e) {
            logger.info("SendMsgTask", e);
        } finally {
            systemConfigMapper.updateSyn("red.activity.switch","1","0");
        }

    }

    /**
     * 爬取东方头条
     *
     * @author wanglei 2016年11月16日下午5:33:07
     */
    class SearchEastDayNews implements PageProcessor {

        private Site site = Site.me().setRetryTimes(3).setSleepTime(500);

        /**
         * @see us.codecraft.webmagic.processor.PageProcessor#getSite()
         */
        @Override
        public Site getSite() {
            return site;
        }

        /**
         * @see us.codecraft.webmagic.processor.PageProcessor#process(us.codecraft.webmagic.Page) // http://fashion.ifeng.com/a/20161116/40184969_0.shtml
         */
        @Override
        public void process(Page page) {
            try {
                if (StringUtils.equals(page.getUrl().toString(), "http://mini.eastday.com/")) {
                    List<String> urls = new ArrayList<>();
//                    urls.addAll(searchUrl("http://toutiao.eastday.com/toutiao_h5/RefreshJP?type=toutiao&picnewsnum=1", "RMZX"));
                    urls.addAll(searchUrl("http://toutiao.eastday.com/toutiao_h5/RefreshJP?type=shishang&picnewsnum=1", "SSCL"));
//                    urls.addAll(searchUrl("http://toutiao.eastday.com/toutiao_h5/RefreshJP?type=toutiao&picnewsnum=1", "YLXW"));
                    urls.addAll(searchUrl("http://toutiao.eastday.com/toutiao_h5/RefreshJP?type=jiankang&picnewsnum=1", "YS"));
                    urls.addAll(searchUrl("http://toutiao.eastday.com/toutiao_h5/RefreshJP?type=yinshi&picnewsnum=1", "YS"));
                    urls.addAll(searchUrl("http://toutiao.eastday.com/toutiao_h5/RefreshJP?type=baojian&picnewsnum=1", "YS"));

                    urls.addAll(searchUrl("http://toutiao.eastday.com/toutiao_h5/RefreshJP?type=guonei&picnewsnum=1", "RD"));//热点
                    urls.addAll(searchUrl("http://toutiao.eastday.com/toutiao_h5/RefreshJP?type=lishi&picnewsnum=1", "LS"));//历史
                    urls.addAll(searchUrl("http://toutiao.eastday.com/toutiao_h5/RefreshJP?type=yule&picnewsnum=1", "YL"));//娱乐
                    urls.addAll(searchUrl("http://toutiao.eastday.com/toutiao_h5/RefreshJP?type=junshi&picnewsnum=1", "JS"));//军事
                    urls.addAll(searchUrl("http://toutiao.eastday.com/toutiao_h5/RefreshJP?type=caijing&picnewsnum=1", "CJ"));//财经
                    urls.addAll(searchUrl("http://toutiao.eastday.com/toutiao_h5/RefreshJP?type=keji&picnewsnum=1", "KJ"));//科技
                    urls.addAll(searchUrl("http://toutiao.eastday.com/toutiao_h5/RefreshJP?type=tiyu&picnewsnum=1", "TY"));//体育

                    //临时使用
                    urls.addAll(searchUrl("http://toutiao.eastday.com/toutiao_h5/RefreshJP?type=shehui&picnewsnum=1", "QW"));//奇闻
//                    urls.addAll(searchUrl("http://toutiao.eastday.com/toutiao_h5/RefreshJP?type=baojian&picnewsnum=1", "YE"));//育儿
                    urls.addAll(searchUrl("http://toutiao.eastday.com/toutiao_h5/RefreshJP?type=xiaohua&picnewsnum=1", "GX"));//搞笑


                    page.addTargetRequests(checkURL(urls));
                } else {
                    String title = page.getHtml().xpath("//div[@class='article-title']").toString().trim();
                    String msgSource = page.getHtml().xpath("//div[@class='article-src-time']").toString();
                    if (StringUtils.isNotEmpty(msgSource)) {
                        msgSource = StringUtils.replacePattern(msgSource, "<.*?>", "");
                        msgSource = StringUtils.split(msgSource, "：")[1];
                    }
                    String content = page.getHtml().xpath("//div[@id='content']").toString();
                    String type = page.getUrl().get().split("=")[1];
                    String url = page.getUrl().toString();
                    String titleImg = "";
                    List<String> imgList = page.getHtml().xpath("//div[@id='content']").xpath("//img/@src").all();
                    saveMsg(title, content, titleImg, msgSource, url, type, imgList);
                }
            } catch (Exception e) {
                logger.info("exception", e);
            }
        }

        private List<String> searchUrl(String url, String type) {
            List<String> urls = new ArrayList<>();
            String resp = HttpUtils.executeGet(url);
            if (StringUtils.isNotEmpty(resp)) {
                String res = StringUtils.substring(resp, 5, resp.length() - 1);
                JSONObject obj1 = JSON.parseObject(res);
                JSONArray array = obj1.getJSONArray("data");
                for (int i = 0; i < array.size(); i++) {
                    JSONObject o = array.getJSONObject(i);
                    String u = o.getString("url");
                    urls.add(u + "?type=" + type);
                }
            }
            return urls;
        }
    }

    /**
     * 爬取乡邻小站新闻
     *
     * @author wanglei 2016年11月16日下午5:28:24
     */
    class SearchXlNews implements PageProcessor {

        private Site site = Site.me().setRetryTimes(3).setSleepTime(500);

        /**
         * @see us.codecraft.webmagic.processor.PageProcessor#getSite()
         */
        @Override
        public Site getSite() {
            return site;
        }

        /**
         * @see us.codecraft.webmagic.processor.PageProcessor#process(us.codecraft.webmagic.Page)
         */
        @Override
        public void process(Page page) {
            try {
                page.addTargetRequests(checkURL(page.getHtml().xpath("//ul[@id='idxnews_list']").links().all()));
                String content = page.getHtml().xpath("//div[@id='articont']/div").toString();
                String title = page.getHtml().xpath("//div[@id='articont']/p[@class='title']").toString();
                String url = page.getUrl().toString();
                String createdate = page.getHtml().xpath("//div[@id='articont']/p[@class='date']").toString();
                String titleImg = "";
                List<String> imgList = page.getHtml().xpath("//div[@id='articont']/div").xpath("//img/@src").all();
                logger.debug("url:{},title:{},content:{}", url, title, StringUtils.substring(content, 0, 30));

                JSONObject remark = new JSONObject();
                remark.put("createdate", DateUtils.parse("yyyy-MM-dd", StringUtils.replacePattern(createdate, "<.*?>", "")).getTime() + org.apache.commons.lang3.RandomUtils.nextInt(1000000, 10000000));
                saveMsg(Msg.builder().msgTitle(title).message(content)
                        .msgTag(MsgNewsTag.XLXW.name()).msgSource("乡邻小站").msgSourceUrl(url).remark(remark.toJSONString()).build(), imgList);
            } catch (Exception e) {
                logger.info("SearchXlNews exception", e);
            }

        }
    }

    /**
     * 爬取东方头条视频
     */
    class SearchIfengVideo implements PageProcessor {

        private Site site = Site.me().setRetryTimes(3).setSleepTime(500);

        /**
         * process the page, extract urls to fetch, extract the data and store
         *
         * @param page page
         */
        @Override
        public void process(Page page) {
            String url = page.getUrl().toString();
            System.out.println(url);
            if (StringUtils.endsWith(url, "js")) {
                List<String> urls = page.getHtml().xpath("//a/@href").all();
                String vUrl = "http://tv.ifeng.com/h6/{0}/video.json?callback=callback&msg={1}&rt=js&param=play&_={2}";
                List<String> vUrlList = new ArrayList<>(urls.size());
                for (String u : urls) {
                    String id = StringUtils.substringBeforeLast(StringUtils.substringAfter(u, "_"), ".");
                    String vHref = MessageFormat.format(vUrl, id, id, System.currentTimeMillis() + "");
                    vUrlList.add(vHref);
                }
                page.addTargetRequests(checkURL(vUrlList));
            } else {
                String value = page.getRawText();
                String video = StringUtils.substringBeforeLast(StringUtils.substringAfter(value, "="), ";callback");
                JSONObject obj = JSON.parseObject(video);
                JSONObject remark = new JSONObject();
                remark.put("duration", obj.getString("duration"));
                remark.put("aspect", obj.getString("aspect"));
                remark.put("videoPlayUrl", obj.getString("videoPlayUrl"));
                remark.put("createdate", DateUtils.parse("yyyy-MM-dd HH:mm:ss", obj.getString("createdate")).getTime());
                String title = obj.getString("title");
                String videoUrl = uploadToAliyun(obj.getString("videoPlayUrl"));
                String titleImg = uploadToAliyun(obj.getString("posterUrl"));
                saveMsg(Msg.builder().msgTitle(title).message(videoUrl).titleImg(titleImg)
                        .msgTag(MsgNewsTag.VIDEO.name()).msgSource("乡邻生活").msgSourceUrl(url).remark(remark.toJSONString()).build(), null);
            }
        }

        /**
         * get the site settings
         *
         * @return site
         * @see Site
         */
        @Override
        public Site getSite() {
            return site;
        }
    }

    /**
     * 图片处理
     *
     * @param url
     * @param imgList
     * @return
     */
    private Map<String, String> dealImg(String url, List<String> imgList) {
        Map<String, String> imgMap = new HashMap<>(imgList.size());
        try {
            URI u = new URI(url);
            for (String s : imgList) {
                String imgURL = u.resolve(s).toURL().toString();
                String fileImg = uploadToAliyun(imgURL);
                if (fileImg != null) {
                    imgMap.put(s, fileImg);
                }
            }
        } catch (Exception e) {
            logger.info("download img error {}", e.getMessage());
        }
        return imgMap;
    }


    /**
     * 判断该页面是否已经被搜索过
     *
     * @param urls 需要处理的图片地址
     * @return
     */
    private List<String> checkURL(List<String> urls) {
        Set<String> urlSet = new HashSet<>();
        Map<String, Object> paras = DTOUtils.queryMap();
        for (String url : urls) {
            paras.put("msgSourceToken", md5(url));
            paras.put("isDeleted", YESNO.NO.code);
            if (CollectionUtils.isEmpty(msgDAO.query(paras))) {
                urlSet.add(url);
            }
        }
        return new ArrayList<String>(urlSet);
    }

    /**
     * 新增新闻类型消息
     *
     * @param msg     消息
     * @param imgList 爬取的图片信息
     */
    private void saveMsg(Msg msg, List<String> imgList) {
        try {
            if (CollectionUtils.isNotEmpty(imgList)) {
                StringBuilder sb = new StringBuilder();
                int index = 0;
                Map<String, String> imgMap = dealImg(msg.getMsgSourceUrl(), imgList);
                for (Map.Entry<String, String> entry : imgMap.entrySet()) {
                    if (StringUtils.isEmpty(msg.getTitleImg())) {
                        msg.setTitleImg(entry.getValue());
                    }
                    if (++index <= 3) {
                        sb.append(entry.getValue()).append(",");
                    }
                    msg.setMessage(msg.getMessage().replace(entry.getKey(), entry.getValue()));
                }
                msg.setTitleImgList(sb.substring(0, sb.length() - 1));
            }
            msg.setMsgType(MsgType.NEWS.name());
            msg.setMsgTitle(StringUtils.replacePattern(StringUtils.replacePattern(msg.getMsgTitle(), "<.*?>", ""),"\\s*|\t|\r|\n",""));
            msg.setMessage(EmojiCharacterUtil.escape(msg.getMessage().replaceAll("<a.*?>", ""), true, ""));
            msg.setMsgSourceToken(md5(msg.getMsgTitle()));//修改为针对名字进行排重
            msg.setStatus(MsgStatus.INIT.code);
            msg.setLoginCheck(YESNO.NO.code);
            msg.setPassCheck(YESNO.NO.code);
            msg.setCreator("timeSearcher");
            msg.setIsDeleted(YESNO.NO.code);
            msg.setExpiryTime(0);
            msg.setCreateTime(getRandomDate());
            msgDAO.insertExceptUrl(msg);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    /**
     * 保存消息信息
     *
     * @param title        标题
     * @param content      内容
     * @param titleImg     标题图片
     * @param msgSource    来源网站
     * @param msgSourceUrl 来源url
     * @param msgTag       消息类别
     * @param imgList      原始图片地址列表
     */
    private void saveMsg(String title, String content, String titleImg, String msgSource, String msgSourceUrl, String msgTag, List<String> imgList) {
        Msg msg = Msg.builder().msgTitle(title).message(content).titleImg(titleImg).msgSource(msgSource).msgSourceUrl(msgSourceUrl).msgTag(msgTag).build();
        saveMsg(msg, imgList);
    }

    /**
     * 在一个小时内随机时间
     *
     * @return
     */
    private Date getRandomDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, RandomUtils.nextInt(60));
        cal.add(Calendar.HOUR_OF_DAY, -1);
        return cal.getTime();
    }

    /**
     * 文件上传(上传阿里云)
     *
     * @param url
     * @return
     */
    private String uploadToAliyun(String url) {
        String returnUrl = null;
        try {
            URL u = new URI(url).toURL();
            byte[] fileBytes;
            if (StringUtils.equalsIgnoreCase(u.getProtocol(), "http")) {
                fileBytes = HttpUtils.httpImgDownload(url);
            } else {
                fileBytes = HttpUtils.httpsImgDownload(url);
            }
            String last = StringUtils.substringAfterLast(url, ".");
            File file = new File("/tmp/" + UUID.randomUUID().toString() + "." + last);
            FileUtils.writeByteArrayToFile(file, fileBytes);
            //上传文件到File服务器
            returnUrl = AliyunUtil.uploadSearchFile(file);
        } catch (Exception e) {
            logger.info("Failed to connect to url ", e);
        }
        return returnUrl;
    }

    /**
     * 图片上传(上传到文件服务器)
     *
     * @param url
     * @return
     */
    private String uploadImg(String url) {
        String returnUrl = null;
        try {
            URL u = new URI(url).toURL();
            byte[] fileBytes;
            if (StringUtils.equalsIgnoreCase(u.getProtocol(), "http")) {
                fileBytes = HttpUtils.httpImgDownload(url);
            } else {
                fileBytes = HttpUtils.httpsImgDownload(url);
            }
            //上传文件到File服务器
            if (fileBytes != null && fileBytes.length > 0) {
                CommonReq<FileReqVo> commonReq = new CommonReq<>();
                FileReqVo vo = new FileReqVo();
                vo.setData(fileBytes);
                vo.setFileSize(fileBytes.length);
                vo.setFileType("1");
                vo.setFileName(UUID.randomUUID().toString());
                commonReq.setBody(vo);
                CommonResp<FileRespVo> fileResp = appFileService.uploadImgFile(commonReq);
                if (fileResp.getBody() != null) {
                    returnUrl = appFileURL + fileResp.getBody().getId();
                }
            }

        } catch (Exception e) {
            logger.info("Failed to connect to url ", e);
        }
        return returnUrl;
    }

    /**
     * 爬取聚合数据健康资讯
     */
    private void SearchJuhe() {
        try {
            logger.info("search juhe data");
            String resp = HttpUtils.executeGet("http://op.juhe.cn/yi18/news/list?key=1ff289ebd896db49787e91f270765a98");
            if (StringUtils.isNotEmpty(resp)) {
                logger.info("search juhe data {}", resp);
                JSONObject jo = JSON.parseObject(resp);
                JSONArray array = jo.getJSONObject("result").getJSONArray("list");
                if (array.size() > 0) {
                    for (int i = 0; i < array.size(); i++) {
                        jo = array.getJSONObject(i);
                        addJuheDetail(jo.getString("id"));
                    }
                }
            }
        } catch (Exception e) {
            logger.info("SearchJuhe", e);
        }
    }

    private void addJuheDetail(String id) {
        try {
            String resp = HttpUtils.executeGet("http://op.juhe.cn/yi18/news/show?key=1ff289ebd896db49787e91f270765a98&id=" + id);
            logger.info("search juhe data {}", resp);
            if (StringUtils.isNotEmpty(resp)) {
                JSONObject jo = JSON.parseObject(resp).getJSONObject("result");
                saveMsg(jo.getString("title"), jo.getString("message"), jo.getString("img"), "聚合数据", "http://op.juhe.cn/yi18/news/show?key=1ff289ebd896db49787e91f270765a98&id=" + id, MsgNewsTag.JKZX.name(), null);
            }
        } catch (Exception e) {
            logger.info("SearchJuhe", e);
        }
    }

    /**
     * MD加密
     *
     * @param input 输入数据
     * @return
     */
    private String md5(String input) {
        try {
            if (StringUtils.isNotEmpty(input)) {
                return MD5.encode(input);
            }
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
//		URI i = new URI("https://03.imgmini.eastday.com/mobile/20161206/20161206164403_d4ee4d0466c26cc61d2e5e1f4ecacd24_1.jpeg");
//		System.out.println(i.toURL().getProtocol());
//		String resp = HttpUtils.executeGet("http://toutiao.eastday.com/toutiao_h5/RefreshJP?type=shehui&picnewsnum=1");
//		System.out.println(resp.substring(4));
//        byte[] bs = HttpUtils.httpImgDownload("https://appfile-dev.xianglin.cn/file/56881");
////				HttpUtils.httpsImgDownload("https://03.imgmini.eastday.com/mobile/20161206/20161206164403_d4ee4d0466c26cc61d2e5e1f4ecacd24_1.jpeg");
//        if (bs != null) {
//            FileUtils.writeByteArrayToFile(new File("D:\\SVN\\front\\1.jpg"), bs);
//        }
        String result = Md5Crypt.apr1Crypt("123456");
        System.out.println(result);

        int i = 1;
        while (i < 100) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MINUTE, RandomUtils.nextInt(60));
            i ++;
            System.out.println(cal);
        }

        System.out.println(StringUtils.replacePattern(" \n" +
                " 无锡市药械不良反应/事件监测业务测评成绩位列全省第一 \n","\\s*|\t|\r|\n",""));
    }
}
