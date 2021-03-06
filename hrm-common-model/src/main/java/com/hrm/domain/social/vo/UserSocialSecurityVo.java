package com.hrm.domain.social.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户社保vo
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/31-1:53
 */
@Data
@ExcelIgnoreUnannotated
public class UserSocialSecurityVo {

    //    @ExcelProperty(value = "ID")
    private String userId;

    @ExcelProperty(value = "姓名")
    private String username;
    @ExcelProperty(value = "手机号")
    private String mobile;
    /**
     * 参保城市id
     */
    private String participatingInTheCityId;
    /**
     * 社保基数
     */
    @ExcelProperty(value = "社保基数")
    private BigDecimal socialSecurityBase;

    /**
     * 工伤比例
     */
    @ExcelProperty(value = "工伤比例")
    private BigDecimal industrialInjuryRatio;


    /**
     * 公积金城市id
     */
    private String providentFundCityId;
    /**
     * 公积金基数
     */
    @ExcelProperty(value = "公积金基数")
    private BigDecimal providentFundBase;
    /**
     * 公积金企业比例
     */
    @ExcelProperty(value = "公积金企业比例")
    private BigDecimal enterpriseProportion;
    /**
     * 公积金个人比例
     */
    @ExcelProperty(value = "公积金个人比例")
    private BigDecimal personalProportion;
    /**
     * 户籍所在地
     */
//    private String householdRegistration;
    /**
     * 社保城市
     */
    @ExcelProperty(value = "社保城市")
    private String participatingInTheCity;
    /**
     * 公积金城市
     */
    @ExcelProperty(value = "公积金城市")
    private String providentFundCity;
    /**
     * 参保类型  1为首次开户 2为非首次开户
     */
    @ExcelProperty(value = "参保类型")
    private String socialSecurityType;

    /**
     * 户籍类型 1为本市城镇 2为本市农村 3为外埠城镇 4为外埠农村
     */
    @ExcelProperty(value = "户籍类型")
    private String householdRegistrationType;
}
