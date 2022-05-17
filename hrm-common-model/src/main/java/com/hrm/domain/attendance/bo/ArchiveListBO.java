package com.hrm.domain.attendance.bo;


import com.hrm.domain.attendance.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
public class ArchiveListBO extends BaseEntity implements Serializable {

    @Id
    private String id;


    /**
     * 总人数
     */
    private String totalPeople;

    /**
     * 全勤认数
     */
    private String fullAttendancePeople;


    /**
     * 月份
     */
    private String month;


}
