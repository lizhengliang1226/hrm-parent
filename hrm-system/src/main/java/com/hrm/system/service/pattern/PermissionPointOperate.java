package com.hrm.system.service.pattern;

import com.hrm.common.utils.BeanMapUtils;
import com.hrm.domain.system.PermissionPoint;
import com.hrm.system.dao.PermissionPointDao;

import java.util.Map;

/**
 * 权限按钮操作行为实现类
 *
 * @author LZL
 * @version v1.0
 * @date 2022/3/31-2:55
 */
public class PermissionPointOperate extends AbstractPermissionOperateBehavior<PermissionPointDao> {

    public PermissionPointOperate(int type, PermissionPointDao permissionPointDao) {
        super(type, permissionPointDao);
    }

    @Override
    public void permSaveOrUpdateBehavior(Map<String, Object> map, String id) throws Exception {
        PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map, PermissionPoint.class);
        permissionPoint.setId(id);
        dao.save(permissionPoint);
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
