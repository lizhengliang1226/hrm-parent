package com.hrm.employee.dao;

import com.hrm.domain.employee.UserCompanyPersonal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

/**
 * 数据访问接口
 *
 * @author 17314
 */
public interface UserCompanyPersonalDao extends JpaRepository<UserCompanyPersonal, String>, JpaSpecificationExecutor<UserCompanyPersonal> {
    /**
     * 查询详细信息
     *
     * @param userId 用户id
     * @return 员工详细信息
     */
    UserCompanyPersonal findByUserId(String userId);

    /**
     * 通过离职时间和入职时间查询当月的月度报表
     *
     * @param companyId 企业id
     * @param month     月份
     * @return 列表
     */
    @Query(value = "select a.in_service_status inServiceStatus, " +
            "a.id userId, a.username, a.mobile , " +
            " a.department_name departmentName,  " +
            "a.company_id companyId, a.gender , b.id_number idNumber, " +
            "b.native_place nativePlace, b.nation , b.age , b.qq , " +
            "b.wechat , b.place_of_residence placeOfResidence,  " +
            "b.social_security_computer_number socialSecurityComputerNumber,  " +
            "b.provident_fund_account providentFundAccount, b.bank_card_number bankCardNumber,  " +
            "b.opening_bank openingBank, b.educational_type educationalType, b.major,b.personal_mailbox personalMailbox,a.time_of_entry timeOfEntry " +
            "from bs_user a " +
            "left join em_user_company_personal b on " +
            "a.id = b.user_id " +
            "left join em_resignation c on " +
            "b.user_id = c.user_id " +
            "where (a.time_of_entry like ?2 " +
            "or c.resignation_time like ?2) " +
            "and a.company_id =?1", nativeQuery = true,
            countQuery = "select count(*)"
                    + "from bs_user a " +
                    "left join em_user_company_personal b on " +
                    "a.id = b.user_id " +
                    "left join em_resignation c on " +
                    "b.user_id = c.user_id " +
                    "where (a.time_of_entry like ?2 " +
                    "or c.resignation_time like ?2) " +
                    "and a.company_id =?1")
    Page<Map> findByTimeOfEntryAndResignationTime(String companyId, String month, Pageable pageable);

    @Query(value = "select count(*) from bs_user where company_id=?1 and time_of_entry like ?2 and in_service_status=?3", nativeQuery = true)
    Integer numOfJobStatus(String companyId, String month, int status);
}