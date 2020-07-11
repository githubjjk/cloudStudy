package jjk.csauth.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jjk.csauth.pojo.Role;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jjk
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 获取角色根据id
     *
     * @param id
     * @return
     */
    @Select("select * from role where rid=#{id}")
    @Results(id = "findRoleById", value = {
            @Result(id = true, column = "rid", property = "rid"),
            @Result(column = "enable", property = "enable"),
            @Result(column = "rname", property = "rname"),
            @Result(column = "rsign", property = "rsign"),
            @Result(column = "rid", property = "rlist", many = @Many(select = "jjk.csauth.dao.ResourceMapper.findResourceByRid", fetchType = FetchType.LAZY)),
    })
    Role findRoleById(@Param("id") int id);

    /**
     * 角色关联资源
     *
     * @param rid
     * @param resId
     */
    @Insert({
            "<script>",
            "insert into role_resource (role_id,res_id) values",
            "<foreach collection='resIds' item='resId' index='index' separator=','>",
            "(#{roleId},#{resId})",
            "</foreach>",
            "</script>"
    })
    void insertRoleAndRes(@Param("roleId") Integer rid, @Param("resIds") List<Integer> resId);

    /**
     * 删除角色下关联的角色
     *
     * @param rid
     */
    @Delete("delete from role_resource where role_id=#{rid}")
    void deleteRoleRes(Integer rid);
}
