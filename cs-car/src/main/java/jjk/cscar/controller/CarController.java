package jjk.cscar.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jjk.cscar.mapper.CarMapeer;
import jjk.cscar.pojo.Car;
import jjk.csutils.pojo.ApiResult;
import jjk.csutils.pojo.ErrorResult;
import jjk.csutils.pojo.MyPage;
import jjk.csutils.pojo.SuccessResult;
import jjk.csutils.service.JsonSwitch;
import jjk.csutils.service.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jjk
 * @create 2020-06-28 15:36
 * @Describtion 车辆控制层
 **/
@RestController
@RequestMapping("/car")
@Slf4j
public class CarController {
    @Autowired
    private CarMapeer carMapeer;


    /**
     * 保存或修改车辆信息
     *
     * @param json
     * @return
     */
    @PostMapping("/saveCar")
    public ApiResult saveCar(@RequestBody String json) {
        Car car = JsonSwitch.getJavaObj(json, Car.class);
        List<String> prop = ObjectUtils.setArrayList("carType", "carCode", "carName");
        if (ObjectUtils.checkObjNotNull(car, (ArrayList<String>) prop)) {
            if (null != car.getId()) {
                carMapeer.updateById(car);
                return new SuccessResult<Car>("保存成功", car);
            }
            QueryWrapper<Car> cq = new QueryWrapper<>();
            cq.eq("carName", car.getCarName());
            cq.eq("carCode", car.getCarCode());
            if (carMapeer.selectCount(cq) > 0) {
                return new ErrorResult("车辆已存在");
            }
            carMapeer.insert(car);
            return new SuccessResult<Car>("保存成功", car);
        }
        return new ErrorResult<>("参数不能为空");
    }

    /**
     * 删除车辆信息
     *
     * @return
     */
    @GetMapping("/deleteCar/{id}")
    public ApiResult deleteCar(@PathVariable Integer id) {
        int count = carMapeer.findCarRelationCount(id);
        if (count > 0) {
            return new ErrorResult<>("车辆正在使用");
        }
        carMapeer.deleteById(id);
        return new SuccessResult<>("删除成功");
    }

    /**
     * 获取车辆列表
     *
     * @param json
     * @return
     */
    @PostMapping("/pageCar")
    public ApiResult pageCar(@RequestBody String json) {
        MyPage<Car> page = JsonSwitch.getPage(json, Car.class);
        Car car = page.getParam();
        Page<Car> mpage = new Page<>(page.getCurrPage(), page.getPageSize());
        QueryWrapper<Car> cw = new QueryWrapper<>();
        if (null != car) {
            if (StringUtils.isNotEmpty(car.getCarName())) {
                cw.like("car_name", car.getCarName());
            }
            if (StringUtils.isNotEmpty(car.getCarCode())) {
                cw.like("car_code", car.getCarCode());
            }
            if (StringUtils.isNotEmpty(car.getCarType())) {
                cw.eq("car_type", car.getCarType());
            }
        }
        Page<Car> carPage = carMapeer.selectPage(mpage, cw);
        return new SuccessResult<>(carPage);
    }

    /**
     * 没有关联关系的车辆
     *
     * @return
     */
    @GetMapping("/finNoRelationCar")
    public ApiResult findNoRelationCar() {
        
        return null;
    }
}
