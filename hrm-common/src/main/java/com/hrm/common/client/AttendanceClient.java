package com.hrm.common.client;

import com.hrm.common.entity.Result;
import com.hrm.domain.attendance.entity.DeductionDict;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 考勤微服务远程调用
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/19-17:12
 */
@FeignClient(name = "hrm-attendance")
public interface AttendanceClient {
    @GetMapping("attendances/archive/{userId}/{yearMonth}")
    @ApiOperation(value = "根据用户id和月份查询考勤明细")
    public Result userAtteHistoryArchiveDetailData(@PathVariable String userId, @PathVariable String yearMonth) throws Exception;

    @GetMapping(value = "cfg/deductions/setList")
    @ApiOperation(value = "查询企业扣款设置")
    public Result<List<DeductionDict>> findCompanyDeductionsList();
}
