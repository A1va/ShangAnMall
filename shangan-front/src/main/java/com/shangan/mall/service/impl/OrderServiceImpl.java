package com.shangan.mall.service.impl;

import com.shangan.common.*;
import com.shangan.config.GlobalExceptionHandler;
import com.shangan.mall.controller.vo.OrderDetailVo;
import com.shangan.mall.controller.vo.OrderItemVo;
import com.shangan.mall.controller.vo.OrderListVo;
import com.shangan.mall.controller.vo.ShoppingCartItemVo;
import com.shangan.mall.dao.*;
import com.shangan.mall.entity.*;
import com.shangan.mall.service.OrderService;
import com.shangan.util.BeanUtil;
import com.shangan.util.NumberUtil;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @Author Alva
 * @CreateTime 2021/2/2 15:37
 */
@Service("orderSerivce")
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartItemMapper shoppingCartItemMapper;
    private final GoodsMapper goodsMapper;
    private final OrderAddressMapper orderAddressMapper;

    public OrderServiceImpl(OrderMapper orderMapper,OrderItemMapper orderItemMapper,
                            ShoppingCartItemMapper shoppingCartItemMapper,GoodsMapper goodsMapper,
                            OrderAddressMapper orderAddressMapper) {

        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.shoppingCartItemMapper = shoppingCartItemMapper;
        this.goodsMapper = goodsMapper;
        this.orderAddressMapper = orderAddressMapper;
    }

    /**
     * 根据 orderNo 订单号来查询订单数据，如果不存在则提示错误信息，存在则继续后续逻辑。
     * 判断订单的 userId 是否为当前登录的用户 id，如果不是则为非法请求，不能查看别人的订单信息。
     * 之后根据订单 id 去订单项表查询数据。
     * 最后封装 NewBeeMallOrderDetailVO 订单详情页数据并返回。
     * @param orderNo
     * @param userId
     * @return
     */
    @Override
    public OrderDetailVo getOrderDetailByOrderNo(String orderNo, Long userId) {
//        根据订单 No 获取订单数据，对订单进行错误判断
        Order order = orderMapper.selectByOrderNo(orderNo);
//        判空
        if (order == null) {
            GlobalExceptionHandler.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
//        如果当前的订单操作的用户 Id ≠ 上数据库的订单用户 Id，说明该用户没有权限，返回"禁止该操作"
        if (!userId.equals(order.getUserId())) {
            GlobalExceptionHandler.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
//        调用订单项 Dao 层的接口，根据订单 Id 获取订单项列表数据
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderId(order.getOrderId());
//        订单项列表有数据，遍历并拷贝到 VO 层返回给前端
        if (!CollectionUtils.isEmpty(orderItemList)) {
//            将订单项列表的数据深拷贝到订单项的 VO 层列表，用于返回到前端
            List<OrderItemVo> orderItemVoList = BeanUtil.copyList(orderItemList, OrderItemVo.class);
            OrderDetailVo orderDetailVo = new OrderDetailVo();
            BeanUtils.copyProperties(order, orderDetailVo);
//            获取订单状态对应的字符串，其实也可以在 Entity 实体类中的 getter 方法中设置 String 的值
            orderDetailVo.setOrderStatusString(OrderStatusEnum.getOrderStatusEnumByStatus(orderDetailVo.getOrderStatus()).getName());
//            获取订单支付类型对应的字符串
            orderDetailVo.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(orderDetailVo.getPayType()).getName());
//            获取订单相关的所有订单项
            orderDetailVo.setOrderItemVoList(orderItemVoList);
            return orderDetailVo;
        } else {
//            无数据异常，返回 null
            GlobalExceptionHandler.fail(ServiceResultEnum.ORDER_ITEM_NULL_ERROR.getResult());
            return null;
        }
    }

    /**
     * 分页获取用户对应的订单
     * getMyOrders() 方法传入 PageUtil 对象作为参数，该对象中会有分页参数和用户的 userId，
     * 之后通过 SQL 查询出当前 userId 下的订单列表数据以及每个订单所关联的订单项列表数据，
     * 再之后是填充数据，将相关字段封装到 OrderListVo 对象中，并将封装好的 List 对象返回。
     * @param pageUtil
     * @return
     */
    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
//        获取订单总数
        int total = orderMapper.getTotalOrders(pageUtil);
//        订单列表
        List<Order> orderList = orderMapper.findOrderList(pageUtil);
        List<OrderListVo> orderListVoList = new ArrayList<>();
//        订单列表不为空
        if (total > 0) {
//            将订单列表深拷贝到订单列表的 VO 层
            orderListVoList = BeanUtil.copyList(orderList, OrderListVo.class);
//            设置订单状态中文显示值
            for (OrderListVo orderListVo : orderListVoList) {
                orderListVo.setOrderStatusString(OrderStatusEnum.getOrderStatusEnumByStatus(orderListVo.getOrderStatus()).getName());
            }
            List<Long> orderIdList = orderList.stream().map(Order::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIdList)) {
                List<OrderItem> orderItemList = orderItemMapper.selectByOrderIds(orderIdList);
                Map<Long, List<OrderItem>> itemByOrderIdMap = orderItemList.stream().collect(groupingBy(OrderItem::getOrderId));
                for (OrderListVo OrderListVO : orderListVoList) {
//                    封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(OrderListVO.getOrderId())) {
                        List<OrderItem> orderItemListTemp = itemByOrderIdMap.get(OrderListVO.getOrderId());
                        //将OrderItem对象列表转换成OrderItemVO对象列表
                        List<OrderItemVo> orderItemVoList = BeanUtil.copyList(orderItemListTemp, OrderItemVo.class);
                        OrderListVO.setOrderItemVoList(orderItemVoList);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVoList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {

        Order Order = orderMapper.selectByOrderNo(orderNo);
        if (Order != null) {
//            todo 验证是否是当前userId下的订单，否则报错
//            todo 订单状态判断
            if (orderMapper.closeOrder(Collections.singletonList(Order.getOrderId()),
                    OrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {

                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {

        Order Order = orderMapper.selectByOrderNo(orderNo);
        if (Order != null) {
//            todo 验证是否是当前userId下的订单，否则报错
//            todo 订单状态判断
            Order.setOrderStatus((byte) OrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            Order.setUpdateTime(new Date());
            if (orderMapper.updateByPrimaryKeySelective(Order) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    /**
     * 根据订单号查询订单，并进行非空判断和订单状态的判断，如果订单已经不是“待支付”状态下的订单则不进行后续操作，如果验证通过，将该订单的相关状态和支付时间进行修改，之后调用 Mapper 方法进行实际的入库操作
     * @param orderNo
     * @param payType
     * @return
     */
    @Override
    public String paySuccess(String orderNo, int payType) {

        Order Order = orderMapper.selectByOrderNo(orderNo);
        if (Order != null) {
            if (Order.getOrderStatus().intValue() != OrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()) {
                GlobalExceptionHandler.fail("非待支付状态下的订单无法支付");
            }
            Order.setOrderStatus((byte) OrderStatusEnum.OREDER_PAID.getOrderStatus());
            Order.setPayType((byte) payType);
            Order.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            Order.setPayTime(new Date());
            Order.setUpdateTime(new Date());
            if (orderMapper.updateByPrimaryKeySelective(Order) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    /**
     * 总结：先验证，之后进行订单数据封装，最后将订单数据和订单项数据保存到数据库中。
     * 订单生成的详细过程如下：
     * 1.首先是检查是否包含已下架商品，如果有则抛出一个异常，无下架商品则继续后续流程
     * 2.继续判断商品数据和商品库存，如果商品数据有误或者商品库存不足也会抛出异常，一切正常则继续后续流程
     * 3.对象的非空判断
     * 4.生成订单后，购物项数据需要删除，这里调用 ShoppingCartItemMapper.deleteBatch() 方法将这些数据批量删除
     * 5.更新商品库存记录
     * 6.判断订单价格，如果所有购物项加起来的数据为 0 或者小于 0 则不继续生成订单
     * 7.生成订单号并封装 Order 对象，保存订单记录到数据库中
     * 8.封装订单项数据并保存订单项数据到数据库中
     * 9.生成订单收货地址快照，并保存至数据库
     * 10.返回订单号
     *
     * @param loginUser
     * @param address
     * @param itemsForSave
     * @return
     */
    @Override
    public String saveOrder(User loginUser, UserAddress address, List<ShoppingCartItemVo> myShoppingCartItemList) {

        List<Long> itemIdList = myShoppingCartItemList.stream().
                map(ShoppingCartItemVo::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItemList.stream().
                map(ShoppingCartItemVo::getGoodsId).collect(Collectors.toList());

        List<Goods> goods = goodsMapper.selectByPrimaryKeys(goodsIds);
//        检查是否包含已下架商品
        List<Goods> goodsListNotSelling = goods.stream()
                .filter(goodsTemp -> goodsTemp.getGoodsSellStatus() != Constants.SELL_STATUS_UP)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsListNotSelling)) {
//            goodsListNotSelling 对象非空则表示有下架商品
            GlobalExceptionHandler.fail(goodsListNotSelling.get(0).getGoodsName() + "已下架，无法生成订单");
        }
        Map<Long, Goods> goodsMap = goods.stream().collect(Collectors.toMap(Goods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
//        判断商品库存
        for (ShoppingCartItemVo shoppingCartItemVo : myShoppingCartItemList) {
//            查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!goodsMap.containsKey(shoppingCartItemVo.getGoodsId())) {
                GlobalExceptionHandler.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
//            存在数量大于库存的情况，直接返回错误提醒
            if (shoppingCartItemVo.getGoodsCount() > goodsMap.get(shoppingCartItemVo.getGoodsId()).getStockNum()) {
                GlobalExceptionHandler.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
//        删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(goods)) {
            if (shoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItemList, StockNumDTO.class);
                int updateStockNumResult = goodsMapper.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    GlobalExceptionHandler.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
//                生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
//                保存订单
                Order order = new Order();
                order.setOrderNo(orderNo);
                order.setUserId(loginUser.getUserId());
//                总价
                for (ShoppingCartItemVo shoppingCartItemVo : myShoppingCartItemList) {
                    priceTotal += shoppingCartItemVo.getGoodsCount() * shoppingCartItemVo.getSellingPrice();
                }
                if (priceTotal < 1) {
                    GlobalExceptionHandler.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                order.setTotalPrice(priceTotal);
                String extraInfo = "";
                order.setExtraInfo(extraInfo);
//                生成订单项并保存订单项纪录
                if (orderMapper.insertSelective(order) > 0) {
//                    生成订单收货地址快照，并保存至数据库
                    OrderAddress orderAddress = new OrderAddress();
                    BeanUtils.copyProperties(address, orderAddress);
                    orderAddress.setOrderId(order.getOrderId());
//                    生成所有的订单项快照，并保存至数据库
                    List<OrderItem> orderItemList = new ArrayList<>();
                    for (ShoppingCartItemVo ShoppingCartItemVO : myShoppingCartItemList) {
                        OrderItem orderItem = new OrderItem();
//                        使用BeanUtil工具类将ShoppingCartItemVO中的属性复制到OrderItem对象中
                        BeanUtils.copyProperties(ShoppingCartItemVO, orderItem);
//                        OrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        orderItem.setOrderId(order.getOrderId());
                        orderItemList.add(orderItem);
                    }
//                    保存至数据库
                    if (orderItemMapper.insertBatch(orderItemList) > 0 && orderAddressMapper.insertSelective(orderAddress) > 0) {
//                        所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }
                    GlobalExceptionHandler.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                GlobalExceptionHandler.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            GlobalExceptionHandler.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        GlobalExceptionHandler.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }
}
