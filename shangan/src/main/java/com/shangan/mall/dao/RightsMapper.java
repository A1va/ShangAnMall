package com.shangan.mall.dao;

import com.shangan.mall.entity.Rights;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("rightsMapper")
public interface RightsMapper {

    Rights selectRightsById(@Param("adminUserId") Long adminUserId);

    List<Rights> selectAllNormalRights();

    Integer selectRightsTotalNum(boolean includeSuper);

    Integer updateRightsSelective(Rights rights);
}
