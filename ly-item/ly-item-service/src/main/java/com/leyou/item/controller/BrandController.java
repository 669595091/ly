package com.leyou.item.controller;

import com.leyou.common.po.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> pageQuery(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",required = false) Boolean desc,
            @RequestParam(value = "key",required = false) String key
    ){
        PageResult<Brand> brandPageResult = brandService.pageQuery(page,rows,sortBy,desc,key);
        if(null!=brandPageResult && brandPageResult.getItems().size()>0){
            return  ResponseEntity.ok(brandPageResult);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //添加品牌
    @PostMapping
    public ResponseEntity<Void> addBrand(Brand brand, @RequestParam("cids")List<Long> cids){
        this.brandService.addBrand(brand,cids);

        return ResponseEntity.status(HttpStatus.CREATED).build();//返回201
    }

    //编辑品牌
    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("cids")List<Long> cids){
        this.brandService.updateBrand(brand,cids);

        return ResponseEntity.status(HttpStatus.CREATED).build();//返回201

    }
    //品牌新增
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCategory(@PathVariable("cid")Long cid){
        List<Brand> brandList = brandService.queryBrandByCategory(cid);
        if(null!=brandList && brandList.size()>0){
            return  ResponseEntity.ok(brandList);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


}
