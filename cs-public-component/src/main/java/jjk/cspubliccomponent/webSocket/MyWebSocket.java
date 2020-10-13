package jjk.cspubliccomponent.webSocket;


import jjk.cspubliccomponent.constant.PublicConstant;
import jjk.cspubliccomponent.pojo.WsHeart;
import jjk.csutils.service.JsonSwitch;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: jjk
 * @create: 2019-06-03 16:59
 * @program: shirojjk01
 * @description: webSocket测试
 */
@Component
@Slf4j
@ServerEndpoint("/myWebSocket/{username}")
@Data
public class MyWebSocket {
    //用来统计客户连接数
    public static int onLineManNumber;

    //用来存放所有客户的集合
    public static CopyOnWriteArrayList<MyWebSocket> onlineMans = new CopyOnWriteArrayList<>();

    //用来存放session的容器
    public static Map<String, Session> sessionMap = new HashMap<>();

    //webSocket的session用于与某个客户段联络单发
    private Session session;

    //在线人数增加
    public static synchronized void onlineManNumberAdd() {
        MyWebSocket.onLineManNumber++;
    }

    //在线人数减少
    public static synchronized void onlineManNumberReduce() {
        MyWebSocket.onLineManNumber--;
    }

    //获取在线人数
    public static synchronized int getOnlineManCount() {
        return MyWebSocket.onLineManNumber;
    }

    @OnOpen
    public void open(Session session, @PathParam("username") String username) {
        this.session = session;
        WsHeart wsHeart = new WsHeart();
        wsHeart.setHeartSign(PublicConstant.WS_HEART_SIGN);
        wsHeart.setMsgObj(PublicConstant.WS_HEART_S_PAC);
        String jsonString = JsonSwitch.getJsonString(wsHeart);
        if (null != sessionMap.get(username)) {
            //说明有此用户不需要在添加更新一下
            sessionMap.remove(username);
        } else {
            //广播容器
            onlineManNumberAdd();
        }
        //session容器
        if(this.session.isOpen()) {
            sessionMap.put(username, this.session);
            sendMessage(jsonString, this.session);
        }
        log.info("当前在线人数" + getOnlineManCount());
    }

    @OnClose
    public void close() {
        if (null != sessionMap && sessionMap.keySet().size() > 0) {
            String delSessionKey = null;
            for (String key : sessionMap.keySet()) {
                Session session = sessionMap.get(key);
                if (null != session && session.equals(this.session)) {
                    delSessionKey = key;
                }
            }
            if (null != delSessionKey) {
                try {
                    //关闭练接
                    Session session = sessionMap.get(delSessionKey);
                    session.close();
                    //删除练接记录
                    sessionMap.remove(delSessionKey);
                    onlineManNumberReduce();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            log.info("关闭了一个链接，还有" + getOnlineManCount());
        }
    }

    @OnMessage
    public void onMessage(String mes) {
        WsHeart<String> heart = JsonSwitch.getJavaObj(mes, WsHeart.class);
        log.info("收到客户端消息：{}", heart);
        if (null != heart) {
            if (heart.getHeartSign().equals(PublicConstant.WS_HEART_SIGN) && heart.getMsgObj().equals(PublicConstant.WS_HEART_C_PAC)) {
                //发送
                WsHeart<String> stringWsHeart = new WsHeart<>();
                stringWsHeart.setHeartSign(PublicConstant.WS_HEART_SIGN);
                stringWsHeart.setMsgObj(PublicConstant.WS_HEART_S_PAC);
                String jsonString = JsonSwitch.getJsonString(stringWsHeart);
                if(this.session.isOpen()){
                    sendMessage(jsonString, this.session);
                }
            } else if (heart.getHeartSign().equals(PublicConstant.WS_MSG_SIGN)) {
                //数据解析
                log.info("通信数据：{}", heart.getMsgObj());
            }
        }
    }


    //发送信息
    public void sendMessage(String mes, Session session) {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(mes);
            }
        } catch (IOException e) {
            log.error("发送消息异常：", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    /**
     * 向所有用户推送
     *
     * @param msg
     */
    public void sendAllMessage(String msg) {
        for (String key : sessionMap.keySet()) {
            try {
                sessionMap.get(key).getBasicRemote().sendText(msg);
            } catch (IOException e) {
                log.error("发送消息异常：", e.getLocalizedMessage());
            }
        }
    }
}
