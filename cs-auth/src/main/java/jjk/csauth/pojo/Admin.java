package jjk.csauth.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: jjk
 * @create: 2019-04-10 22:37
 * @program: shirojjk01
 * @description: 用户信息
 */
@Data
@Accessors(chain = true)
public class Admin extends Model<Admin> {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 可用标识
     */
    private String enable = "0";

    /**
     * 用户电话
     */
    private String phone;

    /**
     * 用户token
     */
    @TableField(exist = false)
    private String token;

    /**
     * 用户头像路径
     */
    private String avatar;

    /**
     * 对应角色id
     */
    private Integer rid;

    /**
     * 对应的角色
     */
    @TableField(exist = false)
    private Role role;
}
