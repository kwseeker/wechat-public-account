package top.kwseeker.wechat.publicaccount.wechatpublicaccount.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 内部跳转的方式（二维码中只存储重定向路径，扫码后进入重定向路径，然后拼接长链接并跳转）实现简化二维码复杂度
 */
@RestController
public class ShortUrlRedirectController {

    private static final Logger logger = LoggerFactory.getLogger(ShortUrlRedirectController.class);

    @Value("${http.address.root}")
    private  String HTTP_ADDRESS_ROOT;// 项目域名

    @GetMapping("/redirect/{qrcodeId}")
    public void shortUrlRedirect(@PathVariable String qrcodeId, HttpServletResponse response) {
        try {
            //这里拼接实际的长链接，redirectUrl实际上可能很长
            String redirectUrl = "http://" + qrcodeId + "." + HTTP_ADDRESS_ROOT;
            logger.debug("微信扫码链接跳转: {}", redirectUrl);
            //重定向跳转到长链接
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("微信扫码链接跳转失败：e={}", e.getMessage());
        }
    }
}
