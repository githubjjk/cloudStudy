package jjk.cspubliccomponent.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jjk
 * @create 2020-07-18 11:38
 * @Describtion websocket心跳
 **/
@Data
@Accessors(chain = true)
public class WsHeart<T> {
    /**
     * 心跳与数据标识0心跳，1数据
     */
    private String heartSign;

    /**
     * 心跳与数据
     */
    private T msgObj;
}
