package com.shangan.mall.service.impl;

import com.shangan.common.Constants;
import com.shangan.config.GlobalExceptionHandler;
import com.shangan.common.ServiceResultEnum;
import com.shangan.mall.controller.param.SaveCartItemParam;
import com.shangan.mall.controller.param.UpdateCartItemParam;
import com.shangan.mall.controller.vo.ShoppingCartItemVo;
import com.shangan.mall.dao.GoodsMapper;
import com.shangan.mall.dao.ShoppingCartItemMapper;
import com.shangan.mall.entity.Goods;
import com.shangan.mall.entity.ShoppingCartItem;
import com.shangan.mall.service.ShoppingCartService;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author Alva
 * @CreateTime 2021/1/30 21:21
 */
@Service("shoppingCartItemService")
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartItemMapper shoppingCartItemMapper;
    private final GoodsMapper goodsMapper;

    public ShoppingCartServiceImpl(ShoppingCartItemMapper shoppingCartItemMapper, GoodsMapper goodsMapper) {
        this.shoppingCartItemMapper = shoppingCartItemMapper;
        this.goodsMapper = goodsMapper;
    }

    /**
     * 首先对参数进行校验，校验步骤如下：
     *  - 根据用户信息和商品信息去查询购物项表中是否已存在相同的记录，如果存在则进行修改操作，不存在则进行后续操作
     *  - 判断商品数据是否正确
     *  - 判断用户的购物车中的商品数量是否已超出最大限制
     * 校验通过后再进行新增操作，将该记录保存到数据库中，以上操作中都需要调用 SQL 语句来完成
     * @param saveCartItemParam
     * @param userId
     * @return
     */
    @Override
    public String saveCartItem(SaveCartItemParam saveCartItemParam, Long userId) {
//        根据用户信息和商品信息去查询购物项表中是否已存在相同的记录
        ShoppingCartItem temp = shoppingCartItemMapper.selectByUserIdAndGoodsId(userId,
                saveCartItemParam.getGoodsId());

        if (temp != null) {
//            已存在则修改该记录
            UpdateCartItemParam updateCartItemParam = new UpdateCartItemParam();
            updateCartItemParam.setCartItemId(temp.getCartItemId());
            updateCartItemParam.setGoodsCount(temp.getGoodsCount() + saveCartItemParam.getGoodsCount());
//            判断更新购物车数据库操作是否成功
            if (updateCartItem(updateCartItemParam, userId).equals(ServiceResultEnum.SUCCESS.getResult())) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
//        判断商品数据是否正确
        Goods Goods = goodsMapper.selectByPrimaryKey(saveCartItemParam.getGoodsId());
//        商品为空，返回 "商品不存在!"
        if (Goods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
//        判断用户的购物车中的商品数量是否已超出最大限制
        int totalItem = shoppingCartItemMapper.selectCountByUserId(userId);
//        单个商品的数量小于 1，返回 "商品数量不能小于 1！"
        if (saveCartItemParam.getGoodsCount() < 1) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_NUMBER_ERROR.getResult();
        }
//        超出单个商品的最大数量，返回 "超出单个商品的最大购买数量！"
        if (saveCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
//        超出购物车的最大数量，返回 "超出购物车最大容量！"
        if (totalItem > Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
        }
//        通过 ShoppingCartItem 类接收前端传回来的参数类 SaveCartItemParam (深拷贝)
        ShoppingCartItem ShoppingCartItem = new ShoppingCartItem();
        BeanUtils.copyProperties(saveCartItemParam, ShoppingCartItem);
//        注意，设置用户的 Id
        ShoppingCartItem.setUserId(userId);
//        保存记录，返回数据库操作结果
        if (shoppingCartItemMapper.insertSelective(ShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    /**
     * updateCartItem 修改当前购物项的数量
     * 对参数进行校验，校验步骤如下：
     * 1.首先根据前端传参的购物项主键 id 去查询购物项表中是否存在该记录，如果不存在则返回错误信息，存在则进行后续操作
     * 2.判断用户的购物车中的商品数量是否已超出最大限制
     * 3.校验通过后再进行修改操作，将该购物项记录的数量和修改时间修改掉，以上操作中都需要调用 SQL 语句来完成。
     * @param updateCartItemParam
     * @param userId
     * @return
     */
    @Override
    public String updateCartItem(UpdateCartItemParam updateCartItemParam, Long userId) {

        ShoppingCartItem shoppingCartItemUpdate = shoppingCartItemMapper.
                selectByPrimaryKey(updateCartItemParam.getCartItemId());
        if (shoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (!shoppingCartItemUpdate.getUserId().equals(userId)) {
            GlobalExceptionHandler.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
//        超出单个商品的最大数量
        if (updateCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
//        Byte a = updateCartItemParam.getChecked();
        Byte a;
        if(updateCartItemParam.getChecked() == 1){
            a = 1;
        }else{
            a = 0;
        }
        shoppingCartItemUpdate.setChecked(a);
        // shoppingCartItemUpdate.setChecked(updateCartItemParam.getChecked());
        shoppingCartItemUpdate.setGoodsCount(updateCartItemParam.getGoodsCount());
        shoppingCartItemUpdate.setUpdateTime(new Date());
//        修改记录
        if (shoppingCartItemMapper.updateByPrimaryKeySelective(shoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public ShoppingCartItem getCartItemById(Long ShoppingCartItemId) {
        ShoppingCartItem shoppingCartItem = shoppingCartItemMapper.selectByPrimaryKey(ShoppingCartItemId);
        if (shoppingCartItem == null) {
            GlobalExceptionHandler.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return shoppingCartItem;
    }

    @Override
    public Boolean deleteById(Long ShoppingCartItemId) {
        return shoppingCartItemMapper.deleteByPrimaryKey(ShoppingCartItemId) > 0;
    }

    @Override
    public List<ShoppingCartItemVo> getMyShoppingCartItemList(Long UserId) {
        List<ShoppingCartItemVo> shoppingCartItemVoList = new ArrayList<>();
        List<ShoppingCartItem> shoppingCartItemList = shoppingCartItemMapper.selectByUserId(UserId,
                Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        // System.out.println(shoppingCartItemList);

        return getShoppingCartItemVoList(shoppingCartItemVoList, shoppingCartItemList);
    }

    private List<ShoppingCartItemVo> getShoppingCartItemVoList(List<ShoppingCartItemVo> shoppingCartItemVoList,
                                                       List<ShoppingCartItem> shoppingCartItemList) {
        // System.out.println("开始"+shoppingCartItemVoList);

        if (!CollectionUtils.isEmpty(shoppingCartItemList)) {
//            查询商品信息并做数据转换
            List<Long> goodsIdList = shoppingCartItemList.stream().map(ShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<Goods> goodsList = goodsMapper.selectByPrimaryKeys(goodsIdList);
            Map<Long, Goods> GoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(goodsList)) {
                GoodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (ShoppingCartItem shoppingCartItem : shoppingCartItemList) {
                ShoppingCartItemVo shoppingCartItemVo = new ShoppingCartItemVo();
                BeanUtils.copyProperties(shoppingCartItem, shoppingCartItemVo);
                if (GoodsMap.containsKey(shoppingCartItem.getGoodsId())) {
                    Goods GoodsTemp = GoodsMap.get(shoppingCartItem.getGoodsId());
                    shoppingCartItemVo.setGoodsCoverImg(GoodsTemp.getGoodsCoverImg());
                    String goodsName = GoodsTemp.getGoodsName();
//                    字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    shoppingCartItemVo.setGoodsName(goodsName);
                    shoppingCartItemVo.setSellingPrice(GoodsTemp.getSellingPrice());
                    shoppingCartItemVo.setChecked(Integer.valueOf(shoppingCartItem.getChecked()));
                    shoppingCartItemVoList.add(shoppingCartItemVo);
                }
            }
        }
        // System.out.println("结束" + shoppingCartItemVoList);
        return shoppingCartItemVoList;
    }

    @Override
    public List<ShoppingCartItemVo> getCartItemsForSettle(List<Long> cartItemIds, Long UserId) {
        List<ShoppingCartItemVo> shoppingCartItemVoList = new ArrayList<>();
        if (CollectionUtils.isEmpty(cartItemIds)) {
            GlobalExceptionHandler.fail("购物项不能为空");
        }
        List<ShoppingCartItem> shoppingCartItemList = shoppingCartItemMapper.selectByUserIdAndCartItemIds(UserId, cartItemIds);
        if (CollectionUtils.isEmpty(shoppingCartItemList)) {
            GlobalExceptionHandler.fail("购物项不能为空");
        }
        if (shoppingCartItemList.size() != cartItemIds.size()) {
            GlobalExceptionHandler.fail("参数异常");
        }
        return getShoppingCartItemVoList(shoppingCartItemVoList, shoppingCartItemList);
    }

    @Override
    public PageResult getMyShoppingCartItemList(PageQueryUtil pageUtil) {

        List<ShoppingCartItemVo> shoppingCartItemVoList = new ArrayList<>();
        List<ShoppingCartItem> shoppingCartItemList = shoppingCartItemMapper.findMyCartItems(pageUtil);
        // System.out.println(shoppingCartItemList);
        int total = shoppingCartItemMapper.getTotalMyCartItems(pageUtil);
        return new PageResult(getShoppingCartItemVoList(shoppingCartItemVoList, shoppingCartItemList),
                total, pageUtil.getLimit(), pageUtil.getPage());
    }
}
