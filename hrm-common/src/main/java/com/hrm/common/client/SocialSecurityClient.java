package com.hrm.common.client;

import com.hrm.common.entity.Result;
import com.hrm.domain.social.UserSocialSecurity;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * 社保微服务远程调用
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/19-14:37
 */
@FeignClient(name = "hrm-social-security")
public interface SocialSecurityClient {

    @PutMapping(value = "social/{id}")
    @ApiOperation(value = "保存或更新用户社保数据")
    public Result saveUserSocialInfo(@RequestBody UserSocialSecurity userSocialInfo);

    @GetMapping(value = "social/{id}")
    @ApiOperation(value = "查询用户社保数据")
    public Result<Map<String, Object>> findUserSocialInfo(@PathVariable(value = "id") String userId);

    @GetMapping(value = "social/historys/data")
    @ApiOperation(value = "根据用户id和年月查询归档明细")
    public Result userHistoryArchiveData(@RequestBody Map<String, String> map) throws Exception;

}
