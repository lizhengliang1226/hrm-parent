package com.hrm.domain.company.response;

import com.hrm.domain.company.Company;
import com.hrm.domain.company.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/7-20:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeptListResult {
    private String companyId;
    private String companyName;
    //公司联系人
    private String companyManager;
    private List<Department> depts;

    public DeptListResult(Company company, List depts) {
        this.companyId = company.getId();
        this.companyName = company.getName();
        this.companyManager = company.getLegalRepresentative();
        this.depts = depts;
    }
}
