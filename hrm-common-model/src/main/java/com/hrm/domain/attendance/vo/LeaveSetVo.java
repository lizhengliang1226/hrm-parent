package com.hrm.domain.attendance.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请假设置VO
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/17-11:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveSetVo {
    private String leaveType;
    private String name;
    private Integer isEnable;
    private String departmentId;
}
