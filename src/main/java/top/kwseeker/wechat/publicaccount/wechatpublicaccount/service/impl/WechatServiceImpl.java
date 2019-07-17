package top.kwseeker.wechat.publicaccount.wechatpublicaccount.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.config.WxConfig;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.config.WxServiceProperties;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.constants.Constants;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.service.WechatService;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.util.http.HttpUtil;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class WechatServiceImpl implements WechatService {

    @Autowired
    private WxConfig wxConfig;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    //private static String WxAccessToken = null;
    //private byte[] lock = new byte[0];

    /**
     * 刷新 access_token 并将结果存储在redis
     * access_token刷新url
     * https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
     */
    @Override
    public String refreshAccessToken() {
        WxServiceProperties properties = wxConfig.getWxServiceProperties();
        String accessTokenUrl = properties.getAccessTokenUrl();
        String grantType = properties.getGrantType();
        String appid = properties.getAppid();
        String secret = properties.getSecret();
        StringBuilder sb = new StringBuilder();
        sb.append(accessTokenUrl)
                .append("?grant_type=").append(grantType)
                .append("&appid=").append(appid)
                .append("&secret=").append(secret);
        String jsonStr = HttpUtil.doGet(sb.toString());
        log.info("New access_token json: {}", jsonStr);
        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        if(!jsonObject.containsKey("access_token")) {
            log.error("刷新 access_token 请求返回数据异常");
            return null;
        }
        String wxAccessToken = jsonObject.getString("access_token");
        Integer expiresIn = jsonObject.getInt("expires_in");
        //把access_token 放在 redis
        redisTemplate.opsForValue().set(Constants.WX_ACCESS_TOKEN_KEY, wxAccessToken, expiresIn, TimeUnit.MILLISECONDS);
        return wxAccessToken;
    }

    @Override
    public String getAccessToken() {
        String accessToken = (String) redisTemplate.opsForValue().get(Constants.WX_ACCESS_TOKEN_KEY);
        if(accessToken == null || "".equals(accessToken)) {
            accessToken = refreshAccessToken();
        }
        return accessToken;
    }
}
