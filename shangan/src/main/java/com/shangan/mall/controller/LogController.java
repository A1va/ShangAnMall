package com.shangan.mall.controller;

import com.shangan.mall.service.OrderService;
import com.shangan.util.Result;
import com.shangan.util.ResultGenerator;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(value = "v1",tags = "上岸商城日志统计操作接口")
@RequestMapping("/api/private/v1")
public class LogController {
    @Resource
    private OrderService orderService;
    @GetMapping("logs/total")
    Result<?> getLastSevenTotal()
    {
        return ResultGenerator.genSuccessResult(orderService.dayofprice());
    }
}
