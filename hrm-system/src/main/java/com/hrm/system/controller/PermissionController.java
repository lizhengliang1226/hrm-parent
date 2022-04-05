package com.hrm.system.controller;


import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.domain.system.Permission;
import com.hrm.system.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 权限管理
 *
 * @author LZL
 * @date 2022/3/10-6:04
 */
@RestController
@CrossOrigin
@RequestMapping("sys")
@Api(tags = "权限管理")
public class PermissionController extends BaseController {

    private PermissionService permissionService;

    @Autowired
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping(value = "permission", name = "SAVE_PERM_API")
    @ApiOperation(value = "保存权限")
    public Result save(@RequestBody Map<String, Object> map) throws Exception {
        permissionService.save(map);
        return Result.SUCCESS();
    }

    @PutMapping(value = "permission/{id}", name = "UPDATE_PERM_API")
    @ApiOperation(value = "更新权限")
    public Result update(@PathVariable(value = "id") String id, @RequestBody Map<String, Object> map) throws Exception {
        map.put("id", id);
        permissionService.update(map);
        return Result.SUCCESS();
    }

    @DeleteMapping(value = "permission/{id}", name = "DELETE_PERM_API")
    @ApiOperation(value = "根据id删除权限")
    public Result delete(@PathVariable(value = "id") String id) throws Exception {
        permissionService.deleteById(id);
        return Result.SUCCESS();
    }

    @GetMapping(value = "permission/{id}", name = "FIND_PERM_API")
    @ApiOperation(value = "根据ID查找权限")
    public Result findById(@PathVariable(value = "id") String id) throws Exception {
        Map map = permissionService.findById(id);
        return new Result<>(ResultCode.SUCCESS, map);
    }

    @GetMapping(value = "permission", name = "FIND_PERM_LIST_API")
    @ApiOperation(value = "获取某个企业的权限列表")
    public Result findAll(@RequestParam Map map) {
        final List<Permission> all = permissionService.findAll(map);
        return new Result<>(ResultCode.SUCCESS, all);
    }
}
