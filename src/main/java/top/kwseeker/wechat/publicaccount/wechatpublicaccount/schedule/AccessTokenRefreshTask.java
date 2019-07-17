package top.kwseeker.wechat.publicaccount.wechatpublicaccount.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.service.WechatService;

/**
 * 定时刷新微信公众号 access_token, 如果单机架构直接存到service中就行了
 * 如果后面要拓展成分布式架构需要将access存到一个公共的地方，放redis吧
 */
@Slf4j
@Configuration
@EnableScheduling
public class AccessTokenRefreshTask {

    @Autowired
    private WechatService wechatService;

    @Lazy(false)
    @Scheduled(cron="0 */90 * * * ? ")   //每90分钟执行一次
    private void refreshAccessToken() {
        log.info("刷新 access_token");
        wechatService.refreshAccessToken();
    }
}
