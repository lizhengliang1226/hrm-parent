package com.hrm.system.service.pattern;

import com.hrm.common.utils.BeanMapUtils;
import com.hrm.domain.system.PermissionApi;
import com.hrm.system.dao.PermissionApiDao;

import java.util.Map;

/**
 * 权限API操作行为实现类
 *
 * @author LZL
 * @version v1.0
 * @date 2022/3/31-2:57
 */
public class PermissionApiOperate extends AbstractPermissionOperateBehavior<PermissionApiDao> {

    public PermissionApiOperate(int type, PermissionApiDao permissionApiDao) {
        super(type, permissionApiDao);
    }

    @Override
    public void permSaveOrUpdateBehavior(Map<String, Object> map, String id) throws Exception {
        PermissionApi permissionApi = BeanMapUtils.mapToBean(map, PermissionApi.class);
        permissionApi.setId(id);
        dao.save(permissionApi);
    }

    @Override
    public void permDeleteBehavior(String id) {
        dao.deleteById(id);
    }

    @Override
    public Object permFindBehavior(String id) {
        return dao.findById(id).get();
    }
}
