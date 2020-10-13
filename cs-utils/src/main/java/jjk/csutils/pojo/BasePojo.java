package jjk.csutils.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author jjk
 * @create 2020-06-28 14:46
 * @Describtion 基础类
 **/
@Data
public class BasePojo<T extends Model<?>> extends Model<T> {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 数据新建时间
     */
    @TableField(
            fill = FieldFill.INSERT
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date optime;

    /**
     * 数据更新时间
     */
    @TableField(
            fill = FieldFill.UPDATE, update = "now()"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uptime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
