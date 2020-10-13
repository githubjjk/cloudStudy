package jjk.csauth.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jjk.csauth.dao.AdminMapper;
import jjk.csauth.dao.RoleMapper;
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
    private AdminMapper adminMapper;
    @Autowired
    private AdminService adminService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RoleMapper roleMapper;

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
                QueryWrapper<Admin> qw = new QueryWrapper<>();
                qw.eq("username", admin.getUsername());
                qw.eq("enable", "0");
                Integer adminExist = adminMapper.selectCount(qw);
                if (adminExist > 0) {
                    return new ErrorResult("用户名重复");
                }
                if (null == admin.getId()) {
                    if (null != admin.getPassword()) {
                        //新增
                        password = Base64Utils.encodeToString((admin.getUsername() + "123456").getBytes("UTF-8"));
                    } else {
                        password = Base64Utils.encodeToString((admin.getUsername() + admin.getPassword()).getBytes("UTF-8"));
                    }
                    admin.setPassword(password);
                }
                admin.insertOrUpdate();
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
        adminMapper.deleteById(id);
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
            Set<AdminRes> resTree = adminService.getNavTree(admin.getRole().getRlist());
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
            Role role = roleMapper.selectById(roleAdmins.getRid());
            if (null != role) {
                for (int i = 0; i < aids.size(); i++) {
                    Admin admin = new Admin().setId(aids.get(i)).setRid(role.getRid());
                    admin.updateById();
                }
                return new SuccessResult<>("关联成功");
            }
            return new ErrorResult<>("参数有误");
        }
        return new ErrorResult("参数不能为空！！");
    }

    /**
     * 根据username查找用户
     *
     * @param username
     * @return
     */
    @GetMapping("/findAdminByUserName/{username}")
    public ApiResult findAdminById(@PathVariable("username") String username) {
        QueryWrapper<Admin> aq = new QueryWrapper<>();
        aq.eq("username", username);
        Admin admin = adminMapper.selectOne(aq);
        return new SuccessResult<>(admin);
    }
}
