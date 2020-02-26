package jjk.csauth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import jjk.csauth.pojo.Admin;
import jjk.csutils.pojo.SuccessResult;
import jjk.csutils.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: jjk
 * @create: 2020-01-06 16:44
 * @program: cloudStudy
 * @description: 登录鉴权相关
 */
@RestController
@RequestMapping("")
@Slf4j
public class LoginController {
    @Autowired
    private Gson gson;
    @Autowired
    private RedisService redisService;


    @RequestMapping("/login")
    public String login(@RequestBody String json){
        redisService.getVal("jjk");
        if(StringUtils.isNotEmpty(json)){
            Admin admin = gson.fromJson(json, Admin.class);
            if(null!=admin){
                Admin nowAdmin = admin.selectOne(new QueryWrapper<Admin>().eq("aid", admin.getAid()));
                return gson.toJson(new SuccessResult<Admin>(nowAdmin));
            }
        }
        return gson.toJson(new SuccessResult("参数为空"));
    }
}
