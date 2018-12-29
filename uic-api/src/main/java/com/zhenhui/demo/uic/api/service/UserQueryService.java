package com.zhenhui.demo.uic.api.service;

import com.zhenhui.demo.uic.api.domain.UserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Map;

@FeignClient("sparklers-uic")
@RequestMapping("/service")
public interface UserQueryService {

    @RequestMapping(value = "/com.zhenhui.demo.uic.api.service.UserQueryService.queryUser", method = RequestMethod.GET)
    UserDto queryUser(@RequestParam("id") long id);

    @RequestMapping(value = "/com.zhenhui.demo.uic.api.service.UserQueryService.queryUsers", method = RequestMethod.GET)
    Map<Long, UserDto> queryUsers(@RequestBody Collection<Long> userIds);
}
