package com.hrm.system.service.impl;

import com.hrm.common.entity.ResultCode;
import com.hrm.common.exception.CommonException;
import com.hrm.common.utils.BeanMapUtils;
import com.hrm.common.utils.IdWorker;
import com.hrm.common.utils.PermissionConstants;
import com.hrm.domain.system.Permission;
import com.hrm.domain.system.PermissionApi;
import com.hrm.domain.system.PermissionMenu;
import com.hrm.domain.system.PermissionPoint;
import com.hrm.system.dao.PermissionApiDao;
import com.hrm.system.dao.PermissionDao;
import com.hrm.system.dao.PermissionMenuDao;
import com.hrm.system.dao.PermissionPointDao;
import com.hrm.system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/10-6:07
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class PermissionServiceImpl implements PermissionService {
    private static final long serialVersionUID = 1L;
    private static final String PID = "pid";
    private static final String EN_VISIBLE = "enVisible";
    private static final String TYPE = "type";
    private static final String QUERY_FLAG = "0";

    private IdWorker idWorker;
    private PermissionDao permissionDao;
    private PermissionApiDao permissionApiDao;
    private PermissionMenuDao permissionMenuDao;
    private PermissionPointDao permissionPointDao;

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

    @Autowired
    public void setIdWorker(IdWorker idWorker) {
        this.idWorker = idWorker;
    }

    @Override
    public void save(Map<String, Object> map) throws Exception {
        String id = idWorker.nextId() + "";
        //构造不同的权限对象
        Permission permission = BeanMapUtils.mapToBean(map, Permission.class);
        permission.setId(id);
        final Integer type = permission.getType();
        //保存时判断类型的目的是构建权限的详细信息，这些信息保存在另外三张表里
        switch (type) {
            case PermissionConstants.PY_MENU:
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                permissionMenu.setId(id);
                permissionMenuDao.save(permissionMenu);
                break;
            case PermissionConstants.PY_API:
                PermissionApi permissionApi = BeanMapUtils.mapToBean(map, PermissionApi.class);
                permissionApi.setId(id);
                permissionApiDao.save(permissionApi);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                permissionPoint.setId(id);
                permissionPointDao.save(permissionPoint);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
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
        switch (type) {
            case PermissionConstants.PY_MENU:
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                permissionMenu.setId(permission.getId());
                permissionMenuDao.save(permissionMenu);
                break;
            case PermissionConstants.PY_API:
                PermissionApi permissionApi = BeanMapUtils.mapToBean(map, PermissionApi.class);
                permissionApi.setId(permission.getId());
                permissionApiDao.save(permissionApi);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                permissionPoint.setId(permission.getId());
                permissionPointDao.save(permissionPoint);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
        permissionDao.save(permission1);
    }

    @Override
    public Map findById(String id) throws CommonException {
        Permission permission = permissionDao.findById(id).get();
        Object obj;
        if (permission.getType() == PermissionConstants.PY_MENU) {
            obj = permissionMenuDao.findById(id).get();

        } else if (permission.getType() == PermissionConstants.PY_POINT) {
            obj = permissionPointDao.findById(id).get();

        } else if (permission.getType() == PermissionConstants.PY_API) {
            obj = permissionApiDao.findById(id).get();

        } else {
            throw new CommonException(ResultCode.FAIL);
        }
        final Map<String, Object> map1 = BeanMapUtils.beanToMap(obj);
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
                if (PID.equals(k)) {
                    list.add(criteriaBuilder.equal(root.get(PID).as(String.class), v));
                }
                if (EN_VISIBLE.equals(k)) {
                    list.add(criteriaBuilder.equal(root.get(EN_VISIBLE).as(String.class), v));
                }
                if (TYPE.equals(k)) {
                    final CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get(TYPE));
                    if (QUERY_FLAG.equals(v)) {
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
    public void deleteById(String id) throws CommonException {
        Permission permission = permissionDao.findById(id).get();
        final Integer type = permission.getType();
        switch (type) {
            case PermissionConstants.PY_MENU:
                permissionMenuDao.deleteById(id);
                break;
            case PermissionConstants.PY_API:
                permissionApiDao.deleteById(id);
                break;
            case PermissionConstants.PY_POINT:
                permissionPointDao.deleteById(id);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
        permissionDao.deleteById(id);
    }
}
