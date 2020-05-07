package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {
    /**
     * 新增商品分类和品牌中间表数据
     * @param cid 商品分类id
     * @param i 品牌id
     * @return
     */
    @Insert("insert into tb_category_brand (category_id,brand_id) values(#{cid},#{i})")
    void insertBrandCategory(@Param("i") Long i, @Param("cid") Long cid);

    //删除中间表
    @Delete("delete from tb_category_brand where brand_id =#{id}")
    void deleteBrandCategory(@Param("id") Long id);

    //查询新增品牌
    @Select("select tb.* from tb_brand b,tb_category_brand cb where cb.brand_id = b.id and cb.category_id = #{cid}")
    List<Brand> queryBrandByCategory(@Param("cid") Long cid);
}
