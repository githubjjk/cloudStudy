package jjk.cscar.pojo;

import jjk.csutils.pojo.BasePojo;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jjk
 * @create 2020-06-28 14:27
 * @Describtion 车辆信息
 **/
@Data
@Accessors(chain = true)
public class Car extends BasePojo<Car> {
    /**
     * 车辆编号，车牌号
     */
    private String carCode;

    /**
     * 车辆名称
     */
    private String carName;

    /**
     * 0挖机，1作业车
     */
    private String carType;
}
