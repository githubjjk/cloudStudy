package jjk.csauth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jjk.csauth.dao.ResourceMapper;
import jjk.csauth.pojo.AdminRes;
import jjk.csauth.pojo.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: jjk
 * @create: 2020-03-09 21:19
 * @program: cloudStudy
 * @description: 资源相关服务
 */
@Service
@Slf4j
public class ResourceService {
    @Autowired
    private ResourceMapper resourceMapper;


    /**
     * 拼接树路径
     *
     * @param parentId
     * @param id
     * @return
     */
    public String treePath(Integer parentId, Integer id) {
        if (parentId.intValue() == -1) {
            return id + "";
        }
        QueryWrapper<Resource> rq = new QueryWrapper<>();
        rq.eq("id", parentId);
        Resource one = resourceMapper.selectOne(rq);
        return one.getTreePath() + "#" + id;
    }

    /**
     * 获取资源级别
     *
     * @param pid
     * @return
     */
    public Integer resLevel(Integer pid) {
        if (pid.intValue() == -1) {
            return 0;
        }
        QueryWrapper<Resource> rq = new QueryWrapper<>();
        rq.eq("id", pid);
        Resource one = resourceMapper.selectOne(rq);
        return one.getLevel() + 1;
    }

    /**
     * 获取树集合
     *
     * @param list
     * @param routeOrBtn 0获取路由级别，1获取按钮及路由级别
     * @return
     */
    public Set<AdminRes> getTree(Set<Resource> list, String routeOrBtn) {
        if (null != list && list.size() > 0) {
            Set<AdminRes> menu = new HashSet<>();
            Set<AdminRes> path = new HashSet<>();
            Set<AdminRes> button = new HashSet<>();
            //分类
            for (Resource res : list) {
                AdminRes ar = new AdminRes()
                        .setIcon(res.getIcon())
                        .setPath(res.getPath())
                        .setLable(res.getName())
                        .setType(res.getType());
                ar.setParentId(res.getParentId());
                ar.setId(res.getId());
                //在这里进行分类
                if (routeOrBtn.equals("0")) {
                    if (res.getLevel() == 0 && res.getType().equals("0")) {
                        menu.add(ar);
                    } else if (res.getLevel() == 1 && res.getType().equals("1")) {
                        path.add(ar);
                    }
                } else if (routeOrBtn.equals("1")) {
                    if (res.getLevel() == 0 && res.getType().equals("0")) {
                        menu.add(ar);
                    } else if (res.getLevel() == 1) {
                        path.add(ar);
                    } else {
                        button.add(ar);
                    }
                }
            }
            //组合树
            for (AdminRes m : menu) {
                Set<AdminRes> child = new HashSet<>();
                for (AdminRes p : path) {
                    if (m.getId().intValue() == p.getParentId().intValue()) {
                        child.add(p);
                    }
                    Set<AdminRes> child2 = new HashSet<>();
                    for (AdminRes btn : button) {
                        if (p.getId().intValue() == btn.getParentId().intValue()) {
                            child2.add(btn);
                        }
                    }
                    if (child2.size() > 0) {
                        p.setChild(child2);
                    }
                }
                if (child.size() > 0) {
                    m.setChild(child);
                }
            }
            return menu;
        }
        return null;
    }
}
