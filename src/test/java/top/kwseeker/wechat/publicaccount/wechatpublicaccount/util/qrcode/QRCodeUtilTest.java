package top.kwseeker.wechat.publicaccount.wechatpublicaccount.util.qrcode;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Method;
import java.util.UUID;

public class QRCodeUtilTest {

    @Test
    public void createImageTest() throws Exception {
        Class<?> clazz = QRCodeUtil.class;
        Method method = clazz.getDeclaredMethod("createImage", String.class, String.class, Color.class, int.class);
        method.setAccessible(true);
        //String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=sdsdsddfergerfergrerfeg&redirect_uri=" +
        //        "sdjiwehruwkiwjiefji shduhsudf 2/&response_type=code&scope=snsapi_base&state=2" +
        //        "#wechat_redirect";
        String url = "https://www.baidu.com";
        String url2 = "com";
        System.out.println("length: " + url.length());
        BufferedImage bi = (BufferedImage) method.invoke(null,url, "MAC", Color.ORANGE, 20);
        String file = UUID.randomUUID().toString() + ".jpg";
        File imageFile = new File(file);
        ImageIO.write(bi, "JPG", imageFile);
    }
}