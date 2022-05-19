package com.hrm.attendance.dao;

import com.hrm.domain.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AttendanceDao extends CrudRepository<Attendance, String>, JpaRepository<Attendance, String>, JpaSpecificationExecutor<Attendance> {
    /**
     * 根据用户id和考勤日期查询考勤记录
     *
     * @param id
     * @param day
     * @return
     */
    Attendance findByUserIdAndDay(String id, String day);

    /**
     * 查询某年某个月的某人的所有考勤记录
     *
     * @param start
     * @param userId
     * @param end
     * @return
     */
    @Query(value = "select * " +
            "from atte_attendance force index(atte_user_id_index) " +
            "where user_id=?1 and `day` between ?2 and ?3  ", nativeQuery = true)
    List<Attendance> findByUserIdAndDayBetween(String userId, String start, String end);

    /**
     * 查询考勤天数
     *
     * @param id
     * @param s
     * @return
     */
    @Query(value = "SELECT COUNT(*) at0," +
            "       COUNT(CASE WHEN adt_status=1 THEN 1 END) at1," +
            "       COUNT(CASE WHEN adt_status=2 THEN 1 END) at2," +
            "       COUNT(CASE WHEN adt_status=3 THEN 1 END) at3," +
            "       COUNT(CASE WHEN adt_status=4 THEN 1 END) at4," +
            "       COUNT(CASE WHEN adt_status=8 THEN 1 END) at8," +
            "       COUNT(CASE WHEN adt_status=16 THEN 1 END) at16" +

            "       FROM atte_attendance WHERE  user_id=?1 AND DAY LIKE ?2", nativeQuery = true)
    Map<String, Object> statisticalByUser(String id, String s);

    /**
     * 查询某个月的每个用户的考勤天数
     *
     * @param companyId
     * @param atteDate
     * @return
     */
    @Query(value = "select count(case  when adt_status=1 or adt_status=6 or adt_status=16 or adt_status=21 then 1 end )" +
            " from atte_attendance aa where `day` like ?2 and company_id=?1 group by user_id", nativeQuery = true)
    List<Integer> getFullAtteNumber(String companyId, String atteDate);

    /**
     * 查询某个月的每个企业部门用户的考勤天数
     *
     * @param companyId
     * @param atteDate
     * @param departmentId
     * @return
     */
    @Query(value = "select count(case  when adt_status=1 or adt_status=6 or adt_status=16 or adt_status=21 then 1 end )" +
            " from atte_attendance aa where `day` like ?2 and company_id=?1 and department_id=?3 group by user_id", nativeQuery = true)
    List<Integer> getDeptFullAtteNumber(String companyId, String atteDate, String departmentId);


    /**
     * 查询某个月的每个企业部门用户的未打卡人数
     *
     * @param companyId
     * @param atteDate
     * @param departmentId
     * @return
     */
    @Query(value = "select count(*)from( select count(case when adt_status = 23 then 1 end ) no_clock_num " +
            "from atte_attendance aa where `day` like ?2 and company_id = ?1 and department_id = ?3 " +
            "group by user_id having no_clock_num >= 1)a;", nativeQuery = true)
    Integer getNoClockNum(String companyId, String atteDate, String departmentId);

    /**
     * 查询某企业某部门全勤人数
     *
     * @param companyId
     * @param departmentId
     * @param date
     * @return
     */
    @Query(value = "select count(*) from" +
            "( select count(case when adt_status = 22 or adt_status = 2 or adt_status = 4 or adt_status=3 then 1 end ) cn_a" +
            "  from atte_attendance aa where `day` like ?3 and company_id = ?1 and department_id=?2 " +
            "group by user_id having cn_a<1" +
            ")a", nativeQuery = true)
    Integer getDeptFullAtteNum(String companyId, String departmentId, String date);

    /**
     * 查询某企业全勤人数
     *
     * @param companyId
     * @param date
     * @return
     */
    @Query(value = "select count(*) from" +
            "( select count(case when adt_status != 22 and adt_status != 2 and adt_status != 4 and adt_status!=3 then 1 end )" +
            "  from atte_attendance aa where `day` like ?2 and company_id = ?1  group by user_id)a", nativeQuery = true)
    Integer getCompanyFullAtteNum(String companyId, String date);
}
