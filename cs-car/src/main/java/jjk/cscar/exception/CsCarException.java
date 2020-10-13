package jjk.cscar.exception;

import jjk.csutils.pojo.ApiResult;
import jjk.csutils.pojo.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: jjk
 * @create: 2020-03-16 10:18
 * @program: cloudStudy
 * @description: 鉴权全局异常
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class CsCarException {

    @ExceptionHandler(Exception.class)
    public ApiResult csCarException(Exception e) {
        log.error("cs-auth error : {}", e.getMessage());
        return new ErrorResult("车辆微服务模块出错",e.getMessage());
    }
}
