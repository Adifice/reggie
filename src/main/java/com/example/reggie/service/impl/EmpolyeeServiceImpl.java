package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.entity.Employee;
import com.example.reggie.mapper.EmployeeMapper;
import com.example.reggie.service.EmpolyeeService;
import org.springframework.stereotype.Service;

@Service
public class EmpolyeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmpolyeeService {
}
