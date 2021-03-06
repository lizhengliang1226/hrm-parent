package com.hrm.system.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import com.hrm.common.cache.SystemCache;
import com.hrm.common.entity.ResultCode;
import com.hrm.common.exception.CommonException;
import com.hrm.common.service.BaseServiceImpl;
import com.hrm.domain.constant.SystemConstant;
import com.hrm.domain.system.Role;
import com.hrm.domain.system.User;
import com.hrm.system.dao.RoleDao;
import com.hrm.system.dao.UserDao;
import com.hrm.system.service.UserService;
import com.hrm.system.utils.TencentAiFaceUtil;
import com.lzl.IdWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 用户服务
 *
 * @author LZL
 * @date 2022/3/8-20:34
 */
@Service
@RefreshScope
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends BaseServiceImpl<UserDao, User, String> implements UserService {

    private static final String FIND_ALL_FLAG = "3";
    @Value("${initial-password}")
    private String initialPassword;
    private TencentAiFaceUtil tencentAiFaceUtil;
    private UserDao userDao;
    private RoleDao roleDao;
    @Value("${tencent-face.groupId}")
    private String groupId;
    private final ReentrantLock lock = new ReentrantLock();
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setTencentAiFaceUtil(TencentAiFaceUtil tencentAiFaceUtil) {
        this.tencentAiFaceUtil = tencentAiFaceUtil;
    }

    @Override
    public void save(User user) throws CommonException {
        lock.lock();
        try {
            String id = IdWorker.getIdStr();
            user.setId(id);
            user.setCreateTime(new Date());
            // 设置可用状态为可用
            user.setEnableState(1);
            //默认在职
            user.setInServiceStatus(1);
            //默认级别为普通用户
            user.setLevel(SystemConstant.NORMAL_USER);
            DES des = SecureUtil.des(user.getMobile().getBytes(StandardCharsets.UTF_8));
            final String password = des.encryptHex(initialPassword);
            user.setPassword(password);
            if (user.getStaffPhoto() != null && user.getStaffPhoto().length() > 0) {
                // 添加用户到人员库
                addUserToPersonnel(user);
            }
            userDao.save(user);
            final com.hrm.domain.attendance.entity.User user1 = new com.hrm.domain.attendance.entity.User();
            BeanUtils.copyProperties(user, user1);
            redisTemplate.boundHashOps(SystemConstant.REDIS_USER_LIST).put(id, user1);
            redisTemplate.boundHashOps(SystemConstant.REDIS_USER_LIST).put(user.getMobile(), user1);
        } finally {
            lock.unlock();
        }
    }



    /**
     * 添加用户到人员库
     *
     * @param user 用户
     * @throws CommonException 异常
     */
    private void addUserToPersonnel(User user) throws CommonException {
        // 添加用户到人员库
        final boolean addPerson = tencentAiFaceUtil.createPerson(
                groupId,
                user.getUsername(),
                user.getId(),
                Long.parseLong(user.getGender()),
                0L,
                user.getStaffPhoto());
        // 添加失败一定是因为没有人脸
        if (!addPerson) {
            throw new CommonException(ResultCode.IMG_NO_FACE);
        }
    }

    @Override
    public void update(User user) throws CommonException {
        User user1 = userDao.findById(user.getId()).get();
        final String oldMobile = user1.getMobile();
        final String newMobile = user.getMobile();
        int flag = 0;
        // 如果手机号发生了改变需要重新加密密码
        if (!oldMobile.trim().equals(newMobile.trim())) {
            final String pass = user1.getPassword();
            final String oldPass = SecureUtil.des(oldMobile.getBytes(StandardCharsets.UTF_8)).decryptStr(pass);
            final String newPass = SecureUtil.des(newMobile.getBytes(StandardCharsets.UTF_8)).encryptHex(oldPass);
            user1.setPassword(newPass);
            flag = 1;
        }
        // 有照片
        if (user.getStaffPhoto() != null && user.getStaffPhoto().length() > 0) {
            // 判断原来是不是添加过
            if (user1.getStaffPhoto() == null) {
                // 原来没有添加过，就新增人员
                addUserToPersonnel(user);
            } else if (!user.getStaffPhoto().equals(user1.getStaffPhoto())) {
                // 已添加过且人脸不同了，就新增人脸
                final boolean addFace = tencentAiFaceUtil.addFace(user1.getId(), user.getStaffPhoto());
                if (!addFace) {
                    throw new CommonException(ResultCode.ADD_FACE_FAIL);
                }
            }
        }
        // 设置用户的其他信息
        user1.setGender(user.getGender());
        user1.setMobile(newMobile);
        user1.setWorkNumber(user.getWorkNumber());
        user1.setUsername(user.getUsername());
        user1.setStaffPhoto(user.getStaffPhoto());
        user1.setDepartmentId(user.getDepartmentId());
        user1.setDepartmentName(user.getDepartmentName());
        user1.setFormOfEmployment(user.getFormOfEmployment());
        user1.setTimeOfEntry(user.getTimeOfEntry());
        user1.setWorkingCity(user.getWorkingCity());
        userDao.save(user1);
        if (flag == 1) {
            final com.hrm.domain.attendance.entity.User user2 = new com.hrm.domain.attendance.entity.User();
            BeanUtils.copyProperties(user1, user2);
            redisTemplate.boundHashOps(SystemConstant.REDIS_USER_LIST).put(user1.getId(), user2);
            redisTemplate.boundHashOps(SystemConstant.REDIS_USER_LIST).put(user1.getMobile(), user2);
            SystemCache.USER_INFO_CACHE.put(user1.getMobile(), user2);
        }
    }

    @Override
    public User findByMobile(String mobile) {
        return userDao.findByMobile(mobile);
    }

    @Override
    public Page<User> findAll(Map<String, Object> map) {
        return userDao.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>(10);
            map.forEach((k, v) -> {
                // 同一家企业
                if (SystemConstant.COMPANY_ID.equals(k)) {
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class), v));
                }
                // 某一个部门
                if (SystemConstant.DEPARTMENT_ID.equals(k)) {
                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class), v));
                }
                // 分配部门与否 1,已分配，0，未分配
                if (SystemConstant.HAS_DEPT.equals(k)) {
                    if (SystemConstant.ZERO.equals(v)) {
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
                    } else if (SystemConstant.ONE.equals(v)) {
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }
                // 是否在职，1，在职，2，离职，3，全部
                if (SystemConstant.IN_SERVICE_STATUS.equals(k)) {
                    if (!FIND_ALL_FLAG.equals(v)) {
                        list.add(criteriaBuilder.equal(root.get("inServiceStatus").as(String.class), v));
                    }
                }
            });
            return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
        }, PageRequest.of(Integer.parseInt(String.valueOf(map.get("page"))) - 1, Integer.parseInt(String.valueOf(map.get("size")))));
    }

    @Override
    public List<User> findSimpleUsers(String companyId) {
        return userDao.findAll(getSameCompanySpec(companyId));
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

    @Override
    public List<User> findByTimeOfEntry(String yearMonth, String companyId) {
        List<User> list = userDao.findByCompanyIdAndTimeOfEntryLike(companyId, yearMonth);
        return list;
    }

    @Override
    public int findInJobUsers(String companyId) {
        int injobs = userDao.findInJobs(companyId);
        return injobs;
    }
}
