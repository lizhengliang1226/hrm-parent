package com.hrm.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.hrm.common.entity.ResultCode;
import com.hrm.common.exception.CommonException;
import com.hrm.common.service.BaseServiceImpl;
import com.hrm.domain.constant.SystemConstant;
import com.hrm.domain.system.Permission;
import com.hrm.domain.system.Role;
import com.hrm.system.dao.PermissionDao;
import com.hrm.system.dao.RoleDao;
import com.hrm.system.service.RoleService;
import com.lzl.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author LZL
 * @date 2022/3/9-1:07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends BaseServiceImpl<RoleDao, Role, String> implements RoleService {

    private RoleDao roleDao;
    private PermissionDao permissionDao;

    @Autowired
    public void setPermissionDao(PermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }


    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public void save(Role role) throws CommonException {
        final Role byName = roleDao.findByName(role.getName());
        if (byName == null) {
            String id = IdWorker.getIdStr();
            role.setId(id);
            roleDao.save(role);
        } else {
            throw new CommonException(ResultCode.DUPLICATE_ROLE_NAME);
        }

    }

    @Override
    public void update(Role role) {
        final Role role1 = roleDao.findById(role.getId()).get();
        role1.setName(role.getName());
        role1.setDescription(role.getDescription());
        roleDao.save(role1);
    }

    @Override
    public Page<Role> findSearch(Map<String, Object> map) {
        String page = String.valueOf(map.get("page"));
        String size = String.valueOf(map.get("size"));
        Specification<Role> specification = (root, criteriaQuery, criteriaBuilder) ->
                StrUtil.isNotEmpty((CharSequence) map.get("name")) ?
                        criteriaBuilder.like(root.get("name").as(String.class), "%" + map.get("name") + "%")
                        : null;
        return roleDao.findAll(specification.and(getSameCompanySpec(String.valueOf(map.get("companyId")))),
                PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size)));
    }

    @Override
    public List<Role> findAll(Map<String, Object> map) {
        return roleDao.findAll(getSameCompanySpec(String.valueOf(map.get("companyId"))));
    }


    @Override
    public void assignPerms(String id, List<String> permIds) {
        Role role = roleDao.findById(id).get();
        Set<Permission> perms = new HashSet<>();
        permIds.forEach(permId -> {
            // 可能是菜单。按钮，api权限中的一个，不知道
            Permission permission = permissionDao.findById(permId).get();
            //只有当权限是按钮的时候才能够查出来值，添加菜单权限的时候
            // 菜单下面是没有API类型的权限的，因此什么也查不到，不会影响
            final List<Permission> apiList = permissionDao.findByTypeAndPid(SystemConstant.PY_API, permission.getId());
            //添加api权限
            perms.addAll(apiList);
            //添加顶级权限,可能是按钮，也可能是菜单
            perms.add(permission);
        });
        // 权限信息发生改变，在保存时jpa会自动级联更新角色权限表
        role.setPermissions(perms);
        roleDao.save(role);
    }
}
