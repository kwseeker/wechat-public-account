package top.kwseeker.wechat.publicaccount.wechatpublicaccount.service;

public interface WechatService {

    /**
     * 刷新 access_token
     */
    String refreshAccessToken();

    /**
     * 获取 access_token
     */
    String getAccessToken();
}
