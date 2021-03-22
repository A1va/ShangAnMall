package com.shangan.mall.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/2/2 15:35
 * 订单详情页主要有两个功能：
 * 1.展示基本的订单信息
 *   订单基本信息：订单号、订单状态、价格等等信息
 *   商品信息：商品名称、商品图、购买数量等等
 *   客服：联系商家或者拨打电话
 *
 * 2.为用户提供订单操作
 *   用户可以在该页面进行支付订单、取消订单、确认订单等等操作。
 */
@Data
public class OrderDetailVo implements Serializable {

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("订单价格")
    private Integer totalPrice;

    @ApiModelProperty("订单支付状态码")
    private Byte payStatus;

    @ApiModelProperty("订单支付方式")
    private Byte payType;

    @ApiModelProperty("订单支付方式")
    private String payTypeString;

    @ApiModelProperty("订单支付时间")
    private Date payTime;

    @ApiModelProperty("订单状态码")
    private Byte orderStatus;

    @ApiModelProperty("订单状态")
    private String orderStatusString;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("订单项列表")
    private List<OrderItemVo> orderItemVoList;
}
