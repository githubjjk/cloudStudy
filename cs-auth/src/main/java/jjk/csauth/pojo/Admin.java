package jjk.csauth.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: jjk
 * @create: 2019-04-10 22:37
 * @program: shirojjk01
 * @description: 用户信息
 */
@TableName("sh_admin")
@Data
@Accessors(chain = true)
public class Admin extends Model<Admin> {
    //主键
    private Integer aid;
    //用户名
    private String aUsername;
    //密码
    private String aPassword;
    //姓名
    private String aName;
    //是否被锁定0可用1锁定
    private Integer aLock;
    //用户电话
    private String aPhone;
    //用户的token
    @TableField(exist = false)
    private String token;


}
