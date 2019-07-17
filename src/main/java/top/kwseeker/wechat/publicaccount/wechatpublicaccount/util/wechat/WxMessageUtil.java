package top.kwseeker.wechat.publicaccount.wechatpublicaccount.util.wechat;

import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.util.validate.NullUtil;
import top.kwseeker.wechat.publicaccount.wechatpublicaccount.util.TextMessage;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class WxMessageUtil {
    // 各种消息类型,除了扫带二维码事件
    /**
     * 文本消息
     */
    private static final String MESSAGE_TEXT = "text";
    /**
     * 图片消息
     */
    private static final String MESSAtGE_IMAGE = "image";
    /**
     * 图文消息
     */
    private static final String MESSAGE_NEWS = "news";
    /**
     * 语音消息
     */
    private static final String MESSAGE_VOICE = "voice";
    /**
     * 视频消息
     */
    private static final String MESSAGE_VIDEO = "video";
    /**
     * 小视频消息
     */
    private static final String MESSAGE_SHORTVIDEO = "shortvideo";
    /**
     * 地理位置消息
     */
    private static final String MESSAGE_LOCATION = "location";
    /**
     * 链接消息
     */
    private static final String MESSAGE_LINK = "link";
    /**
     * 事件推送消息
     */
    private static final String MESSAGE_EVENT = "event";
    /**
     * 事件推送消息中,事件类型，subscribe(订阅)
     */
    private static final String MESSAGE_EVENT_SUBSCRIBE = "subscribe";
    /**
     * 事件推送消息中,事件类型，unsubscribe(取消订阅)
     */
    private static final String MESSAGE_EVENT_UNSUBSCRIBE = "unsubscribe";
    /**
     * 事件推送消息中,上报地理位置事件
     */
    private static final String MESSAGE_EVENT_LOCATION_UP = "LOCATION";
    /**
     * 事件推送消息中,自定义菜单事件,点击菜单拉取消息时的事件推送
     */
    private static final String MESSAGE_EVENT_CLICK = "CLICK";
    /**
     * 事件推送消息中,自定义菜单事件,点击菜单跳转链接时的事件推送
     */
    private static final String MESSAGE_EVENT_VIEW = "VIEW";

    /**
     * 将xml转化为Map集合
     *
     * @param request
     * @return
     * @throws DocumentException
     */
    public static Map<String, String> xmlToMap(HttpServletRequest request){
        Map<String, String> map = new HashMap<String, String>();
        SAXReader reader = new SAXReader();
        try (InputStream ins = request.getInputStream();){
            Document doc = reader.read(ins);
            Element root = doc.getRootElement();
            @SuppressWarnings("unchecked")
            List<Element> list = root.elements();
            for (Element e : list) {
                map.put(e.getName(), e.getText());
            }
            root.clearContent();
        } catch (Exception e) {
            log.error(e.getMessage());
            System.out.println(e.getStackTrace());
        }
        return map;
    }

    /**
     * 文本消息转化为xml
     * @param textMessage
     * @return
     */
    public static String objectToXml(Object object) {
        XStream xstream = new XStream();
        xstream.alias("xml", object.getClass());
        return xstream.toXML(object);
    }

    /**
     * xml转化为对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T xmlToObject(String xml, Class<T> t) {
        try {
            SOAPMessage message = MessageFactory.newInstance().createMessage(null,
                    new ByteArrayInputStream(xml.getBytes()));
            Unmarshaller unmarshaller = JAXBContext.newInstance(t).createUnmarshaller();
            T ts = (T)unmarshaller.unmarshal(message.getSOAPBody().extractContentAsDocument());
            return ts;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 返回消息
     *@author chencaihui
     *@datetime 创建时间：2017年6月5日 下午2:45:52
     * @param request
     * @return
     */
    public static String processRequest(HttpServletRequest request) {
		/*<xml><ToUserName><![CDATA[gh_0545f33f1d31]]></ToUserName>
			<FromUserName><![CDATA[ovyapxIg28GZ53eOCzTQHl-nUsjA]]></FromUserName>
			<CreateTime>1496635472</CreateTime>
			<MsgType><![CDATA[event]]></MsgType>
			<Event><![CDATA[subscribe]]></Event>
			<EventKey><![CDATA[qrscene_18819499920]]></EventKey>
			<Ticket><![CDATA[gQH-8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyQkVFLTkzX0NlRjQxMDAwMHcwN1oAAgRkLTFZAwQAAAAA]]></Ticket>
			</xml>*/
        // 默认回复一个"success"
        String responseMessage = "success";
        try {
            Map<String, String> result = WxMessageUtil.xmlToMap(request);
            System.out.println(result.toString());
            TextMessage tMessage = new TextMessage();
            tMessage.setFromUserName(result.get("ToUserName"));
            tMessage.setToUserName(result.get("FromUserName"));
            tMessage.setCreateTime(System.currentTimeMillis());
            if(result.containsKey("EventKey")){
                tMessage.setContent(result.get("EventKey"));
            }else{
                tMessage.setContent("您好");
            }
            tMessage.setMsgType(MESSAGE_TEXT);
            responseMessage = WxMessageUtil.objectToXml(tMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return responseMessage;
    }

    /**
     * 把回复消息封装为XML返回
     *@author chencaihui
     *@datetime 创建时间：2018年2月7日 下午10:18:25
     * @param fromMsg
     * @param returnMsg
     * @return XML
     */
    public static String returnText(Map<String, String> fromMsg, String returnMsg) {
        try {
            TextMessage tMessage = new TextMessage();
            tMessage.setFromUserName(fromMsg.get("ToUserName"));
            tMessage.setToUserName(fromMsg.get("FromUserName"));
            tMessage.setCreateTime(System.currentTimeMillis());
            tMessage.setContent(NullUtil.isNull(returnMsg)?fromMsg.get("Content"):returnMsg);
            tMessage.setMsgType(MESSAGE_TEXT);
            return WxMessageUtil.objectToXml(tMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
