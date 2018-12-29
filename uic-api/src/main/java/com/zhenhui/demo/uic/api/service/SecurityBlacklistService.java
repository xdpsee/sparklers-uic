package com.zhenhui.demo.uic.api.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("sparklers-uic")
@RequestMapping("/service")
public interface SecurityBlacklistService {

    @RequestMapping(value = "/com.zhenhui.demo.uic.api.service.SecurityBlacklistService.block", method = RequestMethod.POST)
    void block(@RequestParam("token") String token);

    @RequestMapping(value = "/com.zhenhui.demo.uic.api.service.SecurityBlacklistService.isBlock", method = RequestMethod.GET)
    boolean isBlocked(@RequestParam("token") String token);

}


