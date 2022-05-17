package com.hrm.domain.attendance.vo;


import com.hrm.domain.attendance.entity.DayOffConfig;
import com.hrm.domain.attendance.entity.ExtraDutyConfig;
import com.hrm.domain.attendance.entity.ExtraDutyRule;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 加班设置响应VO
 *
 * @author 17314
 */
@Data
public class ExtWorkVO implements Serializable {

    /**
     * 加班配置
     */
    private ExtraDutyConfig extraDutyConfig;

    /**
     * 加班规则
     */
    private List<ExtraDutyRule> extraDutyRuleList;

    /**
     * 调休配置
     */
    private DayOffConfig dayOffConfigs;


}
