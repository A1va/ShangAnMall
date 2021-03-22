package com.shangan.mall.dao;

import com.shangan.mall.entity.IndexConfig;
import com.shangan.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/1/29 12:12
 */
@Repository("indexConfigMapper")
public interface IndexConfigMapper {

    int deleteByPrimaryKey(Long configId);

    int insert(IndexConfig record);

    int insertSelective(IndexConfig record);

    IndexConfig selectByPrimaryKey(Long configId);

    int updateByPrimaryKeySelective(IndexConfig record);

    int updateByPrimaryKey(IndexConfig record);

    List<IndexConfig> findIndexConfigList(PageQueryUtil pageUtil);

    int getTotalIndexConfigs(PageQueryUtil pageUtil);

    int deleteBatch(Long[] ids);

    /**
     * 参数为 configType 和 number，类型为 SELECT 语句， 根据传入的推荐商品类型和 number 数值，查询固定数量的记录。
     * @param configType
     * @param number
     * @return
     */
    List<IndexConfig> findIndexConfigsByTypeAndNum(@Param("configType") int configType,
                                                   @Param("number") int number);
}
