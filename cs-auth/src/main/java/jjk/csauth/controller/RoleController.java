package jjk.csauth.controller;

import jjk.csauth.dao.AdminDao;
import jjk.csauth.dao.ResourceDao;
import jjk.csauth.dao.RoleDao;
import jjk.csauth.pojo.Admin;
import jjk.csauth.pojo.AdminRes;
import jjk.csauth.pojo.Resource;
import jjk.csauth.pojo.Role;
import jjk.csauth.service.ResourceService;
import jjk.csauth.service.RoleService;
import jjk.csauth.vo.RoleResourceId;
import jjk.csutils.pojo.ApiResult;
import jjk.csutils.pojo.ErrorResult;
import jjk.csutils.pojo.MyPage;
import jjk.csutils.pojo.SuccessResult;
import jjk.csutils.service.JsonSwitch;
import jjk.csutils.service.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author: jjk
 * @create: 2020-03-09 09:27
 * @program: cloudStudy
 * @description: 角色相关
 */
@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ResourceDao resourceDao;


    /**
     * 新增或修改role
     *
     * @param json
     * @return
     */
    @PostMapping("/saveRole")
    public ApiResult<String> saveRole(@RequestBody String json) {
        Role role = JsonSwitch.getJavaObj(json, Role.class);
        List<String> save = ObjectUtils.setArrayList("rname", "enable", "rsign");
        if (ObjectUtils.checkObjNotNull(role, (ArrayList<String>) save)) {
            List<Role> list = roleDao.findByRnameOrRsign(role.getRname(), role.getRsign());
            if (null != list && list.size() > 0) {
                return new ErrorResult<>("角色已存在");
            }
            roleDao.saveAndFlush(role);
            return new SuccessResult("保存成功");
        }
        return new ErrorResult<>("请求参数不能为空");
    }

    /**
     * 获取角色分页
     *
     * @param json
     * @return
     */
    @PostMapping("/pageRole")
    public ApiResult pageRole(@RequestBody String json) {
        MyPage<Role> prop = JsonSwitch.getPage(json, Role.class);
        MyPage<Role> roleMyPage = roleService.pageRole(prop);
        return new SuccessResult("请求成功", roleMyPage);
    }

    /**
     * 删除角色
     *
     * @param rid
     * @return
     */
    @GetMapping("/deleteRole/{rid}")
    public ApiResult deleteRole(@PathVariable Integer rid) {
        boolean admin = adminDao.exists(Example.of(new Admin().setRole(new Role().setRid(rid))));
        if (admin) {
            return new ErrorResult("角色正在被使用！！");
        }
        roleDao.deleteById(rid);
        return new SuccessResult("删除成功");
    }

    /**
     * 角色关联资源
     *
     * @param json
     * @return
     */
    @PostMapping("/saveRoleRes")
    public ApiResult saveRoleRes(@RequestBody String json) {
        RoleResourceId javaObj = JsonSwitch.getJavaObj(json, RoleResourceId.class);
        List<String> props = ObjectUtils.setArrayList("rid", "resId");
        if (ObjectUtils.checkObjNotNull(javaObj, (ArrayList<String>) props)) {
            Role role = roleDao.findById(javaObj.getRid()).orElse(null);
            if(null==role){
                return new ErrorResult<>("角色不存在！");
            }
            Set<Resource> updateRes=new HashSet<>();
            javaObj.getResId().forEach(r -> {
                updateRes.add(resourceDao.getOne(r));
            });
            role.setRlist(updateRes);
            roleDao.save(role);
            return new SuccessResult("保存成功");
        }
        return new ErrorResult("参数不能为空！！");
    }

    /**
     * 获取角色对应资源树
     *
     * @param rid
     * @return
     */
    @GetMapping("/findRoleResTree/{rid}")
    public ApiResult findRoleResTree(@PathVariable Integer rid) {
        Map<String, Object> result = new HashMap<>();
        Role role = roleDao.findById(rid).orElse(null);
        List<Resource> all = resourceDao.findAll();
        HashSet<Resource> resSet = new HashSet<>();
        all.forEach(res -> resSet.add(res));
        Set<AdminRes> tree = resourceService.getTree(resSet);
        result.put("tree", tree);
        if (null != role) {
            Set<Resource> rlist = role.getRlist();
            if (rlist != null && rlist.size() > 0) {
                List<Integer> rids = new ArrayList<>();
                rlist.forEach(res -> {
                    if (res.getLevel().intValue() == 2 && res.getType().contains("2")) {
                        rids.add(res.getId());
                    }
                });
                result.put("rids", rids);
                return new SuccessResult<>("请求成功", result);
            }
        }
        return new SuccessResult<>("请求成功", result);
    }
}
