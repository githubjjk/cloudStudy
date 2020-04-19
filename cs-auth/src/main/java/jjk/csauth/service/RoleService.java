package jjk.csauth.service;

import jjk.csauth.dao.RoleDao;
import jjk.csauth.pojo.Role;
import jjk.csutils.pojo.MyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
    private RoleDao roleDao;


    public MyPage<Role> pageRole(MyPage<Role> page){
        Sort sort=Sort.by(Sort.Direction.ASC,"rid");
        Pageable pageable=PageRequest.of(page.getCurrPage()-1,page.getPageSize(),sort);
        if(null!=page.getParam()){
            ExampleMatcher em=ExampleMatcher.matching().withMatcher("rname",ExampleMatcher.GenericPropertyMatchers.contains());
            Example<Role> of = Example.of(page.getParam(), em);
            Page<Role> pageRole = roleDao.findAll(of, pageable);
            MyPage prop = page.getProp(pageRole);
            return prop;
        }
        MyPage prop2 = page.getProp(roleDao.findAll(pageable));
        return prop2;
    }
}
