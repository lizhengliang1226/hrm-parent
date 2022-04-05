package com.hrm.system.service.impl;

import com.hrm.common.service.BaseSpecService;
import com.hrm.common.utils.BeanMapUtils;
import com.hrm.domain.constant.SystemConstant;
import com.hrm.domain.system.Permission;
import com.hrm.system.dao.PermissionApiDao;
import com.hrm.system.dao.PermissionDao;
import com.hrm.system.dao.PermissionMenuDao;
import com.hrm.system.dao.PermissionPointDao;
import com.hrm.system.service.PermissionService;
import com.hrm.system.service.pattern.AbstractPermissionOperateBehavior;
import com.hrm.system.service.pattern.PermissionApiOperate;
import com.hrm.system.service.pattern.PermissionMenuOperate;
import com.hrm.system.service.pattern.PermissionPointOperate;
import com.lzl.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author LZL
 * @date 2022/3/10-6:07
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class PermissionServiceImpl extends BaseSpecService<Permission> implements PermissionService {
    private static final long serialVersionUID = 1L;
    private PermissionDao permissionDao;
    private PermissionApiDao permissionApiDao;
    private PermissionMenuDao permissionMenuDao;
    private PermissionPointDao permissionPointDao;

    @Override
    public void save(Map<String, Object> map) throws Exception {
        String id = IdWorker.getIdStr();
        //构造顶级权限对象
        Permission permission = BeanMapUtils.mapToBean(map, Permission.class);
        permission.setId(id);
        final Integer type = permission.getType();
        //在责任链模式中根据类型判断保存的哪类权限，保存权限详细信息
        final AbstractPermissionOperateBehavior<PermissionMenuDao> chain = getChainOfPermissionOperate();
        chain.permOperate(type, permOperate -> permOperate.permSaveOrUpdateBehavior(map, id));
        permissionDao.save(permission);
    }

    @Override
    public void update(Map<String, Object> map) throws Exception {
        //构造不同的权限对象
        Permission permission = BeanMapUtils.mapToBean(map, Permission.class);
        Permission permission1 = permissionDao.findById(permission.getId()).get();
        permission1.setCode(permission.getCode());
        permission1.setName(permission.getName());
        permission1.setDescription(permission.getDescription());
        permission1.setPid(permission.getPid());
        permission1.setEnVisible(permission.getEnVisible());
        final Integer type = permission.getType();
        final AbstractPermissionOperateBehavior<PermissionMenuDao> chain = getChainOfPermissionOperate();
        chain.permOperate(type, permOperate -> permOperate.permSaveOrUpdateBehavior(map, permission.getId()));
        permissionDao.save(permission1);
    }

    @Override
    public Map<String, Object> findById(String id) throws Exception {
        Permission permission = permissionDao.findById(id).get();
        AtomicReference<Object> obj = new AtomicReference<>();
        final AbstractPermissionOperateBehavior<PermissionMenuDao> chain = getChainOfPermissionOperate();
        chain.permOperate(permission.getType(), permOperate -> obj.set(permOperate.permFindBehavior(permission.getId())));
        final Map<String, Object> map1 = BeanMapUtils.beanToMap(obj.get());
        map1.put("name", permission.getName());
        map1.put("type", permission.getType());
        map1.put("code", permission.getCode());
        map1.put("description", permission.getDescription());
        map1.put("pid", permission.getPid());
        map1.put("enVisible", permission.getEnVisible());
        return map1;
    }

    /**
     * type: 0:查询菜单，按钮权限 1：菜单 2：按钮 3：api
     * enVisible 0:查询admin权限 1：查询企业权限 null：查询以上两个权限
     * pid： 父权限id
     *
     * @param map
     * @return
     */
    @Override
    public List<Permission> findAll(Map<String, Object> map) {
        Specification<Permission> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>(10);
            map.forEach((k, v) -> {
                if (SystemConstant.PID.equals(k)) {
                    list.add(criteriaBuilder.equal(root.get("pid").as(String.class), v));
                }
                if (SystemConstant.EN_VISIBLE.equals(k)) {
                    list.add(criteriaBuilder.equal(root.get("enVisible").as(String.class), v));
                }
                if (SystemConstant.TYPE.equals(k)) {
                    final CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("type"));
                    if (SystemConstant.PERM_QUERY_FLAG.equals(v)) {
                        in.value(1).value(2);
                    } else {
                        in.value(Integer.parseInt((String) v));
                    }
                    list.add(in);
                }

            });
            return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
        };
        return permissionDao.findAll(specification);
    }

    @Override
    public void deleteById(String id) throws Exception {
        Permission permission = permissionDao.findById(id).get();
        final Integer type = permission.getType();
        final AbstractPermissionOperateBehavior<PermissionMenuDao> chain = getChainOfPermissionOperate();
        chain.permOperate(type, permOperate -> permOperate.permDeleteBehavior(id));
        permissionDao.deleteById(id);
    }

    /**
     * 创建责任链的方法
     *
     * @return 责任链的头节点
     */
    private AbstractPermissionOperateBehavior<PermissionMenuDao> getChainOfPermissionOperate() {
        AbstractPermissionOperateBehavior<PermissionApiDao> permissionApiOperate = new PermissionApiOperate(SystemConstant.PY_API, permissionApiDao);
        AbstractPermissionOperateBehavior<PermissionMenuDao> permissionMenuOperate = new PermissionMenuOperate(SystemConstant.PY_MENU, permissionMenuDao);
        AbstractPermissionOperateBehavior<PermissionPointDao> permissionPointOperate = new PermissionPointOperate(SystemConstant.PY_POINT, permissionPointDao);
        permissionMenuOperate.setNext(permissionPointOperate);
        permissionPointOperate.setNext(permissionApiOperate);
        return permissionMenuOperate;
    }

    @Autowired
    public void setPermissionApiDao(PermissionApiDao permissionApiDao) {
        this.permissionApiDao = permissionApiDao;
    }

    @Autowired
    public void setPermissionMenuDao(PermissionMenuDao permissionMenuDao) {
        this.permissionMenuDao = permissionMenuDao;
    }

    @Autowired
    public void setPermissionPointDao(PermissionPointDao permissionPointDao) {
        this.permissionPointDao = permissionPointDao;
    }

    @Autowired
    public void setPermissionDao(PermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

}
