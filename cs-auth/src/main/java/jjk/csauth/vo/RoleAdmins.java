package jjk.csauth.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author jjk
 */
@Data
@Accessors(chain = true)
public class RoleAdmins {
    /**
     * 角色rid
     */
    private Integer rid;

    /**
     * 用户ids
     */
    private List<Integer> aids;
}
