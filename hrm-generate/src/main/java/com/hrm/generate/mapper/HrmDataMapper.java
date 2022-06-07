package com.hrm.generate.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 数据库操作接口
 *
 * @author LZL
 * @version v1.0
 * @date 2022/6/6-19:57
 */
@Mapper
@Repository
public interface HrmDataMapper {
    Integer deleteData(@Param("companyId") String companyId);
}
