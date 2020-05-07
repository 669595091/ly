package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.po.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> pageQuery(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        // select count(*) from tb_brand limit 0,5
        //like %key%
        //orderby sortBy desc

        //开启分页
        PageHelper.startPage(page,rows);
        //查询条件,过滤
        Example example = new Example(Brand.class);

        if(StringUtils.isBlank(key)){
            //获取Criteria对象,创建一个查询条件的构造对象
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("name","%"+key+"%");
        }
        //排序
        //判断排序字段不为空
        if(StringUtils.isBlank(sortBy)){
            //desc 为true ，倒序
            example.setOrderByClause(sortBy+ (desc?" desc":" asc") );
        }
        //查询
        //直接查询并转换为分页条件结果
        Page<Brand> brandPage = (Page<Brand>)brandMapper.selectByExample(example);


        //封装分页的结果对象
        return new PageResult<>(
               brandPage.getTotal(),
               new Long(brandPage.getPages()),
               brandPage.getResult());
    }

    @Transactional//开启事务管理
    public void addBrand(Brand brand, List<Long> cids) {
        //新增品牌信息,由于是自增主键，并且配置主键生成策略，所以这里会主键回显
        this.brandMapper.insertSelective(brand);
        // 新增品牌和分类中间表tb_category_brand
        /*cids.forEach(
            cid->{this.brandMapper.insertBrandCategory(brand.getId(),cid);}
        );*/
        for(Long i:cids){
            brandMapper.insertBrandCategory(i , brand.getId());
        }


    }

    @Transactional//开启事务注解
    public void updateBrand(Brand brand, List<Long> cids) {
        //更新tb_brand表
        this.brandMapper.updateByPrimaryKey(brand);
        //删除中间表
        this.brandMapper.deleteBrandCategory(brand.getId());
        //添加中间表
        cids.forEach(
                t->{
                    brandMapper.insertBrandCategory(t,brand.getId());
                }
        );
    }

    public List<Brand> queryBrandByCategory(Long cid) {
        return brandMapper.queryBrandByCategory(cid);
    }
}
