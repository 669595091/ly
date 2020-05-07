package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public ResponseEntity<List<Category>> queryByParentId(@RequestParam("pid") Long id){
        List<Category> categoryList = categoryService.queryByParentId(id);
        if(null!=categoryList && categoryList.size()>0){
            return ResponseEntity.ok(categoryList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long id){
        List<Category> categoryList = categoryService.queryByBrandId(id);
        if(null != categoryList && categoryList.size()>0){
            return ResponseEntity.ok(categoryList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("names")
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids") List<Long> ids){
        List<String> list = categoryService.queryNamesByIds(ids);
        if(null != list && list.size()>0){
            return ResponseEntity.ok(list);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
