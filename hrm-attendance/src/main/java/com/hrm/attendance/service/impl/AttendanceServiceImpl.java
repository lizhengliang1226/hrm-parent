package com.hrm.attendance.service.impl;


import cn.hutool.core.date.DatePattern;
import com.hrm.attendance.dao.*;
import com.hrm.attendance.service.AttendanceService;
import com.hrm.common.entity.PageResult;
import com.hrm.common.utils.DateUtils;
import com.hrm.domain.attendance.bo.AtteItemBO;
import com.hrm.domain.attendance.entity.ArchiveMonthlyInfo;
import com.hrm.domain.attendance.entity.Attendance;
import com.hrm.domain.attendance.entity.AttendanceCompanySettings;
import com.hrm.domain.attendance.entity.User;
import com.lzl.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考勤服务
 *
 * @author 17314
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttendanceServiceImpl implements AttendanceService {

	@Autowired
	private AttendanceDao attendanceDao;

	@Autowired
	private DeductionDictDao deductionDictDao;
	@Autowired
	private AttendanceCompanySettingsDao attendanceCompanySettingsDao;
	@Autowired
	private AttendanceConfigDao attendanceConfigDao;
	@Autowired
	private UserDao userDao;

	@Override
	public Map getAtteDate(String companyId, int page, int pageSize) throws ParseException {
		// 获取要查询哪个月的考勤记录
		final AttendanceCompanySettings data = attendanceCompanySettingsDao.findById(companyId).get();
		final String dataMonth = data.getDataMonth();
		// 获取该企业的所有用户
		final Page<User> users = userDao.findAll((root, criteriaQuery, criteriaBuilder) ->
														 criteriaBuilder.equal(root.get("companyId").as(String.class), companyId)
				, PageRequest.of(page - 1, pageSize));
		List<AtteItemBO> list = new ArrayList<>(16);
		users.getContent().forEach(u -> {
			// 遍历用户查询出所有用户在当月的考勤记录
			final AtteItemBO atteItemBO = new AtteItemBO();
			// 构建基础信息
			BeanUtils.copyProperties(u, atteItemBO);
			// 获取查询月有多少天
			String end = DateUtils.getMonthDays(dataMonth, DatePattern.SIMPLE_MONTH_PATTERN) + "";
			String endTime = dataMonth + end;
			String startTime = dataMonth + "01";
			// 查询出某个用户当月的考勤记录
			final List<Attendance> adList = attendanceDao.findByUserIdAndDayBetween(u.getId(), startTime, endTime);
			atteItemBO.setAttendanceRecord(adList);
			list.add(atteItemBO);
		});
		final PageResult<AtteItemBO> listPageResult = new PageResult<>();
		listPageResult.setTotal(users.getTotalElements());
		listPageResult.setRows(list);
		Map map = new HashMap();
		map.put("data", listPageResult);
		map.put("tobeTaskCount", 0);
		map.put("monthOfReport", dataMonth.substring(4));
		return map;
	}


	@Override
	public void editAtte(Attendance attendance) {
		//1.查询考勤是否存在,更新
		Attendance vo = attendanceDao.findByUserIdAndDay(attendance.getUserId(), attendance.getDay());
		//2.如果不存在,设置对象id,保存
		if (vo == null) {
			attendance.setId(IdWorker.getIdStr());
		} else {
			attendance.setId(vo.getId());
		}
		attendanceDao.save(attendance);
	}


	@Override
	public List<ArchiveMonthlyInfo> getReports(String atteDate, String companyId) {
		// 查询所有企业用户
		List<User> users = userDao.findByCompanyId(companyId);
		//2.循环遍历用户列表,统计每个用户当月的考勤记录
		List<ArchiveMonthlyInfo> list = new ArrayList<>();
		for (User user : users) {
			ArchiveMonthlyInfo info = new ArchiveMonthlyInfo(user);
			//统计每个用户的考勤记录
			Map map = attendanceDao.statisticalByUser(user.getId(), atteDate + "%");
			info.setStatisData(map);
			list.add(info);
		}
		return list;
	}
}
