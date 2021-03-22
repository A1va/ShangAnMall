package com.shangan.mall.service;

import com.shangan.mall.entity.Rights;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;

public interface RightService {

    Rights getRightById(Long administratorId);

    PageResult getAllNormalRights(PageQueryUtil pageQueryUtil);

    Boolean editAdminRights(Long adminUserId,Boolean orderManagement,int type);
}
