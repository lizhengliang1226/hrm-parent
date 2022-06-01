package com.hrm.salary.dao;


import com.hrm.domain.salary.SalaryArchiveDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 自定义dao接口继承
 * JpaRepository<实体类，主键>
 * JpaSpecificationExecutor<实体类>
 */
public interface SalaryArchiveDetailDao extends JpaRepository<SalaryArchiveDetail, String>, JpaSpecificationExecutor<SalaryArchiveDetail> {
    /**
     * 根据主档id查询所有的子档数据
     *
     * @param id
     * @return
     */
    List<SalaryArchiveDetail> findByArchiveId(String id);

    /**
     * 根据主档id删除所有子档数据
     *
     * @param id
     */
    @Modifying
    @Query("delete from SalaryArchiveDetail where archiveId=?1")
    void deleteByArchiveId(String id);
}
