package com.hrm.company.service.mapperimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrm.company.mapper.DepartmentMapper;
import com.hrm.company.service.DepartmentMapperService;
import com.hrm.domain.company.Department;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 部门服务实现类
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/30-0:47
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class DepartmentMapperServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentMapperService {
}
