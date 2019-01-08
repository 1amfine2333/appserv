package com.xianglin.appserv.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CompleteMultipartUploadResult;
import com.aliyun.oss.model.UploadFileRequest;
import com.aliyun.oss.model.UploadFileResult;
/*import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.*;*/
import com.xianglin.appserv.common.util.HttpUtils;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import sun.misc.Perf;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import javax.xml.bind.SchemaOutputResolver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglei on 2017/11/9.
 */
public class SearchTest {

    public static void main(String[] args) throws Throwable {
//        Spider.create(new SearchVideo()).addUrl("http://videoapi.dftoutiao.com/VideositeView/pcrefresh?param=ttdshipin15096029525694488toutiao_video_pcDFTT_VIDEO_PC1&typeid=700001&num=36&jsonpcallback=jQuery18309837145625087748_1510220384033&_=1510220384897").thread(5).run();
//        byte[] data = HttpUtils.httpImgDownload("http://v6-tt.ixigua.com/video/m/220eb1b0cfae3f84c949bcdbe0447037b2e1151530c00008caaf923cb99/?Expires=1510297593&AWSAccessKeyId=qh0h9TdcEMoS2oPj7aKX&Signature=o3Lz%2BPZM0Vfipf1Z%2BOiJtmkvktU%3D");
//        System.out.println(data.length);
//
//        Spider.create(new SearchTodayNew()).addUrl("http://v.ifeng.com/vlist/channel/105/showData/first_more.js").thread(5).run();
//        System.out.println(1);
//        Spider.create(new SearchVideo()).addUrl("http://v.ifeng.com/video_9290121.shtml").thread(5).run();
//        Spider.create(new SearchToutiaoNew()).addUrl("https://www.toutiao.com").thread(5).run();

        String html = HttpUtils.executeGet("https://www.toutiao.com/a6499682411373134349/");
        System.out.println(html);
//        testAliyun();
//        uploadAliyunVod();
        //getURL();
    }

    /*private static void getURL() throws ClientException {
        final String accessKeyId = "LTAIqBCrRyjR66Um";
        //账号AK信息请填写(必选)
        final String accessKeySecret = "v1RUDztVgatawBiTMOpyMHZB1VfBA6";
        DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai", accessKeyId, accessKeySecret);
        GetPlayInfoRequest request = new  GetPlayInfoRequest();
        request.setVideoId("cea63227072e4bb6aa19a9ef6f59854d");

//        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
//        request.setVideoId("cea63227072e4bb6aa19a9ef6f59854d");

        DefaultAcsClient client = new DefaultAcsClient(profile);
        GetPlayInfoResponse response = client.getAcsResponse(request);
//        System.out.println(ToStringBuilder.reflectionToString(response.getVideoMeta()));
        System.out.println(ToStringBuilder.reflectionToString(response));
    }


    private static void uploadAliyunVod(){
        //账号AK信息请填写(必选)
        final String accessKeyId = "LTAIqBCrRyjR66Um";
        //账号AK信息请填写(必选)
        final String accessKeySecret = "v1RUDztVgatawBiTMOpyMHZB1VfBA6";
        String title = "14325086-102-998767-1634121.mp4";
        String fileName = "d:\\14325086-102-998767-1634121.mp4";
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
//        request.setPartSize(10 * 1024 * 1024L);     //可指定分片上传时每个分片的大小，默认为10M字节
        request.setTaskNum(1);   //可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）
        request.setIsShowWaterMark(true);           //是否使用默认水印
//        request.setCallback("http://callback.sample.com");  //设置上传完成后的回调URL(可选)
        request.setCateId(0);                       //视频分类ID(可选)
        request.setTags("标签1,标签2");              //视频标签,多个用逗号分隔(可选)
//        request.setDescription("视频描述");          //视频描述(可选)
//        request.setCoverURL("http://cover.sample.com/sample.jpg"); //封面图片(可选)
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        System.out.println(ToStringBuilder.reflectionToString(response));
    }*/

    /**
     * 爬取头条新闻
     */
    static class SearchToutiaoNew implements PageProcessor {

        private Site site = Site.me().setRetryTimes(3).setSleepTime(500);

