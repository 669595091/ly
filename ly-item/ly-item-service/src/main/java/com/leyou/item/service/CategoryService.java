package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryByParentId(Long id) {

        //select * from tb_category where parent_id=id
        Category category = new Category();
        category.setParentId(id);
        return categoryMapper.select(category);

    }

    public List<Category> queryByBrandId(Long id) {
        return categoryMapper.queryByBrandId(id);
    }
    //查询分类菜单名称
    public List<String> queryNameByIds(List<Long> asList) {
        //74,75,76
        //select * from tb_category where id in(74,75,76)
        List<String> str = new ArrayList<>();
        List<Category> categoryList = this.categoryMapper.selectByIdList(asList);
        categoryList.forEach(
                t->{
                    str.add(t.getName());
                }
        );
        return str;
    }

    public List<String> queryNamesByIds(List<Long> ids) {
        //74,75,76
        //select * from tb_category where id in(74,75,76)
        List<String> str = new ArrayList<>();
        List<Category> categoryList = this.categoryMapper.selectByIdList(ids);
        categoryList.forEach(
                t->{
                    str.add(t.getName());
                }
        );
        return str;
    }
}
