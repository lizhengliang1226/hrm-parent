package com.hrm.domain.salary;


import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hrm.domain.attendance.entity.DeductionDict;
import com.hrm.domain.attendance.enums.DeductionEnum;
import com.hrm.domain.salary.constant.SalaryConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "sa_archive_detail")
@TableName(value = "sa_archive_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryArchiveDetail implements Serializable {
    private static final long serialVersionUID = 6021094301665428271L;

    public SalaryArchiveDetail(String userId, String mobile, String username, String departmentName) {
        this.userId = userId;
        this.mobile = mobile;
        this.username = username;
        this.departmentName = departmentName;
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
    @ExcelProperty(order = 1)
    private String username;
    /**
     * 手机号
     */
    @ExcelProperty(order = 2)
    private String mobile;
    /**
     * 工号
     */
    @ExcelProperty(order = 3)
    private String workNumber;
    /**
     * 部门名称
     */
    @ExcelProperty(order = 4)
    private String departmentName;
    /**
     * 身份证号
     */
    @ExcelProperty(order = 5)
    private String idNumber;
    /**
     * 在职状态
     */
    @ExcelProperty(order = 6)
    private String inServiceStatus;
    /**
     * 聘用形式
     */
    @ExcelProperty(order = 7)
    private Integer formOfEmployment;
    /**
     * 银行卡号
     */
    @ExcelProperty(order = 49)
    private String bankCardNumber;
    /**
     * 开户行
     */
    @ExcelProperty(order = 50)
    private String openingBank;

    //社保相关
    /**
     * 公积金个人
     */
    @ExcelProperty(order = 20)
    private BigDecimal providentFundIndividual;
    /**
     * 社保个人
     */
    @ExcelProperty(order = 21)
    private BigDecimal socialSecurityIndividual;
    /**
     * 养老个人
     */
    @ExcelProperty(order = 22)
    private BigDecimal oldAgeIndividual;
    /**
     * 医疗个人
     */
    @ExcelProperty(order = 23)
    private BigDecimal medicalIndividual;
    /**
     * 失业个人
     */
    @ExcelProperty(order = 24)
    private BigDecimal unemployedIndividual;
    /**
     * 大病个人
     */
    @ExcelProperty(order = 25)
    private BigDecimal aPersonOfGreatDisease;
    /**
     * 社保扣款 个人社保扣款
     */
    @ExcelProperty(order = 26)
    private BigDecimal socialSecurity;
    /**
     * 公积金扣款 个人公积金扣款
     */
    @ExcelProperty(order = 27)
    private BigDecimal totalProvidentFundIndividual;
    /**
     * 社保企业
     */
    @ExcelProperty(order = 33)
    private BigDecimal socialSecurityEnterprise;
    /**
     * 养老企业
     */
    @ExcelProperty(order = 34)
    private BigDecimal pensionEnterprise;
    /**
     * 医疗企业
     */
    @ExcelProperty(order = 35)
    private BigDecimal medicalEnterprise;
    /**
     * 失业企业
     */
    @ExcelProperty(order = 36)
    private BigDecimal unemployedEnterprise;
    /**
     * 工伤企业
     */
    @ExcelProperty(order = 37)
    private BigDecimal industrialInjuryEnterprise;
    /**
     * 生育企业
     */
    @ExcelProperty(order = 38)
    private BigDecimal childbearingEnterprise;
    /**
     * 大病企业
     */
    @ExcelProperty(order = 39)
    private BigDecimal bigDiseaseEnterprise;
    /**
     * 公积金企业缴纳
     */
    @ExcelProperty(order = 40)
    private BigDecimal providentFundEnterprises;
    /**
     * 公积金社保企业
     */
    @ExcelProperty(order = 41)
    private BigDecimal socialSecurityProvidentFundEnterprises;
    /**
     * 公积金需纳税额
     */
    @ExcelProperty(order = 16)
    private BigDecimal taxToProvidentFund;

    //考勤相关
    /**
     * 计薪天数
     */
    @ExcelProperty(order = 11)
    private BigDecimal officialSalaryDays;
    /**
     * 考勤扣款
     */
    @ExcelProperty(order = 15)
    private BigDecimal attendanceDeductionMonthly;
    /**
     * 计薪标准
     */
    @ExcelProperty(order = 12)
    private String salaryStandard;

    //薪资相关
    /**
     * 最新工资基数合计 基本工资+岗位工资
     */
    @ExcelProperty(order = 8)
    private BigDecimal currentSalaryTotalBase;
    /**
     * 最新基本工资基数
     */
    @ExcelProperty(order = 9)
    private BigDecimal currentBaseSalary;
    /**
     * 当月基本工资基数
     */
    @ExcelProperty(order = 10)
    private BigDecimal baseSalaryByMonth;
    /**
     * 计税方式
     */
    @ExcelProperty(order = 13)
    private Integer taxCountingMethod;
    /**
     * 当月纳税基本工资 = 当月基本工资基数
     */
    @ExcelProperty(order = 14)
    private BigDecimal baseSalaryToTaxByMonth;
    /**
     * 税前工资合计 （当月基本工资+当月岗位工资）
     */
    @ExcelProperty(order = 17)
    private BigDecimal salaryBeforeTax;
    /**
     * 工资合计 （基本工资+岗位工资+津贴
     */
    @ExcelProperty(order = 18)
    private BigDecimal salary;
    /**
     * 应纳税工资 基本工资 + 岗位工资 + （【开关】津贴）
     */
    @ExcelProperty(order = 19)
    private BigDecimal salaryByTax;
    /**
     * 税前实发  基本工资 + 岗位工资 + 津贴 - 五险一金  ？
     */
    @ExcelProperty(order = 28)
    private BigDecimal paymentBeforeTax;
    /**
     * 应扣税 （工资 + 【开关】津贴）* 阶梯税率 - 速算扣除数
     */
    @ExcelProperty(order = 29)
    private BigDecimal tax;
    /**
     * 税后工资合计 税前工资 - 税
     */
    @ExcelProperty(order = 30)
    private BigDecimal salaryAfterTax;
    /**
     * 实发工资  基本工资+岗位工资 + 津贴 - 五险一金 -税
     */
    @ExcelProperty(order = 31)
    private BigDecimal payment;
    /**
     * 实发工资备注
     */
    @ExcelProperty(order = 32)
    private String paymentRemark;
    /**
     * 薪酬成本  0
     */
    @ExcelProperty(order = 42)
    private BigDecimal salaryCost;
    /**
     * 企业人工成本 0
     */

    @ExcelProperty(order = 43)
    private BigDecimal enterpriseLaborCost;
    /**
     * 调薪金额 当月的
     */
    @ExcelProperty(order = 44)
    private BigDecimal salaryChangeAmount;
    /**
     * 调薪比例
     */
    @ExcelProperty(order = 45)
    private BigDecimal salaryChangeScale;
    /**
     * 调薪生效时间
     */
    @ExcelProperty(order = 46)
    private String effectiveTimeOfPayAdjustment;
    /**
     * 调薪原因
     */
    @ExcelProperty(order = 47)
    private String causeOfSalaryAdjustment;
    /**
     * 注释  --
     */
    @ExcelProperty(order = 48)
    private String remark;
    /**
     * 发薪月数   0
     */
    @ExcelProperty(order = 51)
    private Integer paymentMonths;

    public BigDecimal getLaterMoney() {
        return laterMoney == null ? BigDecimal.ZERO : laterMoney;
    }

    public BigDecimal getEarlyMoney() {
        return earlyMoney == null ? BigDecimal.ZERO : earlyMoney;
    }

    public void setLaterMoney(BigDecimal laterMoney) {
        this.laterMoney = laterMoney;
    }

    public void setEarlyMoney(BigDecimal earlyMoney) {
        this.earlyMoney = earlyMoney;
    }

    private BigDecimal laterMoney;
    private BigDecimal earlyMoney;
    /**
     * 津贴
     */
    private BigDecimal allowance;

    public BigDecimal getAllowance() {
        return allowance == null ? BigDecimal.ZERO : allowance;
    }

    public void setAllowance(BigDecimal allowance) {
        this.allowance = allowance;
    }

    public BigDecimal getProvidentFundEnterprises() {
        return this.providentFundEnterprises == null ? new BigDecimal(0) : this.providentFundEnterprises;
    }

    public BigDecimal getSocialSecurityEnterprise() {
        return this.socialSecurityEnterprise == null ? new BigDecimal(0) : this.socialSecurityEnterprise;
    }

    public BigDecimal getSocialSecurityIndividual() {
        return this.socialSecurityIndividual == null ? new BigDecimal(0) : this.socialSecurityIndividual;
    }

    public BigDecimal getProvidentFundIndividual() {
        return this.providentFundIndividual == null ? new BigDecimal(0) : this.providentFundIndividual;
    }

    public BigDecimal getSalaryBeforeTax() {
        return this.salaryBeforeTax == null ? new BigDecimal(0) : this.salaryBeforeTax;
    }

    public BigDecimal getEntTotal() {
        return getProvidentFundEnterprises().add(getSocialSecurityEnterprise());
    }

    public BigDecimal getPerTotal() {
        return getSocialSecurityIndividual().add(getProvidentFundIndividual());
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getMobile() {
        return mobile;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getInServiceStatus() {
        return inServiceStatus;
    }

    public Integer getFormOfEmployment() {
        return formOfEmployment;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public String getOpeningBank() {
        return openingBank;
    }

    public BigDecimal getOldAgeIndividual() {
        return oldAgeIndividual;
    }

    public BigDecimal getMedicalIndividual() {
        return medicalIndividual;
    }

    public BigDecimal getUnemployedIndividual() {
        return unemployedIndividual;
    }

    public BigDecimal getaPersonOfGreatDisease() {
        return aPersonOfGreatDisease;
    }

    public BigDecimal getSocialSecurity() {
        return socialSecurity;
    }

    public BigDecimal getTotalProvidentFundIndividual() {
        return totalProvidentFundIndividual;
    }

    public BigDecimal getPensionEnterprise() {
        return pensionEnterprise;
    }

    public BigDecimal getMedicalEnterprise() {
        return medicalEnterprise;
    }

    public BigDecimal getUnemployedEnterprise() {
        return unemployedEnterprise;
    }

    public BigDecimal getIndustrialInjuryEnterprise() {
        return industrialInjuryEnterprise;
    }

    public BigDecimal getChildbearingEnterprise() {
        return childbearingEnterprise;
    }

    public BigDecimal getBigDiseaseEnterprise() {
        return bigDiseaseEnterprise;
    }

    public BigDecimal getSocialSecurityProvidentFundEnterprises() {
        return socialSecurityProvidentFundEnterprises;
    }

    public BigDecimal getTaxToProvidentFund() {
        return taxToProvidentFund;
    }

    public BigDecimal getOfficialSalaryDays() {
        return officialSalaryDays;
    }

    public BigDecimal getAttendanceDeductionMonthly() {
        return attendanceDeductionMonthly == null ? BigDecimal.ZERO : attendanceDeductionMonthly;
    }

    public String getSalaryStandard() {
        return salaryStandard;
    }

    public BigDecimal getCurrentSalaryTotalBase() {
        return currentSalaryTotalBase;
    }

    public BigDecimal getCurrentBaseSalary() {
        return currentBaseSalary;
    }

    public BigDecimal getBaseSalaryByMonth() {
        return baseSalaryByMonth;
    }

    public Integer getTaxCountingMethod() {
        return taxCountingMethod;
    }

    public BigDecimal getBaseSalaryToTaxByMonth() {
        return baseSalaryToTaxByMonth;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public BigDecimal getSalaryByTax() {
        return salaryByTax;
    }

    public BigDecimal getPaymentBeforeTax() {
        return paymentBeforeTax;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getSalaryAfterTax() {
        return salaryAfterTax;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public String getPaymentRemark() {
        return paymentRemark;
    }

    public BigDecimal getSalaryCost() {
        return salaryCost;
    }

    public BigDecimal getEnterpriseLaborCost() {
        return enterpriseLaborCost;
    }

    public BigDecimal getSalaryChangeAmount() {
        return salaryChangeAmount;
    }

    public BigDecimal getSalaryChangeScale() {
        return salaryChangeScale;
    }

    public String getEffectiveTimeOfPayAdjustment() {
        return effectiveTimeOfPayAdjustment;
    }

    public String getCauseOfSalaryAdjustment() {
        return causeOfSalaryAdjustment;
    }

    public String getRemark() {
        return remark;
    }

    public Integer getPaymentMonths() {
        return paymentMonths;
    }

    //设置用户属性
    public void setUser(Map map) {
        /*用户信息部分*/
        this.bankCardNumber = (String) map.get("bankCardNumber");
        this.openingBank = (String) map.get("openingBank");
        this.idNumber = (String) map.get("idNumber");
        this.formOfEmployment = (Integer) map.get("formOfEmployment");
        this.username = map.get("username").toString();
        this.departmentName = map.get("departmentName").toString();
        this.mobile = map.get("mobile").toString();
        this.userId = map.get("id").toString();
        this.workNumber = (String) map.get("workNumber").toString();
        this.inServiceStatus = map.get("inServiceStatus").toString();
        /*五险一金部分*/
        // 养老企业缴费
        this.pensionEnterprise = (BigDecimal) map.get("pensionEnterprise");
        // 医疗企业缴费
        this.medicalEnterprise = (BigDecimal) map.get("medicalEnterprise");
        this.unemployedEnterprise = (BigDecimal) map.get("unemployedEnterprise");
        this.industrialInjuryEnterprise = (BigDecimal) map.get("industrialInjuryEnterprise");
        this.childbearingEnterprise = (BigDecimal) map.get("childbearingEnterprise");
        this.bigDiseaseEnterprise = (BigDecimal) map.get("bigDiseaseEnterprise");
        // 个人社保扣款
        this.socialSecurity = (BigDecimal) map.get("socialSecurity");
        // 个人公积金扣款
        this.totalProvidentFundIndividual = (BigDecimal) map.get("totalProvidentFund");
        this.oldAgeIndividual = (BigDecimal) map.get("oldAgeIndividual");
        this.medicalIndividual = (BigDecimal) map.get("medicalIndividual");
        this.unemployedIndividual = (BigDecimal) map.get("unemployedIndividual");
        this.aPersonOfGreatDisease = (BigDecimal) map.get("aPersonOfGreatDisease");
        // 公积金缴纳税额
        this.taxToProvidentFund = (BigDecimal) map.get("taxToProvidentFund");
        // 计税方式，针对津贴的，有税前和税后
        Object taxCountingMethod = map.get("taxCountingMethod");
        if (taxCountingMethod == null) {
            taxCountingMethod = 1;
        }
        this.taxCountingMethod = (Integer) taxCountingMethod;
        // 当月纳税基本薪资
        this.baseSalaryToTaxByMonth = (BigDecimal) map.get("baseSalaryToTaxByMonth");
        this.salaryStandard = (String) map.get("salaryStandard");
        this.currentBaseSalary = (BigDecimal) map.get("currentBaseSalary");
        this.baseSalaryByMonth = (BigDecimal) map.get("baseSalaryByMonth");
        // 计薪天数21.75
        this.officialSalaryDays = (BigDecimal) map.get("officialSalaryDays");
        this.providentFundIndividual = (BigDecimal) map.get("providentFundIndividual");
        this.providentFundEnterprises = (BigDecimal) map.get("providentFundEnterprises");
        this.socialSecurityEnterprise = (BigDecimal) map.get("socialSecurityEnterprise");
        this.socialSecurityIndividual = (BigDecimal) map.get("socialSecurityIndividual");
        // 公积金社保企业合计缴费
        this.socialSecurityProvidentFundEnterprises = (BigDecimal) map.get("socialSecurityProvidentFundEnterprises");
        // 最新基本工资合计
        this.currentSalaryTotalBase = (BigDecimal) map.get("currentSalaryTotalBase");
    }

    // 计算工资
    public void calSalary(Settings settings, Map map, List<DeductionDict> deductionDicts) {

        final String departmentId = (String) map.get("departmentId");
        final int later = (int) map.get("laterTimes");
        final int early = (int) map.get("earlyTimes");
        BigDecimal lateMoney = BigDecimal.ZERO;
        BigDecimal earlyMoney = BigDecimal.ZERO;
        if (deductionDicts != null && deductionDicts.size() > 0) {
            // 计算迟到和早退扣款
            for (DeductionDict dict : deductionDicts) {
                if (dict.getIsEnable() == 1) {
                    // 扣款可用
                    final String departmentId1 = dict.getDepartmentId();
                    final BigDecimal lowerLimit = dict.getDedAmonutLowerLimit();
                    if (departmentId.equals(departmentId1)) {
                        // 部门相同
                        if (dict.getDedTypeCode().equals(DeductionEnum.LATE_DEDUCTION.getCode())) {
                            // 迟到扣款
                            if (later >= Integer.parseInt(dict.getTimesUpperLimit())) {
                                lateMoney = dict.getDedAmonutUpperLimit();
                            } else {
                                lateMoney = lateMoney.add(lowerLimit.multiply(new BigDecimal(later)));
                            }
                        }
                        if (dict.getDedTypeCode().equals(DeductionEnum.EARLY_DEDUCTION.getCode())) {
                            // 早退扣款
                            if (early >= Integer.parseInt(dict.getTimesUpperLimit())) {
                                earlyMoney = dict.getDedAmonutUpperLimit();
                            } else {
                                earlyMoney = earlyMoney.add(lowerLimit.multiply(new BigDecimal(early)));
                            }
                        }
                    }
                }
            }
        }

        // 设置早退和迟到扣款
        this.laterMoney = lateMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
        this.earlyMoney = earlyMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
        //计算福利津贴
        BigDecimal money = BigDecimal.ZERO;
        // 默认当月
        Integer socialSecurityType = 1;
        // 默认税前
        Integer taxCalculationType = 1;
        if (settings != null) {
            taxCalculationType = settings.getTaxCalculationType();
            socialSecurityType = settings.getSocialSecurityType();
            if (settings.getCommunicationSubsidyScheme().equals(SalaryConstant.MONTH_FIXED)) {
                money = money.add(settings.getCommunicationSubsidyAmount());
            } else if (settings.getCommunicationSubsidyScheme().equals(SalaryConstant.ATTE_DAY)) {
                money = money.add(settings.getCommunicationSubsidyAmount().multiply(this.officialSalaryDays));
            } else if (settings.getCommunicationSubsidyScheme().equals(SalaryConstant.PAID_DAY)) {
                money = money.add(settings.getCommunicationSubsidyAmount().multiply(this.officialSalaryDays));

            }
            if (settings.getHousingSubsidyScheme() == 3) {
                money = money.add(settings.getHousingSubsidyAmount());
            } else {
                money = money.add(settings.getHousingSubsidyAmount().multiply(this.officialSalaryDays));
            }
            if (settings.getLunchAllowanceScheme() == 3) {
                money = money.add(settings.getLunchAllowanceAmount());
            } else {
                money = money.add(settings.getLunchAllowanceAmount().multiply(this.officialSalaryDays));
            }
            if (settings.getTransportationSubsidyScheme() == 3) {
                money = money.add(settings.getTransportationSubsidyAmount());
            } else {
                money = money.add(settings.getTransportationSubsidyAmount().multiply(this.officialSalaryDays));
            }

        }

        //津贴
        this.allowance = money.setScale(2, BigDecimal.ROUND_HALF_UP);

        //计算考勤扣款
        BigDecimal attendanceMoney = this.currentSalaryTotalBase;
        this.salaryBeforeTax = this.currentSalaryTotalBase;
        // 20 , 10000, 500  19
        if (officialSalaryDays.compareTo(new BigDecimal("21.75")) <= 0) {
            attendanceMoney = this.currentSalaryTotalBase.divide(new BigDecimal("21.75"), 2, BigDecimal.ROUND_HALF_UP).
                    multiply(this.officialSalaryDays).setScale(2, BigDecimal.ROUND_HALF_UP);
            this.attendanceDeductionMonthly = this.currentSalaryTotalBase.subtract(attendanceMoney)
                                                                         .add(lateMoney)
                                                                         .add(earlyMoney)
                                                                         .setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            this.attendanceDeductionMonthly = BigDecimal.ZERO.add(lateMoney).add(earlyMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        // 去掉考勤扣款后的薪资，再加上补助
        this.currentSalaryTotalBase = this.currentSalaryTotalBase.subtract(this.attendanceDeductionMonthly)
                                                                 .add(money)
                                                                 .setScale(2, BigDecimal.ROUND_HALF_UP);
        //薪资合计
        this.salary = this.currentSalaryTotalBase;

        //计算应纳税工资 (岗位工资 + 基本工资 + 补助 - 缴纳公积金和社保)
        if (socialSecurityType == 1) {
            // 当月
            this.salaryByTax = this.currentSalaryTotalBase.subtract(this.providentFundIndividual)
                                                          .subtract(this.socialSecurityIndividual)
                                                          .setScale(2, BigDecimal.ROUND_HALF_UP);

        } else if (socialSecurityType == 2) {
            // 次月
            this.salaryByTax = this.currentSalaryTotalBase.subtract(this.providentFundIndividual)
                                                          .subtract(this.socialSecurityIndividual)
                                                          .setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        this.salaryByTax = this.salaryByTax.compareTo(BigDecimal.ZERO) >= 0 ? this.salaryByTax : BigDecimal.ZERO;

        //计算税(扣除员工社保和员工公积金部门)
        this.tax = getTax(taxCalculationType == 1 ? salaryByTax : salaryByTax.subtract(money)).setScale(2, BigDecimal.ROUND_HALF_UP);
        this.paymentBeforeTax = this.salaryByTax;

        //计算实发工资
        this.payment = this.salaryByTax.subtract(tax).setScale(2, BigDecimal.ROUND_HALF_UP);
        this.salaryAfterTax = payment;
        this.payment = this.payment.compareTo(BigDecimal.ZERO) >= 0 ? this.payment : BigDecimal.ZERO;

    }

    private BigDecimal getTax(BigDecimal salary) {
        //salary * 阶梯税率 - 速算扣除数
        BigDecimal easyNum = new BigDecimal(0);
        BigDecimal stageNum = new BigDecimal(0.03);
        if (salary.doubleValue() <= 3500) {
            return BigDecimal.ZERO;
        } else if (salary.doubleValue() > 80000) {
            easyNum = new BigDecimal(13505);
            stageNum = new BigDecimal(0.45);
        } else if (salary.doubleValue() > 55000) {
            easyNum = new BigDecimal(5505);
            stageNum = new BigDecimal(0.35);
        } else if (salary.doubleValue() > 35000) {
            easyNum = new BigDecimal(2755);
            stageNum = new BigDecimal(0.3);
        } else if (salary.doubleValue() > 9000) {
            easyNum = new BigDecimal(1005);
            stageNum = new BigDecimal(0.25);
        } else if (salary.doubleValue() > 4500) {
            easyNum = new BigDecimal(555);
            stageNum = new BigDecimal(0.2);
        }
        salary = salary.multiply(stageNum);
        salary = salary.subtract(easyNum);
        return salary;
    }

}
