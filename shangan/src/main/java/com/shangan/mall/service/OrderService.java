package com.shangan.mall.service;

import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;

import java.util.List;

public interface OrderService {
    PageResult getOrdersList(PageQueryUtil pageQueryUtil);
    Boolean editOrderStatus(String orderNo,Byte edittype);
    List<Integer> dayofprice();
}
