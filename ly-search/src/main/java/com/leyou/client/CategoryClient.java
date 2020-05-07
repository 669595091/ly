package com.leyou.client;


import com.leyou.item.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("item-service")
public interface CategoryClient {


    @GetMapping("category/list")
    public List<Category> queryByParentId(@RequestParam("pid") Long id);

    @GetMapping("category/bid/{bid}")
    public List<Category> queryByBrandId(@PathVariable("bid") Long id);

    @GetMapping("category/names")
    public List<String> queryNamesByIds(@RequestParam("ids") List<Long> ids);
    //http://item-service/category/names
    //http://127.0.0.1:9081/category/names
}
