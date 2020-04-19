package jjk.csauth.service;

import jjk.csauth.dao.AdminDao;
import jjk.csauth.pojo.Admin;
import jjk.csauth.pojo.AdminRes;
import jjk.csauth.pojo.Resource;
import jjk.csutils.pojo.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: jjk
 * @create: 2020-03-16 10:25
 * @program: cloudStudy
 * @description: 用户服务
 */
@Service
@Slf4j
public class AdminService {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private ResourceService resourceService;

    public MyPage<Admin> pageAdmin(MyPage<Admin> page) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page.getCurrPage() - 1, page.getPageSize(), sort);
        Admin admin = page.getParam();
        if (null != admin) {
            ExampleMatcher em = ExampleMatcher.matching();
            if (null != admin.getUsername()) {
                em = em.withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains());
            }
            if (null != admin.getName()) {
                em = em.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
            }
            if (null != admin.getPhone()) {
                em = em.withMatcher("phone", ExampleMatcher.GenericPropertyMatchers.contains());
            }
            Example<Admin> example = Example.of(admin, em);
            Page<Admin> all = adminDao.findAll(example, pageable);
            MyPage prop = page.getProp(all);
            return prop;
        }
        Page<Admin> all = adminDao.findAll(Example.of(admin), pageable);
        MyPage prop = page.getProp(all);
        return prop;
    }

    /**
     * 获取用户资源树
     *
     * @param rlist
     * @return
     */
    public Set<AdminRes> getResTree(Set<Resource> rlist) {
        return resourceService.getTree(rlist);
    }

    /**
     * 获取按钮级别path
     *
     * @param rlist
     * @return
     */
    public List<String> getPerBtn(Set<Resource> rlist) {
        if (null != rlist && rlist.size() > 0) {
            ArrayList<String> list = new ArrayList<>();
            for (Resource r : rlist) {
                if (r.getType().contains("2")) {
                    list.add(r.getPath());
                }
            }
            return list;
        }
        return null;
    }
}
