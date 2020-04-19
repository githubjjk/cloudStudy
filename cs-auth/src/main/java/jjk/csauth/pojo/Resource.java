package jjk.csauth.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jjk.csutils.pojo.Tree;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: jjk
 * @create: 2020-03-06 11:33
 * @program: cloudStudy
 * @description: 资源
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
public class Resource extends Tree {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资源名称
     */
    @Column(length = 20)
    private String name;

    /**
     * 是否可用(0可用,1不可用)
     */
    @Column(length = 2)
    private String enable;

    /**
     * 类型(0目录,1菜单,2按钮)
     */
    @Column(length = 2)
    private String type;

    /**
     * 图标
     */
    @Column(length = 50)
    private String icon;

    @JsonIgnoreProperties(value = {"rlist"})
    @ManyToMany(mappedBy = "rlist")
    private Set<Role> roles = new HashSet<>();

}
