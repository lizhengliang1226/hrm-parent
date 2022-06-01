package com.hrm.social.mapper;

import com.hrm.domain.social.vo.UserSocialSecuritySimpleVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 用户社保信息接口
 *
 * @author LZL
 * @version v1.0
 * @date 2022/5/31-6:16
 */
@Repository
public interface UserSocialSecurityMapper {
    /**
     * 条件查询社保基础信息构建社保列表
     *
     * @param map
     * @return
     */
    public List<UserSocialSecuritySimpleVo> findByConditions(Map map);

    /**
     * 计算记录数
     *
     * @param map
     * @return
     */
    Integer count(Map map);
}
