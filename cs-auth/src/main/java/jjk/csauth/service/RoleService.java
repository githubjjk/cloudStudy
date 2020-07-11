package jjk.csauth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jjk.csauth.dao.RoleMapper;
import jjk.csauth.pojo.Role;
import jjk.csutils.pojo.MyPage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: jjk
 * @create: 2020-03-09 14:57
 * @program: cloudStudy
 * @description: 角色服务
 */
@Service
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;

    public MyPage<Role> pageRole(MyPage<Role> page) {
        Page<Role> rolePage = new Page<>(page.getCurrPage() - 1, page.getPageSize());
        QueryWrapper<Role> rq = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(page.getParam().getRname())) {
            rq.like("rname", page.getParam().getRname());
        }
        rq.eq("enable", "0");
        rolePage = roleMapper.selectPage(rolePage, rq);
        MyPage prop = page.getProp(rolePage.getRecords(), rolePage.getTotal(), rolePage.getPages());
        return prop;
    }
}
