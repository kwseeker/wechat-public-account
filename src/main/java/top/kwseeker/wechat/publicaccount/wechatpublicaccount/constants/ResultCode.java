package top.kwseeker.wechat.publicaccount.wechatpublicaccount.constants;

public enum ResultCode {

    OK(0, "ok"),

    // 1 通过code获取openid
    //   https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
    //        appid	是	应用唯一标识，在微信开放平台提交应用审核通过后获得
    //        secret	是	应用密钥AppSecret，在微信开放平台提交应用审核通过后获得
    //        code	是	填写第一步获取的code参数
    //        grant_type	是	填authorization_code
    //   正常返回：返回access_token信息
    //   错误返回样例："errcode":40029,"errmsg":"invalid code"

    INVALID_GRANTTYPE(40002, "invalid grant_type"),
    APPSECRET_MISSING(41004, "appsecret missing"),
    MISSING_CODE(41008, "missing code"),
    INVALID_CODE(40029,	"invalid code"),
    CODE_EXPIRED(42003,	"code expired"),
    INVALID_APPID(40013, "invalid appid"),

    // 2 通过openid获取用户信息
    //   https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
    //      access_token	是	调用接口凭证
    //      openid	是	普通用户的标识，对当前公众号唯一
    //      lang	否	返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
    //   正常返回：返回用户信息json数据包
    //   错误返回样例："errcode":40013,"errmsg":"invalid appid"
    ACCESS_TOKEN_EXPRIE(42001, "access_token expired"),
    INVALID_ACCESS_TOKEN(40014, "invalid access_token"),
    INVALID_OPENID(40003, "invalid openid"),
    MISSING_OPENID(41009, "missing openid"),


    // 3 发送模版消息
    //   https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN
    //        touser	是	接收者openid
    //        template_id	是	模板ID
    //        url	否	模板跳转链接（海外帐号没有跳转能力）
    //        miniprogram	否	跳小程序所需数据，不需跳小程序可不用传该数据
    //        appid	是	所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系，暂不支持小游戏）
    //        pagepath	否	所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar），要求该小程序已发布，暂不支持小游戏
    //        data	是	模板数据
    //        color	否	模板内容字体颜色，不填默认为黑色
    //   正常返回： errcode: 0  errmsg: ok

    INVALID_TEMPLATEID_SIZE(40036, "invalid template_id size"),
    INVALID_TEMPLATE_ID(40037, "invalid template_id"),
    TEMPLATE_SIZE_OUTOF_LIMIT(45012, "template size out of limit"),
    INVALID_URL_SIZE(40039, "invalid url size"),
    INVALID_URL_DOMAIN(40048, "invalid url domain"),
    EMPTY_POST_DATA(44002, "empty post data"),
    INVALID_URL(40066, "invalid url"),
    MISSING_URL(41010, "missing url"),
    URL_SIZE_OUTOF_LIMIT(45005, "url size out of limit");

    // 4 分享

    private int errcode;
    private String errmsg;

    ResultCode(int errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }
}
