package com.hrm.system.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import com.hrm.common.entity.UserLevel;
import com.hrm.common.utils.IdWorker;
import com.hrm.domain.system.Role;
import com.hrm.domain.system.User;
import com.hrm.system.dao.RoleDao;
import com.hrm.system.dao.UserDao;
import com.hrm.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/8-20:34
 */
@Service
public class UserServiceImpl implements UserService {
    private static final String COMPANY_ID = "companyId";
    private static final String DEPARTMENT_ID = "departmentId";
    private static final String HAS_DEPT = "hasDept";
    private static final String IN_SERVICE_STATUS = "inServiceStatus";
    private static final String FIND_ALL_FLAG = "3";
    @Value("${initial-password}")
    private String initialPassword;
    private IdWorker idWorker;
    private UserDao userDao;
    private RoleDao roleDao;

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setIdWorker(IdWorker idWorker) {
        this.idWorker = idWorker;
    }

    @Override
    public void save(User user) {
        String id = idWorker.nextId() + "";
        user.setId(id);
        user.setCreateTime(new Date());
        user.setEnableState(1);
        //默认在职
        user.setInServiceStatus(1);
        //默认级别为普通用户
        user.setLevel(UserLevel.NORMAL_USER);
        DES des = SecureUtil.des(user.getMobile().getBytes(StandardCharsets.UTF_8));
        final String password = des.encryptHex(initialPassword);
        user.setPassword(password);
        userDao.save(user);
    }

    @Override
    public void update(User user) {
        User user1 = userDao.findById(user.getId()).get();
        // 设置用户的其他信息
        user1.setWorkNumber(user.getWorkNumber());
        user1.setUsername(user.getUsername());
        final String oldMobile = user1.getMobile();
        final String newMobile = user.getMobile();
        // 如果手机号发生了改变需要重新加密密码
        if (!oldMobile.trim().equals(newMobile.trim())) {
            final String pass = user1.getPassword();
            final String oldPass = SecureUtil.des(oldMobile.getBytes(StandardCharsets.UTF_8)).decryptStr(pass);
            final String newPass = SecureUtil.des(newMobile.getBytes(StandardCharsets.UTF_8)).encryptHex(oldPass);
            user1.setPassword(newPass);
        }
        user1.setMobile(newMobile);
        user1.setStaffPhoto(user.getStaffPhoto());
        user1.setDepartmentId(user.getDepartmentId());
        user1.setDepartmentName(user.getDepartmentName());
        user1.setFormOfEmployment(user.getFormOfEmployment());
        user1.setTimeOfEntry(user.getTimeOfEntry());
        userDao.save(user1);
    }

    @Override
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    @Override
    public User findByMobile(String mobile) {
        return userDao.findByMobile(mobile);
    }

    @Override
    public Page<User> findAll(Map<String, Object> map) {
        String page = String.valueOf(map.get("page"));
        String size = String.valueOf(map.get("size"));
        Specification<User> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>(10);
            map.forEach((k, v) -> {
                if (COMPANY_ID.equals(k)) {
                    list.add(criteriaBuilder.equal(root.get(COMPANY_ID).as(String.class), v));
                }
                if (DEPARTMENT_ID.equals(k)) {
                    list.add(criteriaBuilder.equal(root.get(DEPARTMENT_ID).as(String.class), v));
                }
                if (HAS_DEPT.equals(k)) {
                    if ("0".equals(v)) {
                        list.add(criteriaBuilder.isNull(root.get(DEPARTMENT_ID)));
                    } else {
                        list.add(criteriaBuilder.isNotNull(root.get(DEPARTMENT_ID)));
                    }
                }
                if (IN_SERVICE_STATUS.equals(k)) {
                    if (!FIND_ALL_FLAG.equals(v)) {
                        list.add(criteriaBuilder.equal(root.get(IN_SERVICE_STATUS).as(String.class), v));
                    }
                }
            });
            return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
        };
        return userDao.findAll(specification, PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size)));
    }

    @Override
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    @Override
    public void assignRoles(String id, List<String> roles) {
        User user = userDao.findById(id).get();
        Set<Role> roleSet = new HashSet<>();
        roles.forEach(roleId -> {
            final Role role = roleDao.findById(roleId).get();
            roleSet.add(role);
        });
        user.setRoles(roleSet);
        userDao.save(user);
    }

    @Override
    public void updatePassword(String id, String password) {
        User user1 = userDao.findById(id).get();
        final DES des = SecureUtil.des(user1.getMobile().getBytes(StandardCharsets.UTF_8));
        final String s = des.encryptHex(password);
        userDao.updatePassword(id, s);
    }


}
