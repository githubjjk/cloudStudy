package jjk.csutils.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Tree<T> {
    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 级别(0,1,2)依次降低
     */
    private Integer level;

    /**
     * 树路径
     */
    private String path;

    /**
     * 树路径
     */
    private String treePath;

    /**
     * 子节点
     */
    @TableField(exist = false)
    private List<T> children;

    public void setParentId(Integer parentId) {
        if (null != parentId) {
            this.parentId = parentId;
        } else {
            this.parentId = 0;
        }
    }

    public void setLevel(Integer level) {
        if (null != level) {
            this.level = level;
        } else {
            this.level = 0;
        }
    }

    public void setTreePath(String treePath) {
        if (null != treePath && !treePath.equals("")) {
            this.treePath = treePath;
        } else {
            this.treePath = "";
        }
    }
}