        /**
         * process the page, extract urls to fetch, extract the data and store
         *
         * @param page page
         */
        @Override
        public void process(Page page) {
            try {
                if (StringUtils.equals(page.getUrl().toString(), "https://www.toutiao.com")) {
                    List<String> urls = new ArrayList<>();
                    //社会
                    urls.addAll(searchUrl("https://lf.snssdk.com/api/news/feed/v46/?category=news_society&refer=1&refresh_reason=1&count=20&min_behot_time=1513663113&last_refresh_sub_entrance_interval=1513663150&loc_mode=7&tt_from=pull&cp=5ba03a8dabaaeq1&iid=20212830587&device_id=40649785016&ac=wifi&channel=lite_xiaomi&aid=35&app_name=news_article_lite&version_code=619&version_name=6.1.9&device_platform=android&ab_version=201714%2C230915%2C226951%2C233419%2C230230%2C228778%2C206076%2C233576%2C201710%2C229305%2C234151%2C232426%2C230353%2C233284&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7&ab_feature=159180%2Cz1&abflag=3&ssmix=a&device_type=MIX+2&device_brand=Xiaomi&language=zh&os_api=25&os_version=7.1.1&uuid=99001021361498&", "QW"));//奇闻
                    //美文
//                    urls.addAll(searchUrl("https://www.toutiao.com/api/pc/feed/?category=news_essay&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true&as=A105FA63D89B7D0&cp=5A38FBE76D302E1&_signature=W0lPOAAAAXKAdcffghfpnFtJTy", "WE"));//奇闻
                    //育儿
//                    urls.addAll(searchUrl("https://www.toutiao.com/api/pc/feed/?category=news_baby&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true&as=A175FA9308BB429&cp=5A38FBA4B2D95E1&_signature=p5WYUwAA.bx8qRC03-4WN6eVmE", "YE"));//育儿
                    //搞笑
//                    urls.addAll(searchUrl("https://www.toutiao.com/api/pc/feed/?category=funny&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true&as=A1759AC3786B81D&cp=5A38FB78B1CD9E1&_signature=W4cGigAAAb-Au45t8rmK71uHBp", "GX"));//搞笑

                    page.addTargetRequests(urls);
                    for(String s:urls){
                        System.out.println(s);
                    }
                } else {
                    String title = page.getHtml().xpath("//h1[@class='article-title']").toString();

                    Json json = page.getJson();
                    String navi = page.getRawText();
                    String msgSource = page.getHtml().xpath("//div[@class='article-sub']/span").toString();
                    if (StringUtils.isNotEmpty(msgSource)) {
                        msgSource = StringUtils.replacePattern(msgSource, "<.*?>", "");
                        msgSource = StringUtils.split(msgSource, "：")[1];
                    }
                    String content = page.getHtml().xpath("//div[@class='article-content']").toString();
                    String type = page.getUrl().get().split("=")[1];
                    String url = page.getUrl().toString();
                    String titleImg = "";
                    List<String> imgList = page.getHtml().xpath("//div[@id='content']").xpath("//img/@src").all();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private List<String> searchUrl(String url, String type) {
            List<String> urls = new ArrayList<>();
            String resp = HttpUtils.executeGet(url);
            System.out.println(resp);
            if (StringUtils.isNotEmpty(resp)) {
//                String res = StringUtils.substring(resp, 5, resp.length() - 1);
                JSONObject obj1 = JSON.parseObject(resp);
                JSONArray array = obj1.getJSONArray("data");
                for (int i = 0; i < array.size(); i++) {
                    JSONObject o = array.getJSONObject(i);
                    JSONObject item = JSON.parseObject(o.getString("content"));
                    System.out.println(item);
                    urls.add(item.getString("share_url") + "?type=" + type);
                }
            }
            return urls;
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

    static class SearchTodayNew implements PageProcessor {

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
                    System.out.println(id);
                    String vHref = MessageFormat.format(vUrl, id, id, System.currentTimeMillis() + "");
                    vUrlList.add(vHref);
                }
                page.addTargetRequests(vUrlList);
            } else {
                String value = page.getRawText();
                System.out.println(value);
                String video = StringUtils.substringBeforeLast(StringUtils.substringAfter(value, "="), ";callback");
                JSONObject obj = JSON.parseObject(video);
                String title = obj.getString("title");
                String videoUrl = obj.getString("videoPlayUrl");
                File file = new File(title);
                try {
                    FileUtils.writeByteArrayToFile(file, HttpUtils.httpImgDownload(videoUrl));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("video +\n" + video);
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

    static void testAliyun() {
        try {
            String bucketName = "xianglin002";
            String key = "youhui0001";

            String endpoint = "https://oss-cn-beijing.aliyuncs.com";
            String accessKeyId = "LTAIqBCrRyjR66Um";
            String accessKeySecret = "v1RUDztVgatawBiTMOpyMHZB1VfBA6";
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

            String uploadFile = "D:\\youhui0001.png";
            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, key);
            // The local file to upload---it must exist.
            uploadFileRequest.setUploadFile(uploadFile);
            // Sets the concurrent upload task number to 5.
            uploadFileRequest.setTaskNum(5);
            // Sets the part size to 1MB.
//        uploadFileRequest.setPartSize(1024 * 1024 * 1);
            // Enables the checkpoint file. By default it's off.
            uploadFileRequest.setEnableCheckpoint(true);

            UploadFileResult uploadResult = ossClient.uploadFile(uploadFileRequest);
            CompleteMultipartUploadResult multipartUploadResult = uploadResult.getMultipartUploadResult();
            System.out.println("location = " + multipartUploadResult.getLocation());
            System.out.println(multipartUploadResult.getETag());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    static class SearchVideo implements PageProcessor {

        private Site site = Site.me().setRetryTimes(3).setSleepTime(500);

        /**
         * process the page, extract urls to fetch, extract the data and store
         *
         * @param page page
         */
        @Override
        public void process(Page page) {
            String body = page.getRawText();

            if (StringUtils.startsWith(body, "jQuery")) {
                String jsonObj = StringUtils.substringBeforeLast(StringUtils.substringAfter(body, "("), ")");
                JSONObject obj = JSON.parseObject(jsonObj);
                JSONArray array = obj.getJSONArray("data");

                for (int i = 0; i < array.size(); i++) {
                    JSONObject temp = array.getJSONObject(i);
                    String title = temp.getString("title");
                    String htmlname = temp.getString("htmlname");
                    page.addTargetRequest("http://video.eastday.com/a/" + htmlname);
                    System.out.println("htmlname = " + htmlname + " title = " + title);
                    break;
                }
            } else {
                System.out.println(body);
                body = page.getHtml().toString();
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
}

