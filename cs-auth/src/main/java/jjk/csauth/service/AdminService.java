package jjk.csauth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jjk.csauth.dao.AdminMapper;
import jjk.csauth.dao.RoleMapper;
import jjk.csauth.pojo.Admin;
import jjk.csauth.pojo.AdminRes;
import jjk.csauth.pojo.Resource;
import jjk.csauth.pojo.Role;
import jjk.csutils.pojo.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private AdminMapper adminMapper;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private RoleMapper roleMapper;

    public MyPage<Admin> pageAdmin(MyPage<Admin> page) {
        Admin admin = page.getParam();
        QueryWrapper<Admin> qw = new QueryWrapper<>();
        if (null != admin) {
            if (null != admin.getUsername()) {
                qw.like("username", admin.getUsername());
            }
            if (null != admin.getName()) {
                qw.like("name", admin.getName());
            }
            if (null != admin.getPhone()) {
                qw.like("phone", admin.getPhone());
            }
        }
        Page<Admin> adminPage = new Page<>(page.getCurrPage() - 1, page.getPageSize());
        adminPage = adminMapper.selectPage(adminPage, qw);
        List<Admin> records = adminPage.getRecords();
        List<Role> roles = roleMapper.selectList(null);
        for (Role role : roles) {
            for (int i = 0; i < records.size(); i++) {
                if (null != records.get(i).getRid()) {
                    Integer rid = records.get(i).getRid();
                    if (rid.intValue() == role.getRid().intValue()) {
                        records.get(i).setRole(role);
                    }
                }
            }
        }
        MyPage prop = page.getProp(adminPage.getRecords(), adminPage.getTotal(), adminPage.getPages());
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
