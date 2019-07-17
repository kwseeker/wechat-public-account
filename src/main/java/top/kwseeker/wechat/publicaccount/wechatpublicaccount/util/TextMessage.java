package top.kwseeker.wechat.publicaccount.wechatpublicaccount.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class TextMessage implements Serializable {

    private static final long serialVersionUID = 2541634515458495122L;

    private String MsgType;
    private String ToUserName;
    private String FromUserName;
    private Long CreateTime;
    private String Content;
}
