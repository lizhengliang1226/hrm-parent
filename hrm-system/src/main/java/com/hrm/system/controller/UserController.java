package com.hrm.system.controller;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.hrm.common.client.CompanyFeignClient;
import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.domain.company.Department;
import com.hrm.domain.system.User;
import com.hrm.domain.system.response.ProfileResult;
import com.hrm.system.service.OssService;
import com.hrm.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理
 *
 * @author LZL
 * @date 2022/3/7-19:35
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("sys")
@Api(tags = "用户管理")
public class UserController extends BaseController {

    private UserService userService;
    private OssService ossService;
    private CompanyFeignClient companyFeignClient;

    @PostMapping(value = "user/import", name = "IMPORT_USER_API")
    @ApiOperation(value = "批量保存用户")
    public Result importUsers(@RequestParam MultipartFile file) throws Exception {
        final ExcelReaderBuilder read = EasyExcel.read(file.getInputStream(), User.class, new UserExcelListener());
        final ExcelReaderSheetBuilder sheet = read.sheet();
        sheet.doRead();
        return Result.SUCCESS();
    }


    @PostMapping(value = "user", name = "SAVE_USER_API")
    @ApiOperation(value = "保存用户")
    public Result save(@RequestBody User user) {
        //设置保存的企业id，目前使用固定值1，以后会解决
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        userService.save(user);
        return Result.SUCCESS();
    }

    @PutMapping(value = "user/{id}", name = "UPDATE_USER_API")
    @ApiOperation(value = "更新用户")
    public Result update(@PathVariable String id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        return Result.SUCCESS();
    }

    @PutMapping(value = "user/{id}/{password}", name = "UPDATE_PASSWORD_API")
    @ApiOperation(value = "更新密码")
    public Result updatePassword(@PathVariable String id, @PathVariable String password) {
        userService.updatePassword(id, password);
        return Result.SUCCESS();
    }

    @GetMapping(value = "user/{id}/{password}", name = "VERIFY_PASSWORD_API")
    @ApiOperation(value = "验证密码")
    public Result verifyPassword(@PathVariable String id, @PathVariable String password) {
        final User byId = userService.findById(id);
        final String s = SecureUtil.des(byId.getMobile().getBytes(StandardCharsets.UTF_8)).encryptHex(password);
        if (s.equals(byId.getPassword())) {
            return new Result<>(ResultCode.SUCCESS);
        } else {
            return new Result<>(ResultCode.FAIL);
        }
    }

    @RequiresPermissions(value = "DELETE_USER_API")
    @DeleteMapping(value = "user/{id}", name = "DELETE_USER_API")
    @ApiOperation(value = "根据id删除用户")
    public Result delete(@PathVariable String id) {
        userService.deleteById(id);
        return Result.SUCCESS();
    }

    @GetMapping(value = "user/{id}", name = "FIND_USER_API")
    @ApiOperation(value = "根据ID查找用户")
    public Result findById(@PathVariable String id) {
        final User byId = userService.findById(id);
        return new Result<>(ResultCode.SUCCESS, byId);
    }

    @GetMapping(value = "user/roles/{id}", name = "FIND_USER_ROLE_API")
    @ApiOperation(value = "根据ID查找用户拥有的角色数组")
    public Result findUserRole(@PathVariable String id) {
        final User user = userService.findById(id);
        List<String> roleIds = new ArrayList<>();
        user.getRoles().forEach(role -> {
            roleIds.add(role.getId());
        });
        return new Result<>(ResultCode.SUCCESS, roleIds);
    }

    @GetMapping(value = "user", name = "FIND_USER_LIST_API")
    @ApiOperation(value = "获取某个企业的用户列表")
    public Result<PageResult<User>> findAll(@RequestParam Map<String, Object> map) {
        map.put("companyId", companyId);
        final Page<User> all = userService.findAll(map);
        final PageResult<User> pageResult = new PageResult<>(all.getTotalElements(), all.getContent());
        return new Result<>(ResultCode.SUCCESS, pageResult);
    }

    @GetMapping(value = "user/simple", name = "FIND_SIMPLE_USER_LIST_API")
    @ApiOperation(value = "获取某个企业的简洁用户列表")
    public Result<List<Map<String, Object>>> findUserList() {
        final List<User> all = userService.findSimpleUsers(companyId);
        List<Map<String, Object>> list = new ArrayList<>();
        all.forEach(user -> {
            Map<String, Object> map = new HashMap<>(2);
            map.put("username", user.getUsername());
            map.put("id", user.getId());
            list.add(map);
        });
        return new Result<>(ResultCode.SUCCESS, list);
    }

    /**
     * 获取文件上传的后端签名
     *
     * @return 结果集
     */
    @GetMapping(value = "oss", name = "OSS_POLICY")
    @ApiOperation(value = "获取文件上传的后端签名")
    public Result<Map<String, String>> policy() {
        return new Result<>(ResultCode.SUCCESS, ossService.policy());
    }

    @PutMapping(value = "user/assignRoles", name = "ASSIGN_ROLES_API")
    @ApiOperation(value = "给用户分配角色")
    public Result assignRoles(@RequestBody Map map) {
        String id = (String) map.get("id");
        List<String> roles = (List<String>) map.get("roleIds");
        userService.assignRoles(id, roles);
        return Result.SUCCESS();
    }


    @PostMapping("login")
    @ApiOperation("用户登录")
    public Result loginByShiro(@RequestBody Map<String, String> loginMap) {
        // 获取手机号和密码
        String mobile = loginMap.get("mobile");
        String password = loginMap.get("password");
        try {
            final String s = SecureUtil.des(mobile.getBytes(StandardCharsets.UTF_8)).encryptHex(password);
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(mobile, s);
            final Subject subject = SecurityUtils.getSubject();
            subject.login(usernamePasswordToken);
            String sid = (String) subject.getSession().getId();
            return new Result(ResultCode.SUCCESS, sid);
        } catch (Exception e) {
            return new Result(ResultCode.LOGIN_FAIL);
        }
    }


    @GetMapping(value = "profile")
    @ApiOperation(value = "用户登录成功后的返回信息")
    public Result profile(HttpServletRequest request) {
        // 获取session中的安全数据
        final Subject subject = SecurityUtils.getSubject();
        final PrincipalCollection previousPrincipals = subject.getPrincipals();
        final ProfileResult profileResult = (ProfileResult) previousPrincipals.getPrimaryPrincipal();
        return new Result(ResultCode.SUCCESS, profileResult);
    }

    /**
     * Excel操作的内部类
     */
    class UserExcelListener extends AnalysisEventListener<User> {

        @SneakyThrows
        @Override
        public void invoke(User user, AnalysisContext analysisContext) {
            final Result<Department> result = companyFeignClient.findByCode(user.getDepartmentId(), companyId);
            final Department data = result.getData();
            user.setDepartmentId(data.getId());
            user.setDepartmentName(data.getName());
            user.setCompanyName(companyName);
            user.setCompanyId(companyId);
            userService.save(user);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }

    }

    @Autowired
    public void setCompanyFeignClient(CompanyFeignClient companyFeignClient) {
        this.companyFeignClient = companyFeignClient;
    }

    @Autowired
    public void setOssService(OssService ossService) {
        this.ossService = ossService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
