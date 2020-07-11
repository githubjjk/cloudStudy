package jjk.csauth.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jjk.csutils.pojo.Tree;
import lombok.Data;
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
@Data
@Accessors(chain = true)
public class Resource extends Tree {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 是否可用(0可用,1不可用)
     */
    private String enable;

    /**
     * 类型(0目录,1菜单,2按钮)
     */
    private String type;

    /**
     * 图标
     */
    private String icon;


}
