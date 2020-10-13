package jjk.cscar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jjk.cscar.pojo.Car;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author jjk
 * @create 2020-06-28 16:04
 * @Describtion 车辆mapper
 **/
@Repository
public interface CarMapeer extends BaseMapper<Car> {
    /**
     * 查询车辆关系是否为空
     *
     * @return
     */
    @Select("select count(1) from car_relation where main_car_id=#{id} or car_id=#{id}")
    int findCarRelationCount(Integer id);
}
