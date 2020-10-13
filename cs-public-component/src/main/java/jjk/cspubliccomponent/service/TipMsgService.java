package jjk.cspubliccomponent.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jjk.cspubliccomponent.mapper.TipMsgMapper;
import jjk.cspubliccomponent.pojo.TipMsg;
import jjk.csutils.pojo.MyPage;
import jjk.csutils.service.JsonSwitch;
import jjk.csutils.service.RedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jjk
 * @create 2020-07-24 21:40
 * @Describtion 消息提醒service
 **/
@Service
public class TipMsgService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private TipMsgMapper tipMsgMapper;

    /**
     * 查询消息分页
     *
     * @param myPage
     * @param accessToken
     * @return
     */
    public Page<TipMsg> findTMByTimeAndSignPage(MyPage<TipMsg> myPage, String accessToken) {
        Page<TipMsg> page = new Page<>();
        TipMsg tipMsg = myPage.getParam();
        QueryWrapper<TipMsg> tq = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(tipMsg.getMsgSign())) {
            tq.eq("msg_sign", tipMsg.getMsgSign());
        }
        if (null != tipMsg.getActionTime() && null != tipMsg.getEndTime()) {
            tq.between("optime", tipMsg.getActionTime(), tipMsg.getEndTime());
        }
        if (null != tipMsg.getActionTime() && null == tipMsg.getEndTime()) {
            tq.ge("optime", tipMsg.getActionTime());
        }
        if (null == tipMsg.getActionTime() && null != tipMsg.getEndTime()) {
            tq.le("optime", tipMsg.getEndTime());
        }
        //确定分页
        //确定查自己还是查全部
        page.setCurrent(myPage.getCurrPage());
        page.setSize(myPage.getPageSize());
        if (tipMsg.getCheckAll().equals("0")) {
            String adminStr = redisService.getVal(accessToken);
            int id = Integer.parseInt(JsonSwitch.getJsonByKey(adminStr, "id"));
            tq.eq("aid", id);
        }
        Page<TipMsg> tipMsgPage = tipMsgMapper.selectPage(page, tq);
        return tipMsgPage;
    }
}
