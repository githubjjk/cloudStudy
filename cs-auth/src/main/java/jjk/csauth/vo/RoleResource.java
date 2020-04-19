package jjk.csauth.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: jjk
 * @create: 2020-03-16 13:19
 * @program: cloudStudy
 * @description: 角色与资源
 */
@Data
@Accessors(chain = true)
public class RoleResource {
    private Integer roleId;

    private Integer resId;
}
