package com.hrm.salary.dao;


import com.hrm.domain.salary.UserSalary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

/**
 * 自定义dao接口继承
 * JpaRepository<实体类，主键>
 * JpaSpecificationExecutor<实体类>
 */
public interface UserSalaryDao extends JpaRepository<UserSalary, String>, JpaSpecificationExecutor<UserSalary> {

    @Query(nativeQuery = true,
            value = "select " +
                    "bu.id,bu.username," +
                    "bu.mobile," +
                    "bu.work_number workNumber," +
                    "bu.in_service_status inServiceStatus," +
                    "bu.department_name departmentName," +
                    "bu.time_of_entry timeOfEntry ," +
                    "bu.form_of_employment formOfEmployment ," +
                    "sauss.current_basic_salary wageBase," +
                    "sauss.current_basic_salary currentBasicSalary," +
                    "sauss.current_post_wage currentPostWage," +
                    "case when sauss.current_basic_salary=0 then 0  when sauss.current_basic_salary>0 then 1 end  isFixed " +
                    "from bs_user bu LEFT JOIN sa_user_salary sauss ON bu.id=sauss.user_id  " +
                    "WHERE bu.company_id = ?1",
            countQuery = "select count(*) from bs_user bu LEFT JOIN sa_user_salary sauss ON bu.id=sauss.user_id WHERE bu.company_id = ?1"
    )
    Page<Map> findPage(String companyId, PageRequest pageRequest);


    @Query(nativeQuery = true,
            value = " select " +
                    "     d.provident_fund_individual providentFundIndividual, " +
                    "     d.provident_fund_enterprises providentFundEnterprises, " +
                    "     d.social_security_enterprise socialSecurityEnterprise, " +
                    "     d.social_security_individual socialSecurityIndividual, " +
                    "        bu.id, " +
                    "        bu.username, " +
                    "        bu.mobile, " +
                    "        bu.work_number workNumber, " +
                    "        bu.in_service_status inServiceStatus, " +
                    "        bu.department_name departmentName, " +
                    "        bu.time_of_entry timeOfEntry , " +
                    "        bu.form_of_employment formOfEmployment , " +
                    "        sauss.current_basic_salary wageBase, " +
                    "        sauss.current_basic_salary currentBasicSalary, " +
                    "        sauss.current_post_wage currentPostWage, " +
                    "        c.salary_official_days salaryOfficialDays, " +
                    "         e.id_number idNumber," +
                    "        case  " +
                    "            when sauss.current_basic_salary = 0 then 0 " +
                    "when sauss.current_basic_salary>0 then 1  " +
                    "        end isFixed " +
                    "from " +
                    "        bs_user bu " +
                    "left join " +
                    "        sa_user_salary sauss  " +
                    "            on " +
                    "bu.id = sauss.user_id " +
                    "left join atte_archive_monthly_info c on " +
                    "bu.id = c.user_id " +
                    "left join ss_archive_detail d on " +
                    "bu.id = d.user_id " +
                    " left join em_user_company_personal e on e.user_id=bu.id " +
                    "where " +
                    "        bu.company_id = ?1 " +
                    "and c.archive_date = ?2 " +
                    "and d.years_month = ?2",
            countQuery = "select count(*) from bs_user bu LEFT JOIN sa_user_salary sauss " +
                    "ON bu.id=sauss.user_id left join atte_archive_monthly_info c on bu.id = c.user_id " +
                    "left join ss_archive_detail d on bu.id = d.user_id" +
                    " left join em_user_company_personal e on e.user_id=bu.id " +
                    " WHERE bu.company_id = ?1 " +
                    "and c.archive_date = ?2 " +
                    "and d.years_month = ?2"
    )
    Page<Map> findArchivePage(String companyId, String yearMonth, PageRequest pageRequest);

    /**
     * 根据用户的id和年月查询某年某月的用户薪资详情
     *
     * @param userId
     * @param yearMonth
     * @return
     */
    @Query(nativeQuery = true, value = "select a.staff_photo staffPhoto, a.company_id , a.in_service_status inServiceStatus,a.id, a.username, b.time_of_entry timeOfEntry, " +
            "c.current_basic_salary + c.current_post_wage latestSalaryBase, " +
            "c.current_basic_salary basicWageBaseForTheLatestMonth, " +
            "c.current_post_wage basicWageBaseForThatMonth, " +
            "d.transportation_subsidy_amount transportationSubsidyAmount, " +
            "d.communication_subsidy_amount communicationSubsidyAmount, " +
            "d.lunch_allowance_amount lunchAllowanceAmount, " +
            "d.housing_subsidy_amount housingSubsidyAmount, " +
            "e.social_security_base socialSecurityBase, " +
            "e.social_security_company_base socialSecurityCompanyBase, " +
            "e.social_security_personal_base socialSecurityPersonalBase, " +
            "e.provident_fund_base providentFundBase, " +
            "e.enterprise_provident_fund_payment providentFundCompanyBase, " +
            "e.personal_provident_fund_payment providentFundPersonalBase, " +
            "f.actual_atte_official_days actualAttendanceDaysAreOfficial, " +
            "f.salary_official_days officialSalaryDays, " +
            "f.salary_standards salaryStandard, " +
            "d.tax_calculation_type taxCountingMethod " +
            "from bs_user a " +
            "left join  " +
            "em_user_company_personal b on " +
            "a.id = b.user_id " +
            "left join " +
            "sa_user_salary c on " +
            "b.user_id = c.user_id " +
            "left join " +
            "ss_user_social_security e on " +
            "c.user_id = e.user_id " +
            "left join " +
            "atte_archive_monthly_info f on " +
            "e.user_id = f.user_id " +
            "left join " +
            "sa_settings d on " +
            "a.company_id = d.company_id " +
            "where a.id=?1 " +
            "and f.archive_date=?2")
    Map findUserSalaryDetailInfo(String userId, String yearMonth);
}
