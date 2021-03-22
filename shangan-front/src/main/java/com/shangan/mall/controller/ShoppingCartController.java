package com.shangan.mall.controller;

import com.shangan.common.Constants;
import com.shangan.config.GlobalExceptionHandler;
import com.shangan.common.ServiceResultEnum;
import com.shangan.config.Token2User;
import com.shangan.mall.controller.param.SaveCartItemParam;
import com.shangan.mall.controller.param.UpdateCartItemParam;
import com.shangan.mall.controller.vo.ShoppingCartItemVo;
import com.shangan.mall.entity.ShoppingCartItem;
import com.shangan.mall.entity.User;
import com.shangan.mall.service.ShoppingCartService;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;
import com.shangan.util.Result;
import com.shangan.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/1/30 16:26
 */
@RestController
@Api(value = "v1", tags = "上岸商城购物车相关接口")
@RequestMapping("/api/v1")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/shop-cart")
    @ApiOperation(value = "添加商品到购物车接口", notes = "传参为商品 id、数量")
    public Result saveShoppingCartItem(@RequestBody SaveCartItemParam saveCartItemParam,
                                                 @Token2User User loginUser) {

        String saveResult = shoppingCartService.saveCartItem(saveCartItemParam, loginUser.getUserId());
//        添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
//        添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @GetMapping("/shop-cart/page")
    @ApiOperation(value = "购物车列表(每页默认5条)", notes = "传参为页码")
    public Result<PageResult<List<ShoppingCartItemVo>>> cartItemPageList(Integer pageNumber,
                                                                         @Token2User User loginUser) {

        HashMap params = new HashMap(4);
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("userId", loginUser.getUserId());
        params.put("page", pageNumber);
        params.put("limit", Constants.SHOPPING_CART_PAGE_LIMIT);
//        封装分页请求参数
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        // System.out.println(shoppingCartService.getMyShoppingCartItemList(pageUtil));
        return ResultGenerator.genSuccessResult(shoppingCartService.getMyShoppingCartItemList(pageUtil));
    }

    @PostMapping("/shop-cart/update")
    @ApiOperation(value = "修改购物项数据", notes = "传参为购物项id、数量")
    public Result updateShoppingCartItem(@RequestBody UpdateCartItemParam updateCartItemParam,
                                                   @Token2User User loginUser) {

        if(updateCartItemParam.getOperation().equals("update")){
            UpdateCartItemParam uc = new UpdateCartItemParam();
            uc.setCartItemId(updateCartItemParam.getCartItemId());
            uc.setGoodsCount(updateCartItemParam.getGoodsCount());
            uc.setChecked(updateCartItemParam.getChecked());
            // System.out.println(updateCartItemParam.getChecked());
            String updateResult = shoppingCartService.updateCartItem(uc, loginUser.getUserId());
//        修改成功
            if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
                return ResultGenerator.genSuccessResult();
            }
//        修改失败
            return ResultGenerator.genFailResult(updateResult);
        } else {
            Long ShoppingCartItemId = updateCartItemParam.getCartItemId();
            ShoppingCartItem CartItemById = shoppingCartService.getCartItemById(ShoppingCartItemId);
            if (!loginUser.getUserId().equals(CartItemById.getUserId())) {
                return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
            }
            Boolean deleteResult = shoppingCartService.deleteById(ShoppingCartItemId);
//        删除成功
            if (deleteResult) {
                return ResultGenerator.genSuccessResult();
            }
//        删除失败
            return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
        }

    }

    /**
     * 如果购物车中一些商品，不想进行后续的结算购买操作，可以选择将其删除。
     * 后端的编辑接口负责接收前端的 DELETE 请求并进行处理，接收的参数为 cartItemId 字段，之后调用删除方法即可完成删除操作。
     * @param ShoppingCartItemId
     * @param loginUser
     * @return
     */
    @DeleteMapping("/shop-cart/{ShoppingCartItemId}")
    @ApiOperation(value = "删除购物项", notes = "传参为购物项 id")
    public Result updateShoppingCartItem(@PathVariable("ShoppingCartItemId") Long ShoppingCartItemId,
                                                   @Token2User User loginUser) {

        ShoppingCartItem CartItemById = shoppingCartService.getCartItemById(ShoppingCartItemId);
        if (!loginUser.getUserId().equals(CartItemById.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        Boolean deleteResult = shoppingCartService.deleteById(ShoppingCartItemId);
//        删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
//        删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    /**
     * 首先判断接收的参数，cartItemIds 为空或者未登录则返回错误信息。
     * 根据 cartItemIds 和 userId 去查询 shopping_cart_item 表中的数据。
     * 如果查询到的购物项数据为空，则表示接收到的 cartItemIds 为非法参数，需要返回错误信息。
     * 最后，封装数据并通过接口返回给前端。
     * @param cartItemIds
     * @param loginUser
     * @return
     */
    @GetMapping("/shop-cart/settle")
    @ApiOperation(value = "根据购物项id数组查询购物项明细", notes = "确认订单页面使用")
    public Result<List<ShoppingCartItemVo>> toSettle(Long[] cartItemIds,
                                                     @Token2User User loginUser) {

        if (cartItemIds.length < 1) {
            GlobalExceptionHandler.fail("参数异常");
        }
        int priceTotal = 0;
        List<ShoppingCartItemVo> itemsForSettle = shoppingCartService.getCartItemsForSettle(Arrays.asList(cartItemIds), loginUser.getUserId());
        if (CollectionUtils.isEmpty(itemsForSettle)) {
//            无数据则抛出异常
            GlobalExceptionHandler.fail("参数异常");
        } else {
//            总价
            for (ShoppingCartItemVo ShoppingCartItemVO : itemsForSettle) {
                priceTotal += ShoppingCartItemVO.getGoodsCount() * ShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                GlobalExceptionHandler.fail("价格异常");
            }
        }
        return ResultGenerator.genSuccessResult(itemsForSettle);
    }
}
