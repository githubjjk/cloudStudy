package jjk.csauth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import jjk.csauth.pojo.Admin;
import jjk.csutils.pojo.ApiResult;
import jjk.csutils.pojo.ErrorResult;
import jjk.csutils.pojo.SuccessResult;
import jjk.csutils.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Base64.Encoder;

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
    private Gson gson;

    @Autowired
    private RedisService redisService;


    @RequestMapping("/login")
    public ApiResult login(@RequestBody String json){
        if(StringUtils.isNotEmpty(json)){
            Admin admin = gson.fromJson(json, Admin.class);
            if(null!=admin){
                Admin nowAdmin = admin.selectOne(new QueryWrapper<Admin>().eq("a_username", admin.getAUsername()).eq("a_password", admin.getAPassword()));
                String token="";
                if(null!=nowAdmin){
                    try {
                        token = Base64Utils.encodeToString((nowAdmin.getAUsername() + nowAdmin.getAPassword()).getBytes("UTF-8"));
                        redisService.setKeyValTime(token,token,60*30*1000);
                        return new SuccessResult("登录成功",new Admin().setToken(token));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return new ErrorResult("用户名密码错误");
            }
        }
        return new ErrorResult("请求参数不能为空");
    }
}
