package com.hrm.domain.social;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 社保归档明细表
 */
@Entity
@Table(name = "ss_archive_detail")
@TableName(value = "ss_archive_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class SocialSecrityArchiveDetail implements Serializable {
    private static final long serialVersionUID = -5571547188954376291L;

    public SocialSecrityArchiveDetail(String userId, String mobile, String username, String departmentName) {
        this.userId = userId;
        this.mobile = mobile;
        this.username = username;
        this.firstLevelDepartment = departmentName;
    }

    public void setUserSocialSecurity(Map map) {
        // 社保基数
        final BigDecimal socialSecurityBase = BigDecimal.valueOf((Integer) map.get("SocialSecurityBase"));
        // 公积金基数
        final BigDecimal providentFundBase = BigDecimal.valueOf((Integer) map.get("providentFundBase"));
        this.householdRegistrationType = (Integer) map.get("householdRegistrationType");
        this.participatingInTheCity = (String) map.get("participatingInTheCity");
        this.providentFundCity = (String) map.get("providentFundCity");
        this.socialSecurityNotes = (String) map.get("socialSecurityNotes");
        this.providentFundNotes = (String) map.get("providentFundNotes");
        this.socialSecurityBase = socialSecurityBase;
        this.providentFundBase = providentFundBase;
        this.accumulationFundEnterpriseBase = providentFundBase;
        this.proportionOfProvidentFundEnterprises = (BigDecimal) map.get("enterpriseProportion");
        this.personalRatioOfProvidentFund = (BigDecimal) map.get("personalProportion");
        this.individualBaseOfProvidentFund = providentFundBase;
        this.providentFundEnterprises = (BigDecimal) map.get("enterpriseProvidentFundPayment");
        this.providentFundIndividual = (BigDecimal) map.get("personalProvidentFundPayment");
        this.totalProvidentFund = this.providentFundEnterprises.add(this.providentFundIndividual);
        this.pensionEnterpriseBase = socialSecurityBase;
        this.personalPensionBase = socialSecurityBase;
        this.unemploymentEnterpriseBase = socialSecurityBase;
        this.medicalEnterpriseBase = socialSecurityBase;
        this.medicalPersonalBase = socialSecurityBase;
        this.baseOfIndustrialInjuryEnterprises = socialSecurityBase;
        this.fertilityEnterpriseBase = socialSecurityBase;
        this.baseOfSeriousIllness = socialSecurityBase;
        this.personalBaseOfSeriousIllness = socialSecurityBase;
        this.theNumberOfUnemployedIndividuals = socialSecurityBase;
    }

    public void setUserCompanyPersonal(Map map) {
        this.workingCity = (String) map.get("workingCity");
        this.idNumber = (String) map.get("idNumber");
        this.theHighestDegreeOfEducation = (String) map.get("theHighestDegreeOfEducation");
        this.openingBank = (String) map.get("openingBank");
        this.bankCardNumber = (String) map.get("bankCardNumber");
        this.socialSecurityComputerNumber = (String) map.get("socialSecurityComputerNumber");
        this.providentFundAccount = (String) map.get("providentFundAccount");
    }

    /**
     * id
     */
    @Id
    private String id;
    /**
     * 归档id
     */
    private String archiveId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 姓名
     */
    private String username;
    /**
     * 入职时间
     */
    private Date timeOfEntry;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 身份证号
     */
    private String idNumber;
    /**
     * 学历
     */
    private String theHighestDegreeOfEducation;
    /**
     * 开户行
     */
    private String openingBank;
    /**
     * 银行卡号
     */
    private String bankCardNumber;
    /**
     * 一级部门
     */
    private String firstLevelDepartment;
    /**
     * 二级部门
     */
    private String twoLevelDepartment;
    /**
     * 工作城市
     */
    private String workingCity;
    /**
     * 社保电脑号
     */
    private String socialSecurityComputerNumber;
    /**
     * 公积金账号
     */
    private String providentFundAccount;
    /**
     * 离职时间
     */
    private String leaveDate;
    /**
     * 户籍类型
     */
    private Integer householdRegistrationType;
    /**
     * 参保城市
     */
    private String participatingInTheCity;
    /**
     * 社保月份
     */
    private String socialSecurityMonth;
    /**
     * 社保基数
     */
    private BigDecimal socialSecurityBase;
    /**
     * 社保合计
     */
    private BigDecimal socialSecurity;
    /**
     * 社保企业
     */
    private BigDecimal socialSecurityEnterprise;
    /**
     * 社保个人
     */
    private BigDecimal socialSecurityIndividual;
    /**
     * 公积金城市
     */
    private String providentFundCity;
    /**
     * 公积金月份
     */
    private String providentFundMonth;
    /**
     * 公积金基数
     */
    private BigDecimal providentFundBase;
    /**
     * 公积金企业基数
     */
    private BigDecimal accumulationFundEnterpriseBase;
    /**
     * 公积金企业比例
     */
    private BigDecimal proportionOfProvidentFundEnterprises;
    /**
     * 公积金个人基数
     */
    private BigDecimal individualBaseOfProvidentFund;
    /**
     * 公积金个人比例
     */
    private BigDecimal personalRatioOfProvidentFund;
    /**
     * 公积金合计
     */
    private BigDecimal totalProvidentFund;
    /**
     * 公积金企业
     */
    private BigDecimal providentFundEnterprises;
    /**
     * 公积金个人
     */
    private BigDecimal providentFundIndividual;
    /**
     * 养老企业基数
     */
    private BigDecimal pensionEnterpriseBase;
    /**
     * 养老企业比例
     */
    private BigDecimal proportionOfPensionEnterprises;
    /**
     * 养老企业
     */
    private BigDecimal pensionEnterprise;
    /**
     * 养老个人基数
     */
    private BigDecimal personalPensionBase;
    /**
     * 养老个人比例
     */
    private BigDecimal personalPensionRatio;
    /**
     * 养老个人
     */
    private BigDecimal oldAgeIndividual;
    /**
     * 失业企业基数
     */
    private BigDecimal unemploymentEnterpriseBase;
    /**
     * 失业企业比例
     */
    private BigDecimal proportionOfUnemployedEnterprises;
    /**
     * 失业企业
     */
    private BigDecimal unemployedEnterprise;
    /**
     * 失业个人基数
     */
    private BigDecimal theNumberOfUnemployedIndividuals;
    /**
     * 失业个人比例
     */
    private BigDecimal percentageOfUnemployedIndividuals;
    /**
     * 失业个人
     */
    private BigDecimal unemployedIndividual;
    /**
     * 医疗企业基数
     */
    private BigDecimal medicalEnterpriseBase;
    /**
     * 医疗企业比例
     */
    private BigDecimal proportionOfMedicalEnterprises;
    /**
     * 医疗企业
     */
    private BigDecimal medicalEnterprise;
    /**
     * 医疗个人基数
     */
    private BigDecimal medicalPersonalBase;
    /**
     * 医疗个人比例
     */
    private BigDecimal medicalPersonalRatio;
    /**
     * 医疗个人
     */
    private BigDecimal medicalIndividual;
    /**
     * 工伤企业基数
     */
    private BigDecimal baseOfIndustrialInjuryEnterprises;
    /**
     * 工伤企业比例
     */
    private BigDecimal proportionOfIndustrialInjuryEnterprises;
    /**
     * 工伤企业
     */
    private BigDecimal industrialInjuryEnterprise;
    /**
     * 生育企业基数
     */
    private BigDecimal fertilityEnterpriseBase;
    /**
     * 生育企业比例
     */
    private BigDecimal proportionOfFertilityEnterprises;
    /**
     * 生育企业
     */
    private BigDecimal childbearingEnterprise;
    /**
     * 大病企业基数
     */
    private BigDecimal baseOfSeriousIllness;
    /**
     * 大病企业比例
     */
    private BigDecimal proportionOfSeriouslyIllEnterprises;
    /**
     * 大病企业
     */
    private BigDecimal bigDiseaseEnterprise;
    /**
     * 大病个人基数
     */
    private BigDecimal personalBaseOfSeriousIllness;
    /**
     * 大病个人比例
     */
    private BigDecimal personalProportionOfSeriousIllness;
    /**
     * 大病个人
     */
    private BigDecimal aPersonOfGreatDisease;
    /**
     * 公积金备注
     */
    private String providentFundNotes;
    /**
     * 社保备注
     */
    private String socialSecurityNotes;

    private String yearsMonth;
}
