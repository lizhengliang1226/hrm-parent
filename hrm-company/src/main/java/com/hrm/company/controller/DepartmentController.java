package com.hrm.company.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.company.service.CompanyService;
import com.hrm.company.service.DepartmentService;
import com.hrm.domain.company.Department;
import com.hrm.domain.company.response.DeptListResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LZL
 *
 * @date 2022/3/7-19:35
 */
@RestController
@CrossOrigin
@RequestMapping("company")
@Api(tags = "部门管理")
public class DepartmentController extends BaseController {

    private DepartmentService departmentService;
    private CompanyService companyService;
    private List<Department> list = new ArrayList<>();
    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping(value = "department", name = "SAVE_DEPT_API")
    @ApiOperation(value = "保存部门")
    public Result save(@RequestBody Department department) {
        //设置保存的企业id，目前使用固定值1，以后会解决
        department.setCompanyId(companyId);
        departmentService.save(department);
        return Result.SUCCESS();
    }

    @PutMapping(value = "department/{id}", name = "UPDATE_DEPT_API")
    @ApiOperation(value = "更新部门")
    public Result update(@PathVariable(value = "id") String id, @RequestBody Department department) {
        department.setId(id);
        departmentService.update(department);
        return Result.SUCCESS();
    }

    @DeleteMapping(value = "department/{id}", name = "DELETE_DEPT_API")
    @ApiOperation(value = "根据id删除部门")
    public Result delete(@PathVariable(value = "id") String id) {
        departmentService.deleteById(id);
        return Result.SUCCESS();
    }

    @GetMapping(value = "department/{id}", name = "FIND_DEPT_API")
    @ApiOperation(value = "根据ID查找部门")
    public Result findById(@PathVariable(value = "id") String id) {
        final Department byId = departmentService.findById(id);
        return new Result<>(ResultCode.SUCCESS, byId);
    }

    @GetMapping(value = "department/code/{code}/{companyId}", name = "FIND_DEPT_CODE_API")
    @ApiOperation(value = "根据部门编码查找部门")
    public Result<Department> findByCode(@PathVariable String code, @PathVariable String companyId) {
        final Department department = departmentService.findByCode(code, companyId);
        return new Result<>(ResultCode.SUCCESS, department);
    }

    @GetMapping(value = "department", name = "FIND_DEPT_LIST_API")
    @ApiOperation(value = "获取某个企业的部门列表")
    public Result<DeptListResult> findAll() {
        //暂时都用1企业，之后会改
        final List<Department> all = departmentService.findAll(companyId);
        DeptListResult deptListResult = new DeptListResult(companyService.findById(companyId), all);
        return new Result<>(ResultCode.SUCCESS, deptListResult);
    }

    @PostMapping(value = "department/import")
    @ApiOperation(value = "批量保存部门")
    public Result importDepartments(@RequestParam MultipartFile file) throws Exception {
        final ExcelReaderBuilder read = EasyExcel.read(file.getInputStream(), Department.class, new DepartmentExcelListener());
        final ExcelReaderSheetBuilder sheet = read.sheet();
        sheet.doRead();
        return Result.SUCCESS();
    }

    /**
     * Excel操作的内部类
     */
    class DepartmentExcelListener extends AnalysisEventListener<Department> {

        @SneakyThrows
        @Override
        public void invoke(Department department, AnalysisContext analysisContext) {
//            final Result<Department> result = companyFeignClient.findByCode(user.getDepartmentId(), companyId);
//            final Department data = result.getData();
//            user.setDepartmentId(data.getId());
//            user.setDepartmentName(data.getName());
//            user.setCompanyName(companyName);
//            user.setCompanyId(companyId);
//            userService.save(user);

        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }

    }
}
