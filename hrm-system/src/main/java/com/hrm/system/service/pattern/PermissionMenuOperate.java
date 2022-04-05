package com.hrm.system.service.pattern;

import com.hrm.common.utils.BeanMapUtils;
import com.hrm.domain.system.PermissionMenu;
import com.hrm.system.dao.PermissionMenuDao;

import java.util.Map;

/**
 * 菜单权限的操作行为实现类
 *
 * @author LZL
 * @version v1.0
 * @date 2022/3/31-2:48
 */
public class PermissionMenuOperate extends AbstractPermissionOperateBehavior<PermissionMenuDao> {

    @Override
    public void permSaveOrUpdateBehavior(Map<String, Object> map, String id) throws Exception {
        PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
        permissionMenu.setId(id);
        dao.save(permissionMenu);
    }

    @Override
    public void permDeleteBehavior(String id) {
        dao.deleteById(id);
    }

    @Override
    public Object permFindBehavior(String id) {
        return dao.findById(id).get();
    }

    public PermissionMenuOperate(int type, PermissionMenuDao permissionMenuDao) {
        super(type, permissionMenuDao);
    }
}
