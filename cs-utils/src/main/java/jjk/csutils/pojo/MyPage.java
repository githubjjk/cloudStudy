package jjk.csutils.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author: jjk
 * @create: 2020-03-09 14:40
 * @program: cloudStudy
 * @description: 分页对象
 */
@Data
@Accessors(chain = true)
public class MyPage<T> {
    private Integer currPage;

    private Integer pageSize;

    /**
     * 对象列表
     */
    private List data;

    /**
     * 总条数
     */
    private Long totals;

    /**
     * 总页数
     */
    private Integer totalPage;

    @JsonIgnore
    private T param;

    public MyPage setCurrPage(Integer currPage) {
        if (null != currPage && currPage.intValue() != 0) {
            this.currPage = currPage;
        } else {
            this.currPage = 1;
        }
        return this;
    }

    public MyPage setPageSize(Integer pageSize) {
        if (null != pageSize && pageSize.intValue() != 0) {
            this.pageSize = pageSize;
        } else {
            this.pageSize = 20;
        }
        return this;
    }

    public MyPage getProp(Page page){
        if(null!=page){
            this.data= page.getContent();
            this.totalPage=page.getTotalPages();
            this.totals=page.getTotalElements();
            return this;
        }
        return null;
    }

}
