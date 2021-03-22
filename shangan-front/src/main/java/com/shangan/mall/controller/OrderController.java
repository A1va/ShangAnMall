package com.shangan.mall.controller;

import com.shangan.common.Constants;
import com.shangan.common.ServiceResultEnum;
import com.shangan.config.GlobalExceptionHandler;
import com.shangan.config.Token2User;
import com.shangan.mall.controller.param.SaveOrderParam;
import com.shangan.mall.controller.vo.OrderDetailVo;
import com.shangan.mall.controller.vo.OrderListVo;
import com.shangan.mall.controller.vo.ShoppingCartItemVo;
import com.shangan.mall.entity.User;
import com.shangan.mall.entity.UserAddress;
import com.shangan.mall.service.OrderService;
import com.shangan.mall.service.ShoppingCartService;
import com.shangan.mall.service.UserAddressService;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;
import com.shangan.util.Result;
import com.shangan.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Alva
 * @CreateTime 2021/2/1 14:56
 *
 * 上岸商城订单生成流程：
 * 1.在确认订单页面，用户提交订单
 *   （商品模块）
 *  - 判断购物项（商品）是否存在
 *  - 判断商品是否下架
 *  - 判断商品的库存量
 * 2.若以上判断商品状态正常，生成订单
 *  - 判断订单是否提交成功
 * 3.生成订单成功，跳转到支付页面
 *    （支付模块）
 *  - 支付订单...
 */
@RestController
@Api(value = "v1", tags = "上岸商城订单操作相关接口")
@RequestMapping("/api/v1")
public class OrderController {

    private final ShoppingCartService shoppingCartService;
    private final UserAddressService userAddressService;
    private final OrderService orderService;

    public OrderController(ShoppingCartService shoppingCartService, UserAddressService userAddressService,
                           OrderService orderService) {
        this.shoppingCartService = shoppingCartService;
        this.userAddressService = userAddressService;
        this.orderService = orderService;
    }

