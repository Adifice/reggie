package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie.common.R;
import com.example.reggie.entity.Employee;
import com.example.reggie.service.EmpolyeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmpolyeeController {
    @Autowired
    private EmpolyeeService empolyeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //MD5 处理密码
        String pwd = employee.getPassword();
        String password = DigestUtils.md5DigestAsHex(pwd.getBytes());
        //find user using username
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,employee.getUsername());
        Employee emp = empolyeeService.getOne(lqw);

        if(emp == null){
            return R.error("User does not exist");
        }
        if(!emp.getPassword().equals(password)){
            return R.error("Username and password does not match");
        }
        if(emp.getStatus() == 0){
            return R.error("Banned account");
        }

        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("Logout");
    }

    //add new employee
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        empolyeeService.save(employee);
        return R.success("Success");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page page1 = new Page(page,pageSize);
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(name != null,Employee::getName,name);
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);

        empolyeeService.page(page1,lambdaQueryWrapper);
        return R.success(page1);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
//        Long curId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(curId);
        empolyeeService.updateById(employee);
        return R.success("更新成功");

    }

    @GetMapping("/{id}")
    public R<Employee> get(@PathVariable Long id){
        Employee emp = empolyeeService.getById(id);
        return R.success(emp);
    }

}
