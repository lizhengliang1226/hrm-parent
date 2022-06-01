package com.hrm.domain.social.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户社保信息vo
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/31-6:18
 */
@Data
public class UserSocialSecuritySimpleVo {
    private String id;
    private String username;
    private String mobile;
    private String workNumber;
    private String departmentName;
    private Date timeOfEntry;
    private String participatingInTheCity;
    private String providentFundCity;
    private BigDecimal socialSecurityBase;
    private BigDecimal providentFundBase;
}
