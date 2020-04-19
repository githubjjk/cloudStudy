package jjk.csauth.controller;

import jjk.csauth.dao.ResourceDao;
import jjk.csauth.dao.RoleDao;
import jjk.csauth.pojo.Admin;
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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
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
    private ResourceDao resourceDao;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleDao roleDao;


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
            boolean exists = resourceDao.exists(Example.of(res));
            if (exists) {
                return new ErrorResult<>("资源已存在");
            }
            res.setLevel(resourceService.resLevel(res.getParentId()));
            AdminRes adminRes = new AdminRes();
            if (null != res.getId()) {
                //更新
                res.setTreePath(resourceService.treePath(res.getParentId(), res.getId()));
                Resource saveRes = resourceDao.saveAndFlush(res);
                adminRes.setId(saveRes.getId())
                        .setPath(saveRes.getPath())
                        .setLable(saveRes.getName())
                        .setType(saveRes.getType())
                        .setIcon(saveRes.getIcon());
                return new SuccessResult<>("修改成功", adminRes);
            }
            //新增
            Resource res2 = resourceDao.saveAndFlush(res);
            res2.setTreePath(resourceService.treePath(res2.getParentId(), res2.getId()));
            Resource saveRes1 = resourceDao.saveAndFlush(res2);
            adminRes.setId(saveRes1.getId())
                    .setPath(saveRes1.getPath())
                    .setLable(saveRes1.getName())
                    .setType(saveRes1.getType())
                    .setIcon(saveRes1.getIcon());
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
        Resource res = new Resource();
        res.setParentId(rid);
        Example<Resource> example = Example.of(res);
        if (resourceDao.exists(example)) {
            return new ErrorResult<>("资源正在被使用");
        }
        Resource resource = resourceDao.findById(rid).orElse(null);
        if (null != resource.getRoles() && resource.getRoles().size() > 0) {
            return new ErrorResult<>("资源正在被使用");
        }
        resourceDao.deleteById(rid);
        return new SuccessResult("删除成功");
    }

    /**
     * 获取资源树
     *
     * @return
     */
    @GetMapping("/findResTree")
    public ApiResult findResTree() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<Resource> all = resourceDao.findAll(sort);
        Set<Resource> adminResSet = new HashSet<>();
        all.forEach(r -> adminResSet.add(r));
        AdminRes adminRes = new AdminRes();
        adminRes.setId(0)
                .setLable("菜单列表")
                .setType("-1")
                .setIcon("fa-icon")
                .setTreePath("-1");
        Set<AdminRes> resTree = adminService.getResTree(adminResSet);
        adminRes.setChild(resTree);
        Set<AdminRes> one=new HashSet<>();
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
        Resource resource = new Resource();
        resource.setParentId(parentId);
        List<Resource> all = resourceDao.findAll(Example.of(resource));
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
