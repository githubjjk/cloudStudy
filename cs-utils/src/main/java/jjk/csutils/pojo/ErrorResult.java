package jjk.csutils.pojo;

/**
 * @author: jjk
 * @create: 2020-01-07 09:22
 * @program: cloudStudy
 * @description: 返回警告结果
 */
public class ErrorResult<T> extends ApiResult<T> {
    public ErrorResult(int state,String msg,T data){
        super(state,msg,data);
    }

    public ErrorResult(String msg, T data) {
        super(PublicState.BUSSINSERROR.getValue(), msg, data);
    }

    public ErrorResult(String msg) {
        super(PublicState.BUSSINSERROR.getValue(), msg);
    }

    public ErrorResult(int state, String msg) {
        super(state,msg);
    }
}
