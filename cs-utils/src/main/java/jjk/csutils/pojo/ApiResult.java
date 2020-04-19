package jjk.csutils.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: jjk
 * @create: 2020-01-06 15:30
 * @program: cloudStudy
 * @description: 返回前端
 */
@Data
@Accessors(chain = true)
public class ApiResult<T> {
    //状态
    private int state;

    //信息
    private String msg;

    //数据
    private T data;


    public ApiResult(int state, String msg, T data) {
        this.state = state;
        this.msg = msg;
        this.data = data;
    }

    public ApiResult(int state, String msg) {
        this.state = state;
        this.msg = msg;
    }

}
