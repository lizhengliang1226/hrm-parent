package com.hrm.domain.attendance.bo;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class DaysMonthlyBO {


    @Id
    private String day;
}
