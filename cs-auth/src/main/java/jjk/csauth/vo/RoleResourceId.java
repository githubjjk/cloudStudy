package jjk.csauth.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author: jjk
 * @create: 2020-03-09 20:59
 * @program: cloudStudy
 * @description: 角色资源id
 */
@Data
@Accessors(chain=true)
public class RoleResourceId {
    private Integer rid;

    private List<Integer> resId;
}
