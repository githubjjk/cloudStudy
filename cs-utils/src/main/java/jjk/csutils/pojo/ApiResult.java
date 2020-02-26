package jjk.csutils.pojo;

import com.google.gson.Gson;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.UnsupportedEncodingException;

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

    /**
     * 获取序列化数据
     * @return
     */
    public byte[] getByte(){
        Gson gson = new Gson();
        byte[] result=null;
        try {
            result=gson.toJson(this).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ApiResult(int state,String msg,T data){
        this.state=state;
        this.msg=msg;
        this.data=data;
    }

    public ApiResult(int state,String msg){
        this.state=state;
        this.msg=msg;
    }

}
