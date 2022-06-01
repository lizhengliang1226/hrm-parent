package com.hrm.domain.attendance.bo;


import com.hrm.domain.attendance.entity.Attendance;
import lombok.Data;

import java.util.List;

/**
 * 考勤项bo，记录员工的考勤记录
 *
 * @author 17314
 */
@Data
public class AtteItemBO {

    //编号
    private String id;
    //名称
    private String username;
    //工号
    private String workNumber;
    //部门
    private String departmentName;
    //手机
    private String mobile;
    //考勤记录
    private List<Attendance> attendanceRecord;
    //部门ID
    private String departmentId;

}
