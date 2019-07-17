package top.kwseeker.wechat.publicaccount.wechatpublicaccount.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * 总配置
 */
@Data
@Configuration
public class WxConfig {

    @Autowired
    private WxServiceProperties wxServiceProperties;
}
