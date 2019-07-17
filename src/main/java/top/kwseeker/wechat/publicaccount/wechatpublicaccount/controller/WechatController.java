package top.kwseeker.wechat.publicaccount.wechatpublicaccount.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.util.validate.NullUtil;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.util.ResponseUtil;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.util.wechat.WxSignCheckUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class WechatController {

    @Value("${wx.service.token}")
    private String WX_TOKEN;

    @GetMapping(path = {"/hello", "/"})
    public String greet() {
        log.info("接口测试");
        return "嗨，欢迎访问我的微信号";
    }

    /**
     * 微信接入签名校验
     * 首次用于校验接入签名，
     * 后面用于接收微信服务器推送过来的消息和事件
     */
    @GetMapping("/checksign")
    public void checkAccessSignature(HttpServletRequest request, HttpServletResponse response) {
        log.info("校验接入签名");
        String echostr = request.getParameter("echostr");
        if(NullUtil.isNull(echostr)) {
            log.error("校验接入签名失败，echostr为空");
            ResponseUtil.printJson(response, "");
            return;
        }
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        log.debug("校验参数：signature: {}, timestamp: {}, nonce: {}, echostr: {}", signature, timestamp, nonce, echostr);
        boolean checkRet = WxSignCheckUtil.checkSignature(signature, timestamp, nonce, WX_TOKEN);
        if(checkRet) {
            log.info("校验接入签名成功");
            ResponseUtil.printJson(response, echostr);
        } else {
            log.error("校验接入签名失败");
            ResponseUtil.printJson(response, "");
        }
    }

    //@GetMapping("/checksign")
    //public void checksign(HttpServletRequest request, HttpServletResponse response) {
    //    try {
    //        log.info("开始校验签名");
    //        String echostr = request.getParameter("echostr");// 随机字符串
    //        if (NullUtil.isNotNull(echostr)) {// 用于微信公众号设置url
    //            ResponseUtil.printJson(response, echostr);
    //            return;
    //        }
    //        String signature = request.getParameter("signature");
    //        String timestamp = request.getParameter("timestamp");// 时间戳
    //        String nonce = request.getParameter("nonce");// 随机数
    //        if (WxSignCheckUtil.checkSignature(signature, timestamp, nonce, WX_TOKEN)) {
    //            Map<String, String> msgMap = WxMessageUtil.xmlToMap(request);
    //            if ("aes".equals(request.getParameter("encrypt_type"))) {// aes加密的消息
    //                log.info("公众号推送密文消息：" + JSONObject.fromObject(msgMap).toString());
    //            } else {// 明文传输的消息
    //                log.info("公众号推送明文消息：" + JSONObject.fromObject(msgMap).toString());
    //                String openId = msgMap.get("FromUserName");
    //
    //                if (NullUtil.isNotNull(openId)) {
    //                    String msgType = msgMap.get("MsgType");// 微信推送的消息类型
    //                    if (msgType.equals("event")) {// 事件类型
    //                        if ("unsubscribe".equals(msgMap.get("Event"))) {// 取消关注
    //                            log.info("推送消息：取消关注：openId：" + openId);
    //                            // 从数据库获取
    //                            HashMap<String, Object> params = new HashMap<String, Object>();
    //                            params.put("openId", openId);
    //
    //                        } else if ("subscribe".equals(msgMap.get("Event"))) {// 关注
    //                            log.info("推送消息：关注：openId：" + openId);
    //                            // 从数据库获取
    //
    //                        } else if ("CLICK".equals(msgMap.get("Event"))) {// 点击菜单拉取消息时的事件推送
    //                            String eventKey = msgMap.get("EventKey");// 事件KEY值，与创建自定义菜单时指定的KEY值对应
    //                            if (eventKey.equals("HeMaOnline")) {
    //
    //                            }
    //                        }
    //                    } else if (msgType.equals("text")) {// 普通文本消息
    //                        String content = msgMap.get("Content");// 用户输入文本消息内容
    //                        log.info("用户输入信息：" + content);
    //                        StringBuffer sb = new StringBuffer();
    //                        String returnMsg = "";
    //
    //                        returnMsg = sb.toString();
    //                        // 自动回复
    //                        if (NullUtil.isNotNull(returnMsg)) {
    //                            String returnText = WxMessageUtil.returnText(msgMap, returnMsg);
    //                            if (NullUtil.isNotNull(returnText)) {
    //                                ResponseUtil.printJson(response, returnText);
    //                                return;
    //                            }
    //                        } else {
    //                            return;
    //                        }
    //                    }
    //                }
    //            }
    //            ResponseUtil.printJson(response, "");
    //            return;
    //        }
    //        log.info("公众号推送消息-签名验证错误");
    //        ResponseUtil.printJson(response, "签名错误");
    //        return;
    //    } catch (Exception e) {
    //        ResponseUtil.printJson(response, "error");
    //        log.info("公众号推送消息异常", e);
    //        return;
    //    }
    //
    //}
}
