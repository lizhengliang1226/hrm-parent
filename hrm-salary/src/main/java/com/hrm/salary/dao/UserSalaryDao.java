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


    /**
     * 查询企业用户的薪资明细，用于构建薪资报表
     *
     * @param companyId
     * @param yearMonth
     * @param pageRequest
     * @return
     */
    @Query(nativeQuery = true,
            value = "select d.later_times laterTimes,d.early_times earlyTimes," +
                    " a.department_id departmentId,  " +
                    "a.id ,a.username ,a.mobile ,a.work_number workNumber,a.department_name departmentName,b.id_number idNumber,b.opening_bank openingBank, " +
                    "b.bank_card_number bankCardNumber,a.in_service_status inServiceStatus,a.form_of_employment formOfEmployment " +
                    ",ifnull(c.current_basic_salary,0)+ifnull(c.current_post_wage,0) currentSalaryTotalBase , " +
                    "ifnull(c.current_basic_salary,0) currentBaseSalary,ifnull(c.current_basic_salary,0) baseSalaryByMonth, " +
                    "ifnull(d.salary_official_days,0) officialSalaryDays ,d.salary_standards salaryStandard, " +
                    "e.tax_calculation_type  taxCountingMethod, " +
                    "ifnull(f.personal_provident_fund_payment,0) taxToProvidentFund, " +
                    "ifnull(c.current_basic_salary,0) baseSalaryToTaxByMonth, " +
                    "ifnull(c.current_basic_salary,0)+ifnull(c.current_post_wage,0) salaryBeforeTax, " +
                    "ifnull(f.personal_provident_fund_payment,0) providentFundIndividual, " +
                    "ifnull(f.social_security_personal_base,0) socialSecurityIndividual, " +
                    "ifnull(g.old_age_individual,0) oldAgeIndividual, " +
                    "ifnull(g.medical_individual,0) medicalIndividual, " +
                    "ifnull(g.unemployed_individual,0) unemployedIndividual, " +
                    "g.a_person_of_great_disease aPersonOfGreatDisease, " +
                    "f.social_security_personal_base socialSecurity, " +
                    "f.personal_provident_fund_payment totalProvidentFund, " +
                    "g.big_disease_enterprise bigDiseaseEnterprise, " +
                    "g.childbearing_enterprise childbearingEnterprise, " +
                    "g.industrial_injury_enterprise industrialInjuryEnterprise, " +
                    "g.unemployed_enterprise unemployedEnterprise, " +
                    "g.medical_enterprise medicalEnterprise, " +
                    "g.pension_enterprise pensionEnterprise, " +
                    "g.social_security_enterprise socialSecurityEnterprise, " +
                    "g.provident_fund_enterprises providentFundEnterprises, " +
                    "g.social_security_enterprise+g.provident_fund_enterprises socialSecurityProvidentFundEnterprises " +
                    "from bs_user a " +
                    "left join em_user_company_personal b on a.id=b.user_id  " +
                    "left join sa_user_salary c on a.id=c.user_id  " +
                    "left join atte_archive_monthly_info d on a.id=d.user_id  " +
                    "left join sa_settings e on e.company_id =a.company_id  " +
                    "left join ss_user_social_security f on a.id=f.user_id  " +
                    "left join ss_archive_detail g on g.user_id=a.id " +
                    "where a.company_id = ?1  " +
                    "and d.archive_date = ?2  " +
                    "and g.years_month = ?2 and c.user_id is not null ",
            countQuery = "select count(*) " +
                    "from bs_user a " +
                    "left join em_user_company_personal b on a.id=b.user_id  " +
                    "left join sa_user_salary c on a.id=c.user_id  " +
                    "left join atte_archive_monthly_info d on a.id=d.user_id  " +
                    "left join sa_settings e on e.company_id =a.company_id  " +
                    "left join ss_user_social_security f on a.id=f.user_id  " +
                    "left join ss_archive_detail g on g.user_id=a.id " +
                    "where a.company_id = ?1  " +
                    "and d.archive_date = ?2  " +
                    "and g.years_month = ?2 and c.user_id is not null"
    )
    Page<Map> findSalaryDetail(String companyId, String yearMonth, PageRequest pageRequest);

    /**
     * 根据用户的id和年月查询某年某月的用户薪资详情，在点击薪资列表的查看时调用
     *
     * @param userId
     * @param yearMonth
     * @return
     */
    @Query(nativeQuery = true,
            value = "    select " +
                    "        a.staff_photo staffPhoto, " +
                    "        a.company_id , " +
                    "        a.in_service_status inServiceStatus, " +
                    "        a.id, " +
                    "        a.username, " +
                    "        a.time_of_entry timeOfEntry, " +
                    "        ifnull(c.current_basic_salary,0) + ifnull(c.current_post_wage,0) latestSalaryBase, " +
                    "        ifnull(c.current_basic_salary,0) basicWageBaseForTheLatestMonth, " +
                    "        ifnull(c.current_post_wage,0) basicWageBaseForThatMonth, " +
                    "        ifnull(d.transportation_subsidy_amount,0) transportationSubsidyAmount, " +
                    "        ifnull(d.communication_subsidy_amount,0) communicationSubsidyAmount, " +
                    "        ifnull(d.lunch_allowance_amount,0) lunchAllowanceAmount, " +
                    "        ifnull(d.housing_subsidy_amount,0) housingSubsidyAmount, " +
                    "        ifnull(e.social_security_base,0) socialSecurityBase, " +
                    "        ifnull(e.social_security_company_base,0) socialSecurityCompanyBase, " +
                    "        ifnull(e.social_security_personal_base,0) socialSecurityPersonalBase, " +
                    "        ifnull(e.provident_fund_base,0) providentFundBase, " +
                    "        ifnull(e.enterprise_provident_fund_payment,0) providentFundCompanyBase, " +
                    "        ifnull(e.personal_provident_fund_payment,0) providentFundPersonalBase, " +
                    "        ifnull(f.actual_atte_official_days,0) actualAttendanceDaysAreOfficial, " +
                    "        ifnull(f.salary_official_days,21.75) officialSalaryDays, " +
                    "        f.salary_standards salaryStandard, " +
                    "        d.tax_calculation_type taxCountingMethod  " +
                    "    from " +
                    "        bs_user a  " +
                    "    left join " +
                    "        sa_user_salary c  " +
                    "            on a.id = c.user_id  " +
                    "    left join " +
                    "        ss_user_social_security e  " +
                    "            on a.id = e.user_id  " +
                    "    left join " +
                    "        atte_archive_monthly_info f  " +
                    "            on a.id = f.user_id  " +
                    "    left join " +
                    "        sa_settings d  " +
                    "            on a.company_id = d.company_id  " +
                    "    where " +
                    "        a.id=?1  " +
                    "        and f.archive_date=?2")
    Map findUserSalaryDetailInfo(String userId, String yearMonth);
}
