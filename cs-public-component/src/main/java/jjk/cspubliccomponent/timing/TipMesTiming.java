package jjk.cspubliccomponent.timing;

import jjk.cspubliccomponent.constant.PublicConstant;
import jjk.cspubliccomponent.mapper.TipMsgMapper;
import jjk.cspubliccomponent.pojo.TipMsg;
import jjk.cspubliccomponent.pojo.WsHeart;
import jjk.cspubliccomponent.service.AdminOpenFeign;
import jjk.cspubliccomponent.webSocket.MyWebSocket;
import jjk.csutils.pojo.ApiResult;
import jjk.csutils.service.JsonSwitch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jjk
 * @create 2020-07-06 18:16
 * @Describtion 提醒消息定时任务
 **/
@EnableScheduling
@Slf4j
@Component
public class TipMesTiming {
    @Autowired
    private MyWebSocket myWebSocket;
    @Autowired
    private AdminOpenFeign adminOpenFeign;
    @Autowired
    private TipMsgMapper tipMsgMapper;


    @Scheduled(cron = "0 * * * * *")
    private void sendTipTiming() {
        if (MyWebSocket.sessionMap.keySet().size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, String> userMsg = new HashMap<>();
            for (String key : MyWebSocket.sessionMap.keySet()) {
                Map<String, String> msg = new HashMap<>();
                msg.put("time", sdf.format(new Date()));
                String jsonString = JsonSwitch.getJsonString(msg);
                TipMsg tipMsg = new TipMsg();
                tipMsg.setMsgSign("0").setMsg(jsonString);
                String tipJson = JsonSwitch.getJsonString(tipMsg);
                WsHeart<String> heart = new WsHeart<>();
                heart.setHeartSign("1");
                heart.setMsgObj(tipJson);
                String heartMsg = JsonSwitch.getJsonString(heart);
                userMsg.put(key, jsonString);
                myWebSocket.sendMessage(heartMsg, MyWebSocket.sessionMap.get(key));
            }
            for (String username : userMsg.keySet()) {
                ApiResult result = adminOpenFeign.findAdminByUserName(username);
                if (StringUtils.isNotEmpty(result.getData().toString())) {
                    String id = JsonSwitch.getJsonByKey(JsonSwitch.getJsonString(result.getData()), "id");
                    TipMsg tipMsg = new TipMsg();
                    tipMsg.setMsg(userMsg.get(username));
                    tipMsg.setMsgSign("0");
                    tipMsg.setAid(Integer.parseInt(id));
                    tipMsgMapper.insert(tipMsg);
                }
            }
        }
    }
}
