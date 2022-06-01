package com.hrm.salary.mapper;

import com.hrm.domain.salary.vo.SalaryItemVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 薪资mapper
 *
 * @author LZL
 * @version v1.0
 * @date 2022/6/1-11:11
 */
@Repository
public interface SalaryMapper {
    /**
     * 查询薪资列表
     *
     * @param map
     * @return
     */
    List<SalaryItemVo> findSalaryList(Map map);

    /**
     * 薪资列表记录数
     *
     * @param map
     * @return
     */
    Integer countOfSalaryList(Map map);
}