    /**
     * 该方法处理的映射地址为 /saveOrder，请求方法为 GET，过程如下：
     * 1.参数非空判断，如果有空数据则直接返回错误信息。
     * 2.验证购物车中是否有数据，有则继续后续流程，无则返回异常信息。
     * 3.验证收货地址信息，有则继续后续流程，无则返回异常信息。
     * 4.将购物项数据和用户信息作为参数传给业务层的 saveOrder() 方法进行订单生成的业务逻辑操作。
     * 5.如果订单生成成功，业务层的 saveOrder() 方法会返回订单号，拿到订单号之后通过接口返回给前端即可。
     *
     * @param saveOrderParam
     * @param loginUser
     * @return
     */
    @PostMapping("/saveOrder")
    @ApiOperation(value = "生成订单接口", notes = "传参为地址 id 和待结算的购物项 id 数组")
    public Result<String> saveOrder(@ApiParam(value = "订单参数") @RequestBody SaveOrderParam saveOrderParam,
                                    @Token2User User loginUser) {

        int priceTotal = 0;
        if (saveOrderParam == null || saveOrderParam.getCartItemIds() == null
                || saveOrderParam.getAddressId() == null) {
            GlobalExceptionHandler.fail(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        if (saveOrderParam.getCartItemIds().length < 1) {
            GlobalExceptionHandler.fail(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        List<ShoppingCartItemVo> itemsForSave = shoppingCartService.getCartItemsForSettle(Arrays.
                asList(saveOrderParam.getCartItemIds()), loginUser.getUserId());
        if (CollectionUtils.isEmpty(itemsForSave)) {
//            无数据
            GlobalExceptionHandler.fail("参数异常！");
        } else {
//            总价
            for (ShoppingCartItemVo shoppingCartItemVo : itemsForSave) {
                priceTotal += shoppingCartItemVo.getGoodsCount() * shoppingCartItemVo.getSellingPrice();
            }
            if (priceTotal < 1) {
                GlobalExceptionHandler.fail("价格异常！");
            }
            UserAddress address = userAddressService.getUserAddressById(saveOrderParam.getAddressId());
            if (!loginUser.getUserId().equals((address.getUserId()))) {
                return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
            }
//            保存订单并返回订单号
            String saveOrderResult = orderService.saveOrder(loginUser, address, itemsForSave);
            Result result = ResultGenerator.genSuccessResult();
            result.setData(saveOrderResult);
            return result;
        }
        return ResultGenerator.genFailResult("生成订单失败！");
    }

    /**
     *  接收的参数为订单号 orderNo，避免直接把订单 id 暴露出去，orderNo 这个参数是订单记录的唯一订单号，
     *  通过 @PathVariable 注解读取路径中的这个字段值，并根据这个值去调用 OrderService 中的 getOrderDetailByOrderNo() 方法获取
     *  到 OrderDetailVo 对象，getOrderDetailByOrderNo() 方法的实现方式就是根据 主键订单号 去查询数据库中的 订单表 并返回订单详情所需的数据
     * @param orderNo
     * @param loginUser
     * @return
     */
    @GetMapping("/order/detail")
    @ApiOperation(value = "订单详情接口", notes = "传参为订单号")
    public Result<OrderDetailVo> orderDetailPage(@ApiParam(value = "订单号")String orderNo,
                                                 @Token2User User loginUser) {

        return ResultGenerator.genSuccessResult(orderService.getOrderDetailByOrderNo(orderNo, loginUser.getUserId()));
    }

    @GetMapping("/order")
    @ApiOperation(value = "订单列表接口", notes = "传参为页码")
    public Result<PageResult<List<OrderListVo>>> orderList(@ApiParam(value = "页码") @RequestParam(required = false) Integer pageNumber,
                                                           @ApiParam(value = "订单状态:0.待支付 1.待确认 2.待发货 3:已发货 4.交易成功") @RequestParam(required = false) Integer status,
                                                           @Token2User User loginUser) {
        Map params = new HashMap(4);
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }

        params.put("userId", loginUser.getUserId());
        params.put("orderStatus", status);
        params.put("page", pageNumber);
        params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
//        封装分页请求参数
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(orderService.getMyOrders(pageUtil));
    }

    @PutMapping("/order/{orderNo}/cancel")
    @ApiOperation(value = "订单取消接口", notes = "传参为订单号")
    public Result cancelOrder(@ApiParam(value = "订单号") @PathVariable("orderNo") String orderNo,
                              @Token2User User loginUser) {

        String cancelOrderResult = orderService.cancelOrder(orderNo, loginUser.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(cancelOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(cancelOrderResult);
        }
    }

    @PutMapping("/order/{orderNo}/finish")
    @ApiOperation(value = "确认收货接口", notes = "传参为订单号")
    public Result finishOrder(@ApiParam(value = "订单号") @PathVariable("orderNo") String orderNo,
                              @Token2User User loginUser) {

        String finishOrderResult = orderService.finishOrder(orderNo, loginUser.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(finishOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(finishOrderResult);
        }
    }

    /**
     * 支付这个步骤最终是为了把订单状态改为支付成功，同时也记录下支付的相关信息，
     * 这里记录了支付时间和支付方式，一般还会记录支付的参数和第三方回调信息
     * paySuccess 负责接收支付回调数据，参数为订单号和支付方式，我们根据这两个参数对订单的状态进行修改，
     * 调用的 service 层方法为 paySuccess()
     * @param orderNo
     * @param payType
     * @return
     */
    @GetMapping("/paySuccess")
    @ApiOperation(value = "模拟支付成功回调的接口", notes = "传参为订单号和支付方式")
    public Result paySuccess(@ApiParam(value = "订单号") @RequestParam("orderNo") String orderNo,
                             @ApiParam(value = "支付方式") @RequestParam("payType") int payType) {

        String payResult = orderService.paySuccess(orderNo, payType);
        if (ServiceResultEnum.SUCCESS.getResult().equals(payResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(payResult);
        }
    }
}
