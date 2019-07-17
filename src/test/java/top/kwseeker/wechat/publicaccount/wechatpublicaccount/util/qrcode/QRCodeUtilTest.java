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
        BufferedImage bi = (BufferedImage) method.invoke(null,"http://www.baidu.com", "MAC", Color.ORANGE, 20);
        String file = UUID.randomUUID().toString() + ".jpg";
        File imageFile = new File(file);
        ImageIO.write(bi, "JPG", imageFile);
    }
}