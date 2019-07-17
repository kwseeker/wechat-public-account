package top.kwseeker.wechat.publicaccount.wechatpublicaccount.util.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.constants.Constants;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.util.validate.NullUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.UUID;

public class QRCodeUtil {

    private static final int QRCODE_SIZE = 300;
    // LOGO宽度
    private static final int WIDTH = 60;
    // LOGO高度
    private static final int HEIGHT = 60;

    /**
     * 生成中间为文字的二维码图片
     */
    private static BufferedImage createImage(String content, String logoText, Color logoBgColor, int logoFontSize) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); //纠错级别
        hints.put(EncodeHintType.CHARACTER_SET, Constants.UTF8);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (NullUtil.isNull(logoText)) {
            return image;
        }
        // 生成文字图片并插入
        BufferedImage bi = createTextImage(WIDTH, HEIGHT, logoFontSize, logoBgColor, logoText);

        int x1 = (width - WIDTH)/2;
        int y1 = (height - HEIGHT)/2;
        Graphics g = image.getGraphics();
        //g.drawImage(bi, x1, y1, WIDTH, HEIGHT, null);
        g.drawImage(bi, x1, y1, WIDTH, HEIGHT, null);
        g.dispose();
        return image;
    }

    private static BufferedImage createTextImage(int width, int height, int fontSize, Color bgColor, String content) throws IOException {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        g2.setBackground(bgColor);
        g2.clearRect(0, 0, width, height);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(new Font("微软雅黑", Font.PLAIN, fontSize));
        g2.setPaint(Color.BLACK);
        FontMetrics fontMetrics = g2.getFontMetrics();
        int x = (width - fontMetrics.stringWidth(content))/2;
        int y = (height - fontMetrics.getAscent() - fontMetrics.getDescent())/2 + fontMetrics.getAscent();
        g2.drawString(content, x , y);
        g2.dispose();
        return bi;
    }

    public static File createImageFile(BufferedImage image) throws IOException {
        String file = UUID.randomUUID().toString() + ".jpg";
        File imageFile = new File(file);
        ImageIO.write(image, "JPG", imageFile);
        return imageFile;
    }
}