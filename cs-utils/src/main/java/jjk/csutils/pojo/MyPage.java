package jjk.csutils.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

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
    private Long totalPage;

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

    public MyPage getProp(List<T> records, long total, long pages) {
        if (null != records) {
            this.data = records;
            this.totalPage = pages;
            this.totals = total;
            return this;
        }
        return null;
    }

}
