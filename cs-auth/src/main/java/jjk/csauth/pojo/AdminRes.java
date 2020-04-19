package jjk.csauth.pojo;

import jjk.csutils.pojo.Tree;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * @author jjk
 */
@Data
@Accessors(chain = true)
public class AdminRes extends Tree {
    /**
     * 主键
     */
    private Integer id;

    /**
     * title
     */
    private String lable;

    /**
     * 图标
     */
    private String icon;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 类型 (0目录,1菜单,2按钮)
     */
    private String type;

    /**
     * 子节点
     */
    private Set<AdminRes> child = null;
}
