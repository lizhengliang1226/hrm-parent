package com.hrm.attendance.dao;


import com.hrm.domain.attendance.entity.DeductionDict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DeductionDictDao extends JpaRepository<DeductionDict, String>, JpaSpecificationExecutor<DeductionDict> {


    /**
     * 根据公司和部门查询扣款配置信息
     *
     * @param companyId
     * @param departmentId
     * @return *
     */
    List<DeductionDict> findByCompanyIdAndDepartmentId(String companyId, String departmentId);


    /**
     * 查询扣款配置
     *
     * @param companyId
     * @param departmentId
     * @param dedTypeCode
     * @return
     */
    DeductionDict findByCompanyIdAndDepartmentIdAndDedTypeCode(String companyId, String departmentId, String dedTypeCode);

    /**
     * 根据企业id查询全部扣款设置
     *
     * @param companyId
     * @return
     */
    List<DeductionDict> findByCompanyId(String companyId);
}
