package com.hrm.domain.employee;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 员工详细信息表
 *
 * @author 17314
 */
@Entity
@Table(name = "em_user_company_personal")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ExcelIgnoreUnannotated
public class UserCompanyPersonal implements Serializable {
    private static final long serialVersionUID = -8414369362479539578L;
    /**
     * 用户ID
     */
    @Id
    @ExcelProperty(value = "员工ID")
    private String userId;
    @ExcelProperty(value = "员工姓名")
    private String username;
    @ExcelProperty(value = "部门名")
    private String departmentName;
    @ExcelProperty(value = "手机号")
    private String mobile;
    @ExcelProperty(value = "入职时间")
    private String timeOfEntry;
    /**
     * 企业ID
     */
    @ExcelProperty(value = "企业ID")
    private String companyId;
    /**
     * 性别
     */
//    private String sex;
    /**
     * 出生日期
     */
//    private String dateOfBirth;
    /**
     * 最高学历
     */
    @ExcelProperty(value = "最高学历")
    private String theHighestDegreeOfEducation;
    /**
     * 国家地区
     */
    @ExcelProperty(value = "国家地区")
    private String nationalArea;
    /**
     * 护照号
     */
//    private String passportNo;
    /**
     * 身份证号
     */
    @ExcelProperty(value = "身份证号")
    private String idNumber;
    /**
     * 身份证照片-正面
     */
//    private String idCardPhotoPositive;
    /**
     * 身份证照片-背面
     */
//    private String idCardPhotoBack;
    /**
     * 籍贯
     */
    @ExcelProperty(value = "籍贯")
    private String nativePlace;
    /**
     * 民族
     */
    @ExcelProperty(value = "民族")
    private String nation;
    /**
     * 英文名
     */
//    private String englishName;
    /**
     * 婚姻状况
     */
    @ExcelProperty(value = "婚姻状况")
    private String maritalStatus;
    /**
     * 员工照片
     */
    private String staffPhoto;
    /**
     * 生日
     */
    @ExcelProperty(value = "生日")
    private String birthday;
    /**
     * 属相
     */
//    private String zodiac;
    /**
     * 年龄
     */
    @ExcelProperty(value = "年龄")
    private Integer age;
    /**
     * 星座
     */
//    private String constellation;
    /**
     * 血型
     */
    @ExcelProperty(value = "血型")
    private String bloodType;
    /**
     * 户籍所在地
     */
    @ExcelProperty(value = "户籍所在地")
    private String domicile;
    /**
     * 政治面貌
     */
    @ExcelProperty(value = "政治面貌")
    private String politicalOutlook;
    /**
     * 入党时间
     */
    @ExcelProperty(value = "入党时间")
    private String timeToJoinTheParty;
    /**
     * 存档机构
     */
//    private String archivingOrganization;
    /**
     * 子女状态
     */
//    private String stateOfChildren;
    /**
     * 子女有无商业保险
     */
//    private String doChildrenHaveCommercialInsurance;
    /**
     * 有无违法违纪行为
     */
//    private String isThereAnyViolationOfLawOrDiscipline;
    /**
     * 有无重大病史
     */
    @ExcelProperty(value = "有无重大病史")
    private String areThereAnyMajorMedicalHistories;
    /**
     * QQ
     */
    @ExcelProperty(value = "QQ")
    private String qq;
    /**
     * 微信
     */
    @ExcelProperty(value = "微信")
    private String wechat;
    /**
     * 居住证城市
     */
//    private String residenceCardCity;
    /**
     * 居住证办理日期
     */
//    private String dateOfResidencePermit;
    /**
     * 居住证截止日期
     */
//    private String residencePermitDeadline;
    /**
     * 现居住地
     */
    @ExcelProperty(value = "现居住地")
    private String placeOfResidence;
    /**
     * 详细地址
     */
    @ExcelProperty(value = "详细地址")
    private String detailAddress;
    /**
     * 联系手机
     */
    @ExcelProperty(value = "联系手机")
    private String contactTheMobilePhone;
    /**
     * 个人邮箱
     */
    @ExcelProperty(value = "个人邮箱")
    private String personalMailbox;
    /**
     * 紧急联系人
     */
    @ExcelProperty(value = "紧急联系人")
    private String emergencyContact;
    /**
     * 紧急联系电话
     */
    @ExcelProperty(value = "紧急联系电话")
    private String emergencyContactNumber;
    /**
     * 社保账号
     */
    @ExcelProperty(value = "社保账号")
    private String socialSecurityComputerNumber;
    /**
     * 公积金账号
     */
    @ExcelProperty(value = "公积金账号")
    private String providentFundAccount;
    /**
     * 银行卡号
     */
    @ExcelProperty(value = "银行卡号")
    private String bankCardNumber;
    /**
     * 开户行
     */
    @ExcelProperty(value = "开户行")
    private String openingBank;
    /**
     * 学历类型
     */
    @ExcelProperty(value = "学历类型")
    private String educationalType;
    /**
     * 毕业学校
     */
    @ExcelProperty(value = "毕业学校")
    private String graduateSchool;
    /**
     * 入学时间
     */
    @ExcelProperty(value = "入学时间")
    private String enrolmentTime;
    /**
     * 毕业时间
     */
    @ExcelProperty(value = "毕业时间")
    private String graduationTime;
    /**
     * 专业
     */
    @ExcelProperty(value = "专业")
    private String major;
    /**
     * 毕业证书
     */
    private String graduationCertificate;
    /**
     * 学位证书
     */
    private String certificateOfAcademicDegree;
    /**
     * 上家公司
     */
    @ExcelProperty(value = "上家公司")
    private String homeCompany;
    /**
     * 职称
     */
    @ExcelProperty(value = "职称")
    private String title;
    /**
     * 简历
     */
    private String resume;
    /**
     * 有无竞业限制
     */
    @ExcelProperty(value = "有无竞业限制")
    private String isThereAnyCompetitionRestriction;
    /**
     * 前公司离职证明
     */
    private String proofOfDepartureOfFormerCompany;
    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remarks;
}
