package jjk.cspubliccomponent.service;

import jjk.csutils.pojo.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author jjk
 * @create 2020-07-07 20:53
 * @Describtion admin请求
 **/
@FeignClient(name = "cs-auth")
public interface AdminOpenFeign {

    @GetMapping("/admin/findAdminByUserName/{username}")
    ApiResult findAdminByUserName(@PathVariable("username") String username);
}
