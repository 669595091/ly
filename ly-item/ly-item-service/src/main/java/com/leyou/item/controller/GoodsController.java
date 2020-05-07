package com.leyou.item.controller;

import com.leyou.common.po.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "saleable",required = false)Boolean saleable,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows
    ){
        PageResult<SpuBo> pageResult = goodsService.querySpuByPage(key,saleable,page,rows);
        if(null!=pageResult && pageResult.getItems().size()>0){
            return  ResponseEntity.ok(pageResult);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }
    //新增商品
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){

        this.goodsService.saveGoods(spuBo);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //商品详情修改
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId")Long id){
        SpuDetail spuDetail = goodsService.querySpuDetailBySpuId(id);
        if(null !=spuDetail){
            return  ResponseEntity.ok(spuDetail);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long id){
        List<Sku> skuList = goodsService.querySkuBySpuId(id);
        if(null != skuList && skuList.size()>0){
            return  ResponseEntity.ok(skuList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo){
        goodsService.updateGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }
    //根据spuid查询spu
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id")Long spuId){
        Spu spu = goodsService.querySpuById(spuId);
        if(spu==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return  ResponseEntity.ok(spu);
    }

}
