package com.leyou.item.service;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecService {

    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> querySpecGroups(Long id) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(id);
        //通过分类id查询规格参数
        List<SpecGroup> specGroupList = specGroupMapper.select(specGroup);
        //查询规格参数的规格组
        specGroupList.forEach(
                t->{
                    Long specGroupId = t.getId();
                    SpecParam specParam = new SpecParam();
                    specParam.setGroupId(specGroupId);
                    //通过规格组的id查询规格参数表
                    List<SpecParam> specParams = this.specParamMapper.select(specParam);
                    //select * from spec_group where cid=76
                    //把规格参数表封装到规格组中
                    t.setSpecParams(specParams);
                }
        );
        return specGroupList;
    }

    public List<SpecParam> querySpecParam(Long gid, Long cid, Boolean searching, Boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);

        specParam.setCid(cid);
        specParam.setSearching(searching);
        specParam.setGeneric(generic);

        return this.specParamMapper.select(specParam);

    }
}
