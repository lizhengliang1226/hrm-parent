package com.hrm.company.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrm.domain.company.Department;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门mapper接口
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/30-0:45
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}
