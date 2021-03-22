package com.shangan.mall.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/2/2 16:46
 * 订单列表的 VO 层，返回前端所需的参数
 * 一个订单中可能会有多个订单项，所以订单 VO 对象中也有一个订单项 VO 的列表对象，
 * 订单列表中返回的 VO 对象
 */
@Data
public class OrderListVo {

    private Long orderId;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("订单价格")
    private Integer totalPrice;

    @ApiModelProperty("订单支付方式")
    private Byte payType;

    @ApiModelProperty("订单状态码")
    private Byte orderStatus;

    @ApiModelProperty("订单状态")
    private String orderStatusString;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("订单项列表")
    private List<OrderItemVo> OrderItemVoList;
    
}
