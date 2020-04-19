package jjk.csutils.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
@MappedSuperclass
public class Tree {
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
