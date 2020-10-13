package jjk.cspubliccomponent.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import jjk.csutils.pojo.BasePojo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author jjk
 * @create 2020-07-07 20:22
 * @Describtion 提示信息
 **/
@Data
@Accessors(chain = true)
@TableName("tip_msg")
public class TipMsg extends BasePojo<TipMsg> {
    /**
     * 消息
     */
    private String msg;

    /**
     * 对应用户
     */
    private Integer aid;

    /**
     * 已读状态0未读，1已读
     */
    private String msgSign;

    /**
     * 对应用户名称
     */
    @TableField(exist = false)
    private String username;

    /**
     * 查询全部标识0查自己，1查全部
     */
    @TableField(exist = false)
    @JSONField(serialize = false)
    private String checkAll = new String("0");

    /**
     * 查询数据开始时间
     */
    @TableField(exist = false)
    @JSONField(serialize = false, format = "yyyy-MM-dd HH:mm:ss")
    private Date actionTime;

    /**
     * 查询数据结束时间
     */
    @TableField(exist = false)
    @JSONField(serialize = false, format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
