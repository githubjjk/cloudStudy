package jjk.csauth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jjk.csauth.dao.ResourceMapper;
import jjk.csauth.dao.RoleMapper;
import jjk.csauth.pojo.AdminRes;
import jjk.csauth.pojo.Resource;
import jjk.csauth.service.AdminService;
import jjk.csauth.service.ResourceService;
import jjk.csutils.pojo.ApiResult;
import jjk.csutils.pojo.ErrorResult;
import jjk.csutils.pojo.SuccessResult;
import jjk.csutils.service.JsonSwitch;
import jjk.csutils.service.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: jjk
 * @create: 2020-03-09 14:17
 * @program: cloudStudy
 * @description: 资源相关
 */
@RestController
@RequestMapping("/res")
@Slf4j
public class ResourceController {
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleMapper roleMapper;


    /**
     * 保存资源
     *
     * @param json
     * @return
     */
    @PostMapping("/saveRes")
    public ApiResult saveResource(@RequestBody String json) {
        Resource res = JsonSwitch.getJavaObj(json, Resource.class);
        List<String> prop = ObjectUtils.setArrayList("name", "parentId", "enable", "type", "icon", "path");
        if (ObjectUtils.checkObjNotNull(res, (ArrayList<String>) prop)) {
            QueryWrapper<Resource> rq = new QueryWrapper<>();
            rq.eq("parent_id", res.getParentId());
            rq.eq("name", res.getName());
            rq.eq("enable", "0");
            Integer exists = resourceMapper.selectCount(rq);
            if (exists > 0) {
                return new ErrorResult<>("资源已存在");
            }
            res.setLevel(resourceService.resLevel(res.getParentId()));
            AdminRes adminRes = new AdminRes();
            if (null != res.getId()) {
                //更新
                res.setTreePath(resourceService.treePath(res.getParentId(), res.getId()));
                resourceMapper.updateById(res);
                adminRes.setId(res.getId())
                        .setPath(res.getPath())
                        .setLable(res.getName())
                        .setType(res.getType())
                        .setIcon(res.getIcon());
                return new SuccessResult<>("修改成功", adminRes);
            }
            //新增
            resourceMapper.insert(res);
            res.setTreePath(resourceService.treePath(res.getParentId(), res.getId()));
            adminRes.setId(res.getId())
                    .setPath(res.getPath())
                    .setLable(res.getName())
                    .setType(res.getType())
                    .setIcon(res.getIcon());
            return new SuccessResult<>("保存成功", adminRes);
        }
        return new ErrorResult<>("请求参数不能为空");
    }

    /**
     * 删除资源
     *
     * @param rid
     * @return
     */
    @GetMapping("/deleteRes/{rid}")
    public ApiResult<String> deleteResource(@PathVariable Integer rid) {
        int count = resourceMapper.findResourceRelationById(rid);
        if (count > 0) {
            return new ErrorResult<>("资源正在被使用");
        }
        resourceMapper.deleteById(rid);
        return new SuccessResult("删除成功");
    }

    /**
     * 获取资源树
     *
     * @return
     */
    @GetMapping("/findResTree")
    public ApiResult findResTree() {
        QueryWrapper<Resource> rq = new QueryWrapper<>();
        rq.orderByAsc("id");
        List<Resource> all = resourceMapper.selectList(rq);
        Set<Resource> adminResSet = new HashSet<>();
        all.forEach(r -> adminResSet.add(r));
        AdminRes adminRes = new AdminRes();
        adminRes.setId(-1)
                .setLable("菜单列表")
                .setType("-1")
                .setIcon("fa-icon")
                .setTreePath("-1");
        Set<AdminRes> resTree = adminService.getResTree(adminResSet);
        adminRes.setChild(resTree);
        Set<AdminRes> one = new HashSet<>();
        one.add(adminRes);
        return new SuccessResult<>("请求成功", one);
    }

    /**
     * 根据父id获取子集菜单
     *
     * @return
     */
    @GetMapping("/findNextRes/{parentId}")
    public ApiResult findNextRes(@PathVariable Integer parentId) {
        QueryWrapper<Resource> rq = new QueryWrapper<>();
        rq.eq("parent_id", parentId);
        List<Resource> all = resourceMapper.selectList(rq);
        if (null != all && all.size() > 0) {
            ArrayList<AdminRes> adminRes = new ArrayList<>();
            for (int i = 0; i < all.size(); i++) {
                AdminRes as = new AdminRes();
                as.setId(all.get(i).getId())
                        .setLable(all.get(i).getName())
                        .setIcon(all.get(i).getIcon())
                        .setPath(all.get(i).getPath())
                        .setType(all.get(i).getType())
                        .setTreePath(all.get(i).getTreePath());
                as.setParentId(all.get(i).getParentId());
                adminRes.add(as);
            }
            return new SuccessResult<>("请求成功", adminRes);
        }
        return new SuccessResult<>("请求成功");
    }
}
