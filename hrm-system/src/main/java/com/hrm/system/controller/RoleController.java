package com.hrm.system.controller;

import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.domain.system.Role;
import com.hrm.system.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LZL
 * @date 2022/3/9-1:21
 */
@RestController
@CrossOrigin
@RequestMapping("sys")
@Api(tags = "角色管理")
public class RoleController extends BaseController {

    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping(value = "role", name = "SAVE_ROLE_API")
    @ApiOperation(value = "保存角色")
    public Result save(@RequestBody Role role) {
        //设置保存的企业id，目前使用固定值1，以后会解决
        role.setCompanyId(companyId);
        roleService.save(role);
        return Result.SUCCESS();
    }

    @PutMapping(value = "role/{id}", name = "UPDATE_ROLE_API")
    @ApiOperation(value = "更新角色")
    public Result update(@PathVariable(value = "id") String id, @RequestBody Role role) {
        role.setId(id);
        role.setCompanyId(companyId);
        roleService.update(role);
        return Result.SUCCESS();
    }

    @DeleteMapping(value = "role/{id}", name = "DELETE_ROLE_API")
    @ApiOperation(value = "根据id删除角色")
    public Result delete(@PathVariable(value = "id") String id) {
        roleService.deleteById(id);
        return Result.SUCCESS();
    }

    @GetMapping(value = "role/{id}", name = "FIND_ROLE_API")
    @ApiOperation(value = "根据ID查找角色")
    public Result findById(@PathVariable(value = "id") String id) {
        final Role byId = roleService.findById(id);
        return new Result<>(ResultCode.SUCCESS, byId);
    }

    @GetMapping(value = "role/perms/{id}", name = "FIND_ROLE_PERMS_API")
    @ApiOperation(value = "查询角色拥有的权限")
    public Result findRolePerms(@PathVariable(value = "id") String id) {
        final Role role = roleService.findById(id);
        List<String> permIds = new ArrayList<>();
        role.getPermissions().forEach(perm -> permIds.add(perm.getId()));
        return new Result<>(ResultCode.SUCCESS, permIds);
    }

    @GetMapping(value = "role", name = "FIND_ROLES_PAGE_API")
    @ApiOperation(value = "带分页获取某个企业的角色列表")
    public Result findSearch(@RequestParam Map map) {
        map.put("companyId", companyId);
        final Page<Role> all = roleService.findSearch(map);
        final PageResult<Role> pageResult = new PageResult(all.getTotalElements(), all.getContent());
        return new Result<>(ResultCode.SUCCESS, pageResult);
    }

    @GetMapping(value = "role/list", name = "FIND_ROLE_LIST_API")
    @ApiOperation(value = "获取某个企业的全部角色列表")
    public Result findAll(@RequestParam Map map) {
        map.put("companyId", companyId);
        return new Result<>(ResultCode.SUCCESS, roleService.findAll(map));
    }

    @PutMapping(value = "role/assignPerm", name = "ASSIGN_PERM_API")
    @ApiOperation(value = "给角色分配权限")
    public Result assignPerms(@RequestBody Map map) {
        String id = (String) map.get("id");
        List<String> permissions = (List<String>) map.get("permIds");
        roleService.assignPerms(id, permissions);
        return Result.SUCCESS();
    }
}
