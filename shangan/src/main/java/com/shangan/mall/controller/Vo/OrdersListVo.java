package com.shangan.mall.controller.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class OrdersListVo {
    @ApiModelProperty("订单编号")
    private String orderNo;
    @ApiModelProperty("订单价格")
    private Integer totalPrice;
    @ApiModelProperty("是否付款")
    private Byte payStatus;
    @ApiModelProperty("发货状态")
    private Byte orderStatus;
    @ApiModelProperty("下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
