package jjk.csutils.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: jjk
 * @create: 2020-01-06 15:46
 * @program: cloudStudy
 * @description: 请求成功返回
 */
@Data
@Accessors(chain = true)
public class SuccessResult<T> extends ApiResult<T> {

    public SuccessResult(int state,String msg,T data){
        super(state,msg,data);
    }

    public SuccessResult(int state,String msg){
        super(state,msg);
    }

    public SuccessResult(String msg){
        super(PublicState.SUCCESS.getValue(),msg);
    }

    public SuccessResult(T data){
        super(PublicState.SUCCESS.getValue(),"请求成功",data);
    }
}
