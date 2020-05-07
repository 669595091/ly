package com.leyou.client;

import com.leyou.common.po.PageResult;

import com.leyou.item.pojo.Brand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("item-service")
public interface BrandClient  {

    @GetMapping("brand/page")
    public PageResult<Brand> pageQuery(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                       @RequestParam(value = "rows",defaultValue = "5") Integer rows,
                                       @RequestParam( value = "sortBy",required = false) String sortBy,
                                       @RequestParam(value = "desc",required = false) Boolean desc,
                                       @RequestParam(value = "key",required = false) String  key
    );

    @GetMapping("brand/cid/{cid}")
    public List<Brand> queryBrandByCategory(@PathVariable("cid")  Long id);


}
