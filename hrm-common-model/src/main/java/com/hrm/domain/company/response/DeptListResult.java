package com.hrm.domain.company.response;

import com.hrm.domain.company.Company;
import com.hrm.domain.company.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 部门信息返回实体
 *
 * @author LZL
 * @date 2022/3/7-20:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeptListResult {
    private String companyId;
    private String companyName;
    private String companyManager;
    private List<Department> depts;

    public DeptListResult(Company company, List depts) {
        this.companyId = company.getId();
        this.companyName = company.getName();
        this.companyManager = company.getLegalRepresentative();
        this.depts = depts;
    }
}
