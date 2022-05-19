package com.hrm.social.service;


import com.hrm.domain.social.Archive;
import com.hrm.domain.social.SocialSecrityArchiveDetail;

import java.util.List;

public interface ArchiveService {


	/**
	 * 查询归档历史信息
	 *
	 * @param companyId
	 * @param yearMonth
	 * @return
	 */
	public Archive findArchive(String companyId, String yearMonth);

    /**
     * 根据归档历史id,查询归档明细
     *
     * @param id
     * @return
     */
    public List<SocialSecrityArchiveDetail> findAllDetailByArchiveId(String id);

    /**
     * 获取当月归档信息
     *
     * @param yearMonth
     * @param companyId
     * @return
     * @throws Exception
     */
    public List<SocialSecrityArchiveDetail> getReports(String yearMonth, String companyId) throws Exception;


	/**
	 * 月度员工社保数据归档
	 *
	 * @param yearMonth
	 * @param companyId
	 * @throws Exception
	 */
	public void archive(String yearMonth, String companyId) throws Exception;

	/**
	 * 某年的历史归档列表
	 *
	 * @param year
	 * @param companyId
	 * @return
	 */
	List<Archive> findArchiveByYear(String year, String companyId);

    /**
     * 根据用户id查询某年月的归档明细
     *
     * @param userId
     * @param yearMonth
     * @return
     */
    SocialSecrityArchiveDetail findUserArchiveDetail(String userId, String yearMonth);
}
