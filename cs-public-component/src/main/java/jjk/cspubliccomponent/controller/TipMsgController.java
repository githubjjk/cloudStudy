package jjk.cspubliccomponent.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jjk.cspubliccomponent.pojo.TipMsg;
import jjk.cspubliccomponent.service.TipMsgService;
import jjk.csutils.pojo.ApiResult;
import jjk.csutils.pojo.ErrorResult;
import jjk.csutils.pojo.MyPage;
import jjk.csutils.pojo.SuccessResult;
import jjk.csutils.service.JsonSwitch;
import jjk.csutils.service.ObjectUtils;
import jjk.csutils.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jjk
 * @create 2020-07-24 21:15
 * @Describtion 消息提醒
 **/
@RestController
@Slf4j
@RequestMapping("/tm")
public class TipMsgController {
    @Autowired
    private TipMsgService tipMsgService;
    @Autowired
    private RedisService redisService;

    /**
     * 获取提醒消息列表
     *
     * @param json
     * @param AccessToken
     * @return
     */
    @PostMapping("/findTipMsg")
    public ApiResult findNoReadMsgToDay(@RequestBody String json, @RequestHeader("AccessToken") String AccessToken) {
        MyPage<TipMsg> myPage = JsonSwitch.getPage(json, TipMsg.class);
        Page<TipMsg> tipMsgPage = tipMsgService.findTMByTimeAndSignPage(myPage, AccessToken);
        return new SuccessResult(tipMsgPage);
    }

    /**
     * 保存消息
     *
     * @param json
     * @return
     */
    @PostMapping("/saveTipMsg")
    public ApiResult saveTipMsg(@RequestBody String json, @RequestHeader("AccessToken") String AccessToken) {
        TipMsg tipMsg = JsonSwitch.getJavaObj(json, TipMsg.class);
        if (null != tipMsg.getId()) {
            //更新
            tipMsg.updateById();
            return new SuccessResult("保存成功");
        } else {
            //新增
            List<String> prop = ObjectUtils.setArrayList("msgSign", "msg");
            if (ObjectUtils.checkObjNotNull(tipMsg, (ArrayList<String>) prop)) {
                String adminStr = redisService.getVal(AccessToken);
                int aid = Integer.parseInt(JsonSwitch.getJsonByKey(adminStr, json));
                tipMsg.setAid(aid);
                tipMsg.insert();
                return new SuccessResult("保存成功");
            }
        }
        return new ErrorResult("请求参数不能为空");
    }
}
