package com.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie.common.CustomException;
import com.example.reggie.entity.Category;
import com.example.reggie.entity.Dish;
import com.example.reggie.entity.Setmeal;
import com.example.reggie.mapper.CategoryMapper;
import com.example.reggie.service.CategoryService;
import com.example.reggie.service.DishService;
import com.example.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> lqw1 = new LambdaQueryWrapper<Dish>();
        lqw1.eq(Dish::getCategoryId,id);
        int num1 = dishService.count(lqw1);
        if(num1 > 0){
            throw new CustomException("关联了菜品，无法删除");
        }
        LambdaQueryWrapper<Setmeal> lqw2 = new LambdaQueryWrapper<Setmeal>();
        lqw2.eq(Setmeal::getCategoryId,id);
        int num2 = setmealService.count(lqw2);
        if(num2 > 0){
            throw new CustomException("关联了套餐，无法删除");
        }
        super.removeById(id);
    }
}
