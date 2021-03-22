package com.shangan.mall.service.impl;

import com.shangan.mall.controller.Vo.OrdersListVo;
import com.shangan.mall.dao.OrderMapper;
import com.shangan.mall.entity.Order;
import com.shangan.mall.service.OrderService;
import com.shangan.util.BeanUtil;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    public OrderServiceImpl(OrderMapper orderMapper){
        this.orderMapper=orderMapper;
    }
    @Override
    public PageResult getOrdersList(PageQueryUtil pageQueryUtil) {
        int totalOrders=orderMapper.getOrdersTotalNum(pageQueryUtil);
        List<OrdersListVo> ordersListVos=new ArrayList<OrdersListVo>();
        List<Order> orders=orderMapper.selectAllOrders(pageQueryUtil);
        if (totalOrders>0){
            ordersListVos= BeanUtil.copyList(orders,OrdersListVo.class);
        }
        PageResult pageResult=new PageResult(orders,totalOrders,pageQueryUtil.getLimit(),pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public Boolean editOrderStatus(String orderNo, Byte edittype) {
        Order order=new Order();
        order.setOrderNo(orderNo);
        order.setOrderStatus(edittype);
        if (orderMapper.updateOrderStatus(order)!=0)
        return true;
        else return false;
    }

    @Override
    public List<Integer> dayofprice() {
        SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd" );
        Date date= new Date();
        System.out.println(sdf1.format(date));
        List<Integer> list=new ArrayList<>();
        for (int i=6;i>=0;i--){

            Date dBefore = new Date();
            Calendar calendar = Calendar.getInstance(); //得到日历
            calendar.setTime(date);//把当前时间赋给日历
            calendar.add(Calendar.DAY_OF_MONTH, -i);  //设置为前一天
            dBefore = calendar.getTime();
            String str1 = sdf1.format(dBefore);
            Integer res = orderMapper.dayofprice(str1);
            if(res == null) {
                list.add(0);
            }else {
                list.add(res);
            }
        }
        return list;
    }
}
