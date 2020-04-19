package jjk.csauth.dao;

import jjk.csauth.pojo.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginDao extends JpaRepository<Admin, Integer> {
}
