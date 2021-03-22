package com.shangan.util;

import com.shangan.mall.controller.Vo.MenuIndexVo;
import com.shangan.mall.controller.Vo.MenuSecondVo;
import com.shangan.mall.entity.Rights;

import java.util.ArrayList;
import java.util.List;

public class MenuUtil {

    public static List<?> getMenu(Rights right) {
        List<MenuIndexVo> menuIndexVos = new ArrayList<>();


        if(right.isUserManagement()) {
            MenuSecondVo menuSecondVo = new MenuSecondVo(1,"用户列表","users");
            List<MenuSecondVo> menuSecondVos = new ArrayList<>();
            menuSecondVos.add(menuSecondVo);
            if(right.isSuper()) {
                MenuSecondVo menuSecondVo1 = new MenuSecondVo(2,"权限管理","rights");
                menuSecondVos.add(menuSecondVo1);
            }
            MenuIndexVo menuIndexVo = new MenuIndexVo(125,"账号管理",menuSecondVos);
            menuIndexVos.add(menuIndexVo);
        }
        if(right.isGoodsManagement()) {
            List<MenuSecondVo> menuSecondVos = new ArrayList<>();
            MenuSecondVo menuSecondVo = new MenuSecondVo(1,"分类管理","categories");
            MenuSecondVo menuSecondVo1 = new MenuSecondVo(2,"商品列表","goods");
            menuSecondVos.add(menuSecondVo);
            menuSecondVos.add(menuSecondVo1);
            MenuIndexVo menuIndexVo = new MenuIndexVo(101,"商品管理",menuSecondVos);
            menuIndexVos.add(menuIndexVo);
        }

        if(right.isOrderManagement()) {
            List<MenuSecondVo> menuSecondVos = new ArrayList<>();
            MenuSecondVo menuSecondVo = new MenuSecondVo(1,"订单列表","orders");
            menuSecondVos.add(menuSecondVo);
            MenuIndexVo menuIndexVo = new MenuIndexVo(102,"订单管理",menuSecondVos);
            menuIndexVos.add(menuIndexVo);
        }

        if(right.isLogManagement()) {
            List<MenuSecondVo> menuSecondVos = new ArrayList<>();
            MenuSecondVo menuSecondVo = new MenuSecondVo(1,"日志查看","logs");
            menuSecondVos.add(menuSecondVo);
            MenuIndexVo menuIndexVo = new MenuIndexVo(145,"日志管理",menuSecondVos);
            menuIndexVos.add(menuIndexVo);
        }
        return menuIndexVos;
    };
}
