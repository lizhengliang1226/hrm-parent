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
                    "case when sauss.current_basic_salary=0 or sauss.current_basic_salary is null then 0  when sauss.current_basic_salary>0 then 1 end  isFixed " +
                    "from bs_user bu LEFT JOIN sa_user_salary sauss ON bu.id=sauss.user_id  " +
                    "WHERE bu.company_id = ?1",
            countQuery = "select count(*) from bs_user bu LEFT JOIN sa_user_salary sauss ON bu.id=sauss.user_id WHERE bu.company_id = ?1"
    )
    Page<Map> findPage(String companyId, PageRequest pageRequest);

    @Query(nativeQuery = true,
            value = "select \n" +
                    "a.id ,a.username ,a.mobile ,a.work_number workNumber,a.department_name departmentName,b.id_number idNumber,b.opening_bank openingBank,\n" +
                    "b.bank_card_number bankCardNumber,a.in_service_status inServiceStatus,a.form_of_employment formOfEmployment\n" +
                    ",ifnull(c.current_basic_salary,0)+ifnull(c.current_post_wage,0) currentSalaryTotalBase ,\n" +
                    "ifnull(c.current_basic_salary,0) currentBaseSalary,ifnull(c.current_basic_salary,0) baseSalaryByMonth,\n" +
                    "ifnull(d.salary_official_days,0) officialSalaryDays ,d.salary_standards salaryStandard,\n" +
                    "ifnull(e.tax_calculation_type ,'普通方式') taxCountingMethod,\n" +
                    "f.personal_provident_fund_payment taxToProvidentFund,\n" +
                    "ifnull(c.current_basic_salary,0) baseSalaryToTaxByMonth,\n" +
                    "ifnull(c.current_basic_salary,0)+ifnull(c.current_post_wage,0) salaryBeforeTax,\n" +
                    "ifnull(f.personal_provident_fund_payment,0) providentFundIndividual,\n" +
                    "ifnull(f.social_security_personal_base,0) socialSecurityIndividual,\n" +
                    "ifnull(g.old_age_individual,0) oldAgeIndividual,\n" +
                    "ifnull(g.medical_individual,0) medicalIndividual,\n" +
                    "ifnull(g.unemployed_individual,0) unemployedIndividual,\n" +
                    "g.a_person_of_great_disease aPersonOfGreatDisease,\n" +
                    "f.social_security_personal_base socialSecurity,\n" +
                    "f.personal_provident_fund_payment totalProvidentFund,\n" +
                    "g.big_disease_enterprise bigDiseaseEnterprise,\n" +
                    "g.childbearing_enterprise childbearingEnterprise,\n" +
                    "g.industrial_injury_enterprise industrialInjuryEnterprise,\n" +
                    "g.unemployed_enterprise unemployedEnterprise,\n" +
                    "g.medical_enterprise medicalEnterprise,\n" +
                    "g.pension_enterprise pensionEnterprise,\n" +
                    "g.social_security_enterprise socialSecurityEnterprise,\n" +
                    "g.provident_fund_enterprises providentFundEnterprises,\n" +
                    "g.social_security_enterprise+g.provident_fund_enterprises socialSecurityProvidentFundEnterprises\n" +
                    "from bs_user a\n" +
                    "left join em_user_company_personal b on a.id=b.user_id \n" +
                    "left join sa_user_salary c on a.id=c.user_id \n" +
                    "left join atte_archive_monthly_info d on a.id=d.user_id \n" +
                    "left join sa_settings e on e.company_id =a.company_id \n" +
                    "left join ss_user_social_security f on a.id=f.user_id \n" +
                    "left join ss_archive_detail g on g.user_id=a.id\n" +
                    "where a.company_id = ?1 \n" +
                    "and d.archive_date = ?2 \n" +
                    "and g.years_month = ?2",
            countQuery = "select count(*)\n" +
                    "from bs_user a\n" +
                    "left join em_user_company_personal b on a.id=b.user_id \n" +
                    "left join sa_user_salary c on a.id=c.user_id \n" +
                    "left join atte_archive_monthly_info d on a.id=d.user_id \n" +
                    "left join sa_settings e on e.company_id =a.company_id \n" +
                    "left join ss_user_social_security f on a.id=f.user_id \n" +
                    "left join ss_archive_detail g on g.user_id=a.id\n" +
                    "where a.company_id = ?1 \n" +
                    "and d.archive_date = ?2 \n" +
                    "and g.years_month = ?2"
    )
    Page<Map> findArchivePage(String companyId, String yearMonth, PageRequest pageRequest);

    /**
     * 根据用户的id和年月查询某年某月的用户薪资详情
     *
     * @param userId
     * @param yearMonth
     * @return
     */
    @Query(nativeQuery = true,
            value = "    select\n" +
                    "        a.staff_photo staffPhoto,\n" +
                    "        a.company_id ,\n" +
                    "        a.in_service_status inServiceStatus,\n" +
                    "        a.id,\n" +
                    "        a.username,\n" +
                    "        a.time_of_entry timeOfEntry,\n" +
                    "        c.current_basic_salary + c.current_post_wage latestSalaryBase,\n" +
                    "        c.current_basic_salary basicWageBaseForTheLatestMonth,\n" +
                    "        c.current_post_wage basicWageBaseForThatMonth,\n" +
                    "        d.transportation_subsidy_amount transportationSubsidyAmount,\n" +
                    "        d.communication_subsidy_amount communicationSubsidyAmount,\n" +
                    "        d.lunch_allowance_amount lunchAllowanceAmount,\n" +
                    "        d.housing_subsidy_amount housingSubsidyAmount,\n" +
                    "        e.social_security_base socialSecurityBase,\n" +
                    "        e.social_security_company_base socialSecurityCompanyBase,\n" +
                    "        e.social_security_personal_base socialSecurityPersonalBase,\n" +
                    "        e.provident_fund_base providentFundBase,\n" +
                    "        e.enterprise_provident_fund_payment providentFundCompanyBase,\n" +
                    "        e.personal_provident_fund_payment providentFundPersonalBase,\n" +
                    "        f.actual_atte_official_days actualAttendanceDaysAreOfficial,\n" +
                    "        f.salary_official_days officialSalaryDays,\n" +
                    "        f.salary_standards salaryStandard,\n" +
                    "        d.tax_calculation_type taxCountingMethod \n" +
                    "    from\n" +
                    "        bs_user a \n" +
                    "    left join\n" +
                    "        sa_user_salary c \n" +
                    "            on a.id = c.user_id \n" +
                    "    left join\n" +
                    "        ss_user_social_security e \n" +
                    "            on a.id = e.user_id \n" +
                    "    left join\n" +
                    "        atte_archive_monthly_info f \n" +
                    "            on a.id = f.user_id \n" +
                    "    left join\n" +
                    "        sa_settings d \n" +
                    "            on a.company_id = d.company_id \n" +
                    "    where\n" +
                    "        a.id=?1 \n" +
                    "        and f.archive_date=?2")
    Map findUserSalaryDetailInfo(String userId, String yearMonth);
}
