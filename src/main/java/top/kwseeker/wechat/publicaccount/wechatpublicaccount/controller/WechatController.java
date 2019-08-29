package top.kwseeker.wechat.publicaccount.wechatpublicaccount.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.util.validate.NullUtil;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.util.ResponseUtil;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.util.wechat.WxMessageUtil;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.util.wechat.WxSignCheckUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

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
    @GetMapping("/wxmessage")
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

    //微信消息处理
    @PostMapping("/wxmessage")
    public void messageHandle(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, String> map = WxMessageUtil.xmlToMap(request);
            String msgType = map.get("MsgType");
            response.setCharacterEncoding("UTF-8");
            PrintWriter printWriter = response.getWriter();
            //判断请求类型
            if("event".equals(msgType)) {           //事件
                String eventType = map.get("Event");
                if("subscribe".equals(eventType)) {     //订阅事件
                    String replyMessage = replyMessage();
                    String reply = WxMessageUtil.returnText(map, replyMessage);
                    log.info("发送新用户订阅回复消息");
                    printWriter.println(reply);
                }
            }
        } catch (IOException e) {
            log.error("出现异常：e={}", e.getMessage());
            e.printStackTrace();
        }
    }

    private String replyMessage() {
        StringBuffer sb = new StringBuffer();
        sb.append("Hi，你来啦！\n\n");
        sb.append("贴上防丢贴纸。东西丢了，扫码找回。\n\n");
        sb.append(">>><a href=\"https://mashu.51hutui.com/#/message/1566292887997kXo6d/freshmen\">点我抽取开学签</a> 还有机会中电影票哦~\n\n");
        sb.append("在这里，你还可以：\n");
        sb.append(">>><a href=\" https://mashu.51hutui.com/index.html#/home\">管理贴纸上的二维码</a>\n\n");
        return sb.toString();
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
