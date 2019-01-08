package com.xianglin.appserv.common.util;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.net.MediaType;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Path;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 生成和识别QRcode
 *
 * @author wanglei 2016年12月10日下午4:05:09
 */
public class QRUtils {

    private static final Logger LOG = LoggerFactory.getLogger(QRUtils.class);

    private static final String FILEDIR = "/tmp/";

    private static final int WIDTH = 300;

    private static final int HEIGHT = 300;

    private static final int BLACK_COLOR = 0xFF000000;

    private static final int WHITE_COLOR = 0xFFFFFFFF;

    public static byte[] qrCreate(String content) throws Exception {

        int width = 200; // 图像宽度
        int height = 200; // 图像高度
        String format = "png";// 图像类型
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, "0");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, format, out);
        out.close();
        return out.toByteArray();
    }

    /**
     * 生成带logo的二维码
     *
     * @param content 二维码内容
     * @param logoUrl logo地址需要是https链接的图片地址
     * @return 返回的文件需要自行删除
     */
    public static File createQRCode(String content, String logoUrl) {

        File qrFile = null;
        int width = 300;
        int height = 300;
        String format = "jpeg";
        String contents = content;
        HashMap hashMap = new HashMap();
        hashMap.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hashMap.put(EncodeHintType.MARGIN, 1);
        try {
            Path tempQrFile = new File(FILEDIR + "temp_qr.png").toPath();   //已存在的一张二维码图片
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height, hashMap);
            MatrixToImageWriter.writeToPath(bitMatrix, format, tempQrFile);
            //读取二维码图片
            BufferedImage twodimensioncode = ImageIO.read(new File(tempQrFile.toString()));
            //获取画笔
            Graphics2D g = twodimensioncode.createGraphics();
            //读取logo图片
            File headImg = new File(FILEDIR + "tempImg.jpg");
            FileUtils.writeByteArrayToFile(headImg, HttpUtils.httpsImgDownload(logoUrl));
            BufferedImage logo = ImageIO.read(headImg);  //加入的log图片
            //设置二维码大小，太大，会覆盖二维码，此处20%
            int logoWidth = logo.getWidth() > twodimensioncode.getWidth() * 2 / 10 ? (twodimensioncode.getWidth() * 2 / 10) : logo.getWidth();
            int logoHeight = logo.getHeight() > twodimensioncode.getHeight() * 2 / 10 ? (twodimensioncode.getHeight() * 2 / 10) : logo.getHeight();
            //设置logo图片放置位置
            //中心
            int x = (twodimensioncode.getWidth() - logoWidth) / 2;
            int y = (twodimensioncode.getHeight() - logoHeight) / 2;
            //开始合并绘制图片
            g.drawImage(logo, x, y, logoWidth, logoHeight, null);
            g.drawRoundRect(x, y, logoWidth, logoHeight, 30, 30);
            //logo边框大小
            g.setStroke(new BasicStroke(2));
            //logo边框颜色
            g.setColor(Color.WHITE);
            g.drawRect(x, y, logoWidth, logoHeight);
            g.dispose();
            logo.flush();
            twodimensioncode.flush();
            qrFile = new File(FILEDIR + "qrFile.png");
            ImageIO.write(twodimensioncode, format, qrFile);

            tempQrFile.toFile().delete();
            headImg.delete();
        } catch (Exception e) {
            LOG.warn("createQRCode", e);
        }
        return qrFile;
    }

    /**
     * 生成带logo和文字的二维码
     *
     * @param content    二维码内容
     * @param logoUrl    logo地址需要是https链接的图片地址
     * @param buttonText 底部文字
     * @return 返回的文件需要自行删除
     */
    public static File createQRCode(String content, String logoUrl, String buttonText) {

        File qrFile = new File(FILEDIR + "qrFile-" + UUID.randomUUID().toString() + ".png");

        //===================参数校验
        Preconditions.checkArgument(!Strings.isNullOrEmpty(content));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(logoUrl));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(buttonText));

        String format = MediaType.JPEG.subtype();
        HashMap<EncodeHintType, Object> hashMap = new HashMap<>();
        hashMap.put(EncodeHintType.CHARACTER_SET, Charsets.UTF_8);
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hashMap.put(EncodeHintType.MARGIN, 1);
        try {

            //基础画布
            BufferedImage baseImage = new BufferedImage(WIDTH, HEIGHT,
                    BufferedImage.TYPE_INT_RGB);

            //二维码生成和读取
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hashMap);
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    baseImage.setRGB(x, y, bitMatrix.get(x, y) ? BLACK_COLOR : WHITE_COLOR);
                }
            }

            Graphics2D g = baseImage.createGraphics();
            //logo生成和读取
            ByteArrayInputStream inputLogo = new ByteArrayInputStream(HttpUtils.httpsImgDownload(logoUrl));
            BufferedImage logo = ImageIO.read(inputLogo); //加入的log图片
            int logoWidth = logo.getWidth() > WIDTH * 2 / 10 ? (WIDTH * 2 / 10) : logo.getWidth();
            int logoHeight = logo.getHeight() > HEIGHT * 2 / 10 ? (HEIGHT * 2 / 10) : logo.getHeight();
            //中心
            int x = (WIDTH - logoWidth) / 2;
            int y = (HEIGHT - logoHeight) / 2;
            //开始合并绘制图片
            g.drawImage(logo, x, y, logoWidth, logoHeight, null);
            g.drawRoundRect(x, y, logoWidth, logoHeight, 30, 30);
            //logo边框大小
            g.setStroke(new BasicStroke(2));
            //logo边框颜色
            g.setColor(Color.WHITE);
            g.drawRect(x, y, logoWidth, logoHeight);

            //底部文字
            g.setColor(Color.BLACK);
            Font font = new Font("宋体",Font.BOLD,14);
            g.setFont(font);
            g.drawString(buttonText, 20, HEIGHT - 3);

            g.dispose();
            logo.flush();
            baseImage.flush();

            ImageIO.write(baseImage, format, qrFile);
        } catch (Exception e) {
            LOG.warn("createQRCode", e);
        }
        return qrFile;
    }

    public static void main(String[] args) throws Exception {

        createQRCode("https://h5.xianglin.cn/home/nodeManager/downApp.html?sources=xianglinweb_channel", "https://appfile-dev.xianglin.cn/file/429009", "hello world");
//        String filePath = "D://";
//        String fileName = "zxing.png";
//        JSONObject json = new JSONObject();
//        json.put("zxing", "https://h5-dev.xianglin.cn/home/nodeManager/active/inviteFriends.html");
////		json.put("author", "shihy");
//        String content = json.toJSONString();// 内容
//        int width = 200; // 图像宽度
//        int height = 200; // 图像高度
//        String format = "png";// 图像类型
//        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
//        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//        hints.put(EncodeHintType.MARGIN, "1");
//        BitMatrix bitMatrix = new MultiFormatWriter().encode("https://www.baidu.com",
//                BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
//
//
//        Path path = FileSystems.getDefault().getPath(filePath, fileName);
//        MatrixToImageWriter.writeToPath(bitMatrix, format, path);// 输出图像
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        MatrixToImageWriter.writeToStream(bitMatrix, format, out);
//        System.out.println(out.size());
//        System.out.println("输出成功.");
//        out.close();
    }
}
