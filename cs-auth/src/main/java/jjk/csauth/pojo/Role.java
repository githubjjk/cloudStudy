package jjk.csauth.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
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
@Data
@Accessors(chain = true)
public class Role extends Model<Role> {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer rid;

    /**
     * 角色名称
     */
    private String rname;

    /**
     * 0可用，1不可用
     */
    private String enable;

    /**
     * 角色代号
     */
    private String rsign;

    /**
     * 角色关联的资源
     */
    @TableField(exist = false)
    private Set<Resource> rlist = new HashSet<>();


}
