package jjk.csauth.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jjk.csauth.pojo.Resource;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ResourceMapper extends BaseMapper<Resource> {
    /**
     * 根据角色id获取对应资源
     *
     * @param rid
     * @return
     */
    @Select("select r.* from role_resource rr left join resource r on rr.res_id=r.id where rr.role_id=#{rid}")
    Set<Resource> findResourceByRid(int rid);

    /**
     * 资源是否被使用
     *
     * @param rid
     * @return
     */
    @Select("select count(*) from role_resource where res_id=#{rid}")
    int findResourceRelationById(Integer rid);
}
