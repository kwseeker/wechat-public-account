package top.kwseeker.wechat.publicaccount.wechatpublicaccount.util.wechat;

import lombok.extern.slf4j.Slf4j;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.util.validate.NullUtil;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.util.encrypt.SHA1Util;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Slf4j
public class WxSignCheckUtil {

    /**
     * 微信接入签名验证
     * signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * timestamp	时间戳
     * nonce	    随机数
     * echostr	    随机字符串
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce, String WX_TOKEN) {
        try {
            if(NullUtil.isNull(signature) || NullUtil.isNull(timestamp) || NullUtil.isNull(nonce)) {
                log.error("签名校验字段不完整");
                return false;
            }
            if(NullUtil.isNull(WX_TOKEN)) {
                log.error("无效的微信公众号 token");
                return false;
            }
            String[] strArray = new String[] {WX_TOKEN, timestamp, nonce};
            //1）将token、timestamp、nonce三个参数进行字典序排序
            Arrays.sort(strArray);
            //2）将三个参数字符串拼接成一个字符串进行sha1加密
            StringBuffer sb = new StringBuffer();
            for (String str : strArray) {
                sb.append(str);
            }
            String sha1Str = SHA1Util.encrypt(sb.toString());
            log.debug("SHA1加密后字符串：{}", sha1Str);
            //3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
            return sha1Str != null && signature.equals(sha1Str);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
