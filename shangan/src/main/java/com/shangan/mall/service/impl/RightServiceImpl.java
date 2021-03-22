package com.shangan.mall.service.impl;

import com.shangan.mall.controller.Vo.GoodsListVo;
import com.shangan.mall.dao.RightsMapper;
import com.shangan.mall.entity.Goods;
import com.shangan.mall.entity.Rights;
import com.shangan.mall.service.RightService;
import com.shangan.util.BeanUtil;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("rightService")
public class RightServiceImpl implements RightService {

    @Resource
    private RightsMapper rightsMapper;

    @Override
    public Rights getRightById(Long administratorId) {
        return rightsMapper.selectRightsById(administratorId);
    }

    @Override
    public PageResult getAllNormalRights(PageQueryUtil pageQueryUtil) {
        int total = rightsMapper.selectRightsTotalNum(false);
//        List<GoodsListVo> goodsListVos = new ArrayList<GoodsListVo>();
//        List<Goods> goods = goodsMapper.selectAllGoods(pageQueryUtil);
        List<Rights> rights = rightsMapper.selectAllNormalRights();
        System.out.println(rights);
        PageResult pageResult = new PageResult(rights, total, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public Boolean editAdminRights(Long adminUserId, Boolean Management, int type) {
        Rights rights = rightsMapper.selectRightsById(adminUserId);
        if(type == 1) {
            rights.setUserManagement(Management);
        }else if(type == 2) {
            rights.setOrderManagement(Management);
        }else if(type == 3) {
            rights.setGoodsManagement(Management);
        }else {
            rights.setLogManagement(Management);
        }
        return rightsMapper.updateRightsSelective(rights) > 0;
    }
}
