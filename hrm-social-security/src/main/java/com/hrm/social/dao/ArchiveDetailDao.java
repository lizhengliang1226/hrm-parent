package com.hrm.social.dao;


import com.hrm.domain.social.SocialSecrityArchiveDetail;
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
public interface ArchiveDetailDao extends JpaRepository<SocialSecrityArchiveDetail, String>, JpaSpecificationExecutor<SocialSecrityArchiveDetail> {
    /**
     * 根据归档id删除对应的明细
     *
     * @param archiveId
     */
    @Modifying
    @Query(value = "delete from SocialSecrityArchiveDetail a where a.archiveId=?1")
    void deleteByArchiveId(String archiveId);

    /**
     * 查询归档明细通过归档id
     *
     * @param archiveId
     * @return
     */
    List<SocialSecrityArchiveDetail> findByArchiveId(String archiveId);

    /**
     * 根据用户id和年月查询归档明细
     *
     * @param userId
     * @param yearsMonth
     * @return
     */
    SocialSecrityArchiveDetail findByUserIdAndYearsMonth(String userId, String yearsMonth);
}
