package jjk.csauth.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jjk.csauth.pojo.Admin;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * 获取用户根据用户名与密码
     *
     * @param username
     * @param password
     * @return
     */
    @Select("select * from admin " +
            "where username=#{username} " +
            "and password=#{password} " +
            "and enable='0'")
    @Results(id = "findAdminByUP",
            value = {
                    @Result(id = true, column = "id", property = "id"),
                    @Result(column = "name", property = "name"),
                    @Result(column = "username", property = "username"),
                    @Result(column = "password", property = "password"),
                    @Result(column = "phone", property = "phone"),
                    @Result(column = "avatar", property = "avatar"),
                    @Result(column = "enable", property = "enable"),
                    @Result(column = "rid", property = "role", one = @One(select = "jjk.csauth.dao.RoleMapper.findRoleById", fetchType = FetchType.EAGER)),
            }
    )
    Admin findAdminByUP(@Param("username") String username, @Param("password") String password);
}
