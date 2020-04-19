package jjk.csauth.controller;

import com.alibaba.fastjson.JSONObject;
import jjk.csauth.dao.AdminDao;
import jjk.csauth.dao.RoleDao;
import jjk.csauth.pojo.Admin;
import jjk.csauth.pojo.AdminRes;
import jjk.csauth.pojo.Role;
import jjk.csauth.service.AdminService;
import jjk.csauth.vo.AdminMsg;
import jjk.csauth.vo.RoleAdmins;
import jjk.csutils.pojo.ApiResult;
import jjk.csutils.pojo.ErrorResult;
import jjk.csutils.pojo.MyPage;
import jjk.csutils.pojo.SuccessResult;
import jjk.csutils.service.JsonSwitch;
import jjk.csutils.service.ObjectUtils;
import jjk.csutils.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author: jjk
 * @create: 2020-03-06 14:18
 * @program: cloudStudy
 * @description: 用户管理
 */
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private AdminService adminService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RoleDao roleDao;

    /**
     * 保存修改用户
     *
     * @param json
     * @return
     */
    @PostMapping("/saveAdmin")
    public ApiResult saveAdmin(@RequestBody String json) {
        Admin admin = JsonSwitch.getJavaObj(json, Admin.class);
        List<String> prop = ObjectUtils.setArrayList("username", "phone", "name");
        if (ObjectUtils.checkObjNotNull(admin, (ArrayList<String>) prop)) {
            try {
                String password = "";
                boolean adminExist = adminDao.exists(Example.of(new Admin().setUsername(admin.getUsername())));
                if (adminExist) {
                    return new ErrorResult("用户名重复");
                }
                if (null != admin.getPassword() && !admin.equals("")) {
                    //新增
                    password = Base64Utils.encodeToString((admin.getUsername() + "123456").getBytes("UTF-8"));
                    admin.setPassword(password);
                    adminDao.save(admin);
                    return new SuccessResult("保存成功！！");
                }
                password = Base64Utils.encodeToString((admin.getUsername() + admin.getPassword()).getBytes("UTF-8"));
                admin.setPassword(password);
                adminDao.save(admin);
                return new SuccessResult("保存成功！！");
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage());
            }
        }
        return new ErrorResult("请求参数不能为空");
    }

    /**
     * 获取用户列表
     *
     * @param json
     * @return
     */
    @PostMapping("/pageAdmin")
    public ApiResult pageAdmin(@RequestBody String json) {
        MyPage<Admin> page = JsonSwitch.getPage(json, Admin.class);
        page = adminService.pageAdmin(page);
        return new SuccessResult("请求成功", page);
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteAdmin/{id}")
    public ApiResult deleteAdmin(@PathVariable Integer id) {
        if (id.intValue() == 1) {
            return new ErrorResult("不能删除超级管理员！");
        }
        adminDao.deleteById(id);
        return new SuccessResult("删除成功");
    }

    /**
     * 根据用户获取资源树
     *
     * @param json
     * @return
     */
    @PostMapping("/findResByAdmin")
    public ApiResult findResByAdmin(@RequestBody String json) {
        Object key = JsonSwitch.getJsonByKey(json, "token");
        if (null != key) {
            Map<String, Object> map = new HashMap<>();
            String val = redisService.getVal(key.toString());
            Admin admin = JSONObject.parseObject(val, Admin.class);
            Set<AdminRes> resTree = adminService.getResTree(admin.getRole().getRlist());
            List<String> perBtn = adminService.getPerBtn(admin.getRole().getRlist());
            map.put("resList", resTree);
            map.put("perBtn", perBtn);
            return new SuccessResult<>(map);
        }
        return new ErrorResult<>("参数不能为空");
    }

    /**
     * 根据用户获角色信息
     *
     * @param json
     * @return
     */
    @PostMapping("/findAdminRole")
    public ApiResult findAdminRole(@RequestBody String json) {
        Object key = JsonSwitch.getJsonByKey(json, "token");
        if (null != key) {
            String val = redisService.getVal(key.toString());
            Admin admin = JSONObject.parseObject(val, Admin.class);
            if (null != admin.getRole()) {
                HashSet<String> roles = new HashSet<>();
                roles.add(admin.getRole().getRname());
                AdminMsg adminMsg = new AdminMsg().setAvatar(admin.getAvatar())
                        .setIntroduction("")
                        .setName(admin.getName())
                        .setRoles(roles);
                return new SuccessResult<>(adminMsg);
            }
            return new SuccessResult<>("请求成功");
        }
        return new ErrorResult<>("参数不能为空");
    }

    /**
     * 人员关联角色
     *
     * @param json
     * @return
     */
    @PostMapping("/AdminLinkRole")
    public ApiResult AdminLinkRole(@RequestBody String json) {
        RoleAdmins roleAdmins = JsonSwitch.getJavaObj(json, RoleAdmins.class);
        List<String> strings = ObjectUtils.setArrayList("rid", "aids");
        if (ObjectUtils.checkObjNotNull(roleAdmins, (ArrayList<String>) strings)) {
            List<Integer> aids = roleAdmins.getAids();
            Role role = roleDao.findById(roleAdmins.getRid()).orElse(null);
            if (null != role) {
                Set<Admin> admins = role.getAdmins();
                if (null != admins && admins.size() > 0) {
                    for (Admin admin : admins) {
                        admin.setRole(null);
                        adminDao.save(admin);
                    }
                }
                for (int aid : aids) {
                    Admin admin2 = adminDao.findById(aid).orElse(null);
                    if (null != admin2) {
                        admin2.setRole(role);
                        adminDao.save(admin2);
                    }
                }
                return new SuccessResult<>("关联成功");
            }
            return new ErrorResult<>("参数有误");
        }
        return new ErrorResult("参数不能为空！！");
    }
}
