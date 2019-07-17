package top.kwseeker.wechat.publicaccount.wechatpublicaccount.config;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
//@ConfigurationProperties(prefix = "wx.service.accessToken")
public class WxServiceProperties {

    @Value("${wx.service.accessToken.url}")
    private String accessTokenUrl;

    @Value("${wx.service.accessToken.grantType}")
    private String grantType;

    @Value("${wx.service.accessToken.appid}")
    private String appid;

    @Value("${wx.service.accessToken.secret}")
    private String secret;

}
