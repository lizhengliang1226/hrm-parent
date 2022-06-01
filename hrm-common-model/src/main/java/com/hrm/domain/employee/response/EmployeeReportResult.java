package com.hrm.domain.employee.response;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.hrm.domain.employee.EmployeeResignation;
import com.hrm.domain.employee.UserCompanyPersonal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.Map;


/**
 * @author 17314
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class EmployeeReportResult {
    private String userId;
    private String username;
    private String departmentName;
    private String mobile;
    private String timeOfEntry;
    private String companyId;
    private String gender;
    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 籍贯
     */
    private String nativePlace;
    /**
     * 民族
     */
    private String nation;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * QQ
     */
    private String qq;
    /**
     * 微信
     */
    private String wechat;

    /**
     * 现居住地
     */
    private String placeOfResidence;

    /**
     * 个人邮箱
     */
    private String personalMailbox;

    /**
     * 社保电脑号
     */
    private String socialSecurityComputerNumber;
    /**
     * 公积金账号
     */
    private String providentFundAccount;
    /**
     * 银行卡号
     */
    private String bankCardNumber;
    /**
     * 开户行
     */
    private String openingBank;
    /**
     * 学历类型
     */
    private String educationalType;

    private String major;

    private Integer inServiceStatus;

    public EmployeeReportResult(UserCompanyPersonal personal, EmployeeResignation resignation) {
        BeanUtils.copyProperties(personal, this);
        if (resignation != null) {
            BeanUtils.copyProperties(resignation, this);
        }
    }

    public void setMap(Map map) {
        this.userId = (String) map.get("userId");
        this.username = (String) map.get("username");
        this.departmentName = (String) map.get("departmentName");
        this.mobile = (String) map.get("mobile");
        this.timeOfEntry = DateUtil.format((Date) map.get("timeOfEntry"), DatePattern.NORM_DATE_PATTERN);
        this.companyId = (String) map.get("companyId");
        this.gender = ((Character) map.get("gender")).toString();
        this.idNumber = (String) map.get("idNumber");
        this.nativePlace = (String) map.get("nativePlace");
        this.nation = (String) map.get("nation");
        this.age = (Integer) map.get("age");
        this.qq = (String) map.get("qq");
        this.wechat = (String) map.get("wechat");
        this.placeOfResidence = (String) map.get("placeOfResidence");
        this.userId = (String) map.get("userId");
        this.personalMailbox = (String) map.get("personalMailbox");
        this.socialSecurityComputerNumber = (String) map.get("socialSecurityComputerNumber");
        this.providentFundAccount = (String) map.get("providentFundAccount");
        this.bankCardNumber = (String) map.get("bankCardNumber");
        this.openingBank = (String) map.get("openingBank");
        this.educationalType = (String) map.get("educationalType");
        this.major = (String) map.get("major");
        this.inServiceStatus = (Integer) map.get("inServiceStatus");
    }
}
