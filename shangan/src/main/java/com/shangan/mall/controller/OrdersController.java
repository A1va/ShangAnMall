package com.shangan.mall.controller;

import com.shangan.mall.service.OrderService;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.Result;
import com.shangan.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(value = "v1",tags = "上岸商城订单操作接口")
@RequestMapping("/api/private/v1")
public class OrdersController {
    @Resource
    private OrderService orderService;

    @GetMapping("/orders")
    @ApiOperation(value = "获取订单列表接口",notes = "获取并分页")
    public Result<?> getOrdersList(@RequestParam(required = false) @ApiParam(value = "搜索关键字") String query,
                                   @RequestParam(required = false) @ApiParam(value = "页码") Integer pagenum,
                                   @RequestParam(required = false) @ApiParam(value = "页大小") Integer pagesize)
    {
        Map params = new HashMap(5);
        if (pagenum == null || pagenum < 1) {
            pagenum = 1;
        }
        params.put("query", query);
        params.put("page", pagenum);
        params.put("limit", pagesize);
        System.out.println(params);
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        System.out.println(pageQueryUtil);
        return ResultGenerator.genSuccessResult(orderService.getOrdersList(pageQueryUtil));
    }

    @PutMapping("orders/{orderNo}/{edittype}")
    @ApiOperation(value = "订单状态修改")
    public Result<?> editOrderById(@PathVariable("orderNo") @ApiParam(value = "订单编号") String orderNo,
                                   @PathVariable("edittype") @ApiParam(value = "订单状态") Byte edittype)
    {
        System.out.println(orderNo+edittype);
        if(orderService.editOrderStatus(orderNo,edittype))
            return  ResultGenerator.genSuccessResult();
        else
            return ResultGenerator.genFailResult("");
    }

}
