package jjk.csauth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jjk.csauth.dao.AdminMapper;
import jjk.csauth.dao.ResourceMapper;
import jjk.csauth.dao.RoleMapper;
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
    private RoleMapper roleMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ResourceMapper resourceMapper;


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
            QueryWrapper<Role> rq = new QueryWrapper<>();
            rq.eq("rname", role.getRname());
            rq.eq("enable", "0");
            Integer count = roleMapper.selectCount(rq);
            if (count > 0) {
                return new ErrorResult<>("角色已存在");
            }
            role.insertOrUpdate();
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
        QueryWrapper<Admin> aq = new QueryWrapper<>();
        aq.eq("rid", rid);
        Integer count = adminMapper.selectCount(aq);
        if (null != count && count > 0) {
            return new ErrorResult("角色正在被使用！！");
        }
        roleMapper.deleteById(rid);
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
            Role role = roleMapper.selectById(javaObj.getRid());
            if (null == role) {
                return new ErrorResult<>("角色不存在！");
            }
            roleMapper.deleteRoleRes(javaObj.getRid());
            roleMapper.insertRoleAndRes(javaObj.getRid(), javaObj.getResId());
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
        Role role = roleMapper.findRoleById(rid);
        List<Resource> all = resourceMapper.selectList(null);
        HashSet<Resource> resSet = new HashSet<>();
        all.forEach(res -> resSet.add(res));
        Set<AdminRes> tree = resourceService.getTree(resSet, "1");
        result.put("tree", tree);
        if (null != role) {
            Set<Resource> rlist = role.getRlist();
            if (rlist != null && rlist.size() > 0) {
                List<Integer> btns = new ArrayList<>();
                List<Resource> pathList = new ArrayList<>();
                List<Resource> btnList = new ArrayList<>();
                for (Resource r : rlist) {
                    if (r.getType().equals("2")) {
                        btns.add(r.getId());
                        btnList.add(r);
                    }
                    if (r.getType().equals("1")) {
                        pathList.add(r);
                    }
                }
                List<Integer> btns2 = new ArrayList<>();
                for (Resource route : pathList) {
                    boolean sign = false;
                    for (Resource btn : btnList) {
                        if (route.getId().intValue() == btn.getParentId().intValue()) {
                            sign = false;
                            break;
                        } else {
                            sign = true;
                        }
                    }
                    if (sign) {
                        btns2.add(route.getId());
                    }
                }
                btns.addAll(btns2);
                result.put("rids", btns);
                return new SuccessResult<>("请求成功", result);
            }
        }
        return new SuccessResult<>("请求成功", result);
    }
}
