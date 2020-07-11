package jjk.csauth.controller;

import com.alibaba.fastjson.JSONObject;
import jjk.csauth.dao.AdminMapper;
import jjk.csauth.pojo.Admin;
import jjk.csutils.pojo.ApiResult;
import jjk.csutils.pojo.ErrorResult;
import jjk.csutils.pojo.SuccessResult;
import jjk.csutils.service.JsonSwitch;
import jjk.csutils.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * @author: jjk
 * @create: 2020-01-06 16:44
 * @program: cloudStudy
 * @description: 登录鉴权相关
 */
@RestController
@RequestMapping("/")
@Slf4j
public class LoginController {

    @Autowired
    private RedisService redisService;
    @Autowired
    private AdminMapper adminMapper;

    /**
     * 登录
     *
     * @param json
     * @return
     */
    @PostMapping("/login")
    public ApiResult<String> login(@RequestBody String json) {
        Admin admin = JsonSwitch.getJavaObj(json, Admin.class);
        if (null != admin) {
            Admin nowAdmin = adminMapper.findAdminByUP(admin.getUsername(), admin.getPassword());
            String token = "";
            if (null != nowAdmin) {
                if (null == nowAdmin.getRole()) {
                    return new ErrorResult<>("未授权,请联系管理员！");
                }
                try {
                    token = Base64Utils.encodeToString((nowAdmin.getUsername() + nowAdmin.getPassword() + Math.random()).getBytes("UTF-8"));
                    redisService.setKeyValTime(token, JSONObject.toJSONString(nowAdmin), 1800);
                    return new SuccessResult("请求成功", token);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return new ErrorResult("用户名密码错误");
                }
            }
            return new ErrorResult("用户名密码错误");
        }
        return new SuccessResult("请求参数不能为空");
    }

    /**
     * 退出系统
     *
     * @param json
     * @return
     */
    @PostMapping("/loginOut")
    public ApiResult<String> loginOut(@RequestBody String json) {
        Object token = JsonSwitch.getJsonByKey(json, "token");
        if (null != token) {
            redisService.removeVal(token.toString());
            return new SuccessResult<>("退出成功");
        }
        return new ErrorResult<>("参数不能为空");
    }
}
