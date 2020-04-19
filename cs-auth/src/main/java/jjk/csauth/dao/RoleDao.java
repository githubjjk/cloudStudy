package jjk.csauth.dao;

import jjk.csauth.pojo.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jjk
 */
@Repository
public interface RoleDao extends JpaRepository<Role,Integer> {
    /**
     * 查询角色or
     *
     * @param rname
     * @param rsign
     * @return
     */
    List<Role> findByRnameOrRsign(String rname, String rsign);

}
