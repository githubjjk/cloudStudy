package jjk.cspubliccomponent.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import jjk.cspubliccomponent.pojo.TipMsg;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author jjk
 * @create 2020-06-28 18:05
 * @Describtion 自动填充数据数据
 **/
@Component
public class MybatisPlusMetaObjectHandle implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Object optime = getFieldValByName("optime", metaObject);
        if (null != optime) {
            this.setFieldValByName("optime", new Date(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
    }
}
