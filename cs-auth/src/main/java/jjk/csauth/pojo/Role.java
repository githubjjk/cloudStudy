package jjk.csauth.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: jjk
 * @create: 2020-03-05 22:30
 * @program: cloudStudy
 * @description: 角色
 */
@Setter
@Getter
@Accessors(chain = true)
@Entity
public class Role {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rid;

    /**
     * 角色名称
     */
    @Column(length = 20)
    private String rname;

    /**
     * 0可用，1不可用
     */
    @Column(length = 2)
    private String enable;

    /**
     * 角色代号
     */
    @Column
    private String rsign;

    @JsonIgnoreProperties(value = {"roles"})
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "role_resource",
            joinColumns = @JoinColumn(referencedColumnName = "rid", name = "role_id"),
            inverseJoinColumns = @JoinColumn(referencedColumnName = "id", name = "res_id"))
    private Set<Resource> rlist = new HashSet<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.MERGE)
    private Set<Admin> admins = new HashSet<>();

}
