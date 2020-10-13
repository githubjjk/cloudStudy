package jjk.cscar.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
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
        this.setFieldValByName("optime", new Date(), metaObject);
        this.setFieldValByName("uptime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("uptime", new Date(), metaObject);
    }
}
