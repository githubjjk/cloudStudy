package jjk.csauth.service;

import jjk.csauth.dao.ResourceDao;
import jjk.csauth.pojo.AdminRes;
import jjk.csauth.pojo.Resource;
import jjk.csutils.pojo.Tree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
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
    private ResourceDao resourceDao;


    /**
     * 拼接树路径
     *
     * @param parentId
     * @param id
     * @return
     */
    public String treePath(Integer parentId, Integer id) {
        if (parentId.intValue() == 0) {
            return id + "";
        }
        Resource one = resourceDao.getOne(parentId);
        return one.getTreePath() + "#" + id;
    }

    /**
     * 获取资源级别
     *
     * @param pid
     * @return
     */
    public Integer resLevel(Integer pid) {
        if (pid.intValue() == 0) {
            return 1;
        }
        Resource one = resourceDao.getOne(pid);
        return one.getLevel() + 1;
    }

    /**
     * 获取树集合
     *
     * @param list
     * @return
     */
    public Set<AdminRes> getTree(Set<Resource> list) {
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
                if (res.getLevel() == 0) {
                    menu.add(ar);
                } else if (res.getLevel() == 1) {
                    path.add(ar);
                } else if (res.getLevel() == 2) {
                    button.add(ar);
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
