package com.xm.cpsmall.module.user.controller;

import com.xm.cpsmall.module.user.mapper.SuConfigMapper;
import com.xm.cpsmall.module.user.serialize.entity.SuConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@RestController
@RequestMapping("/api-user/config")
public class UserConfigController {

    @Autowired
    private SuConfigMapper suConfigMapper;

    @GetMapping("/all")
    public List<SuConfigEntity> getAllConfig(Integer userId){
        Example example = new Example(SuConfigEntity.class);
        example.createCriteria().andEqualTo("userId",userId);
        return suConfigMapper.selectByExample(example);
    }
    @GetMapping("/one")
    public SuConfigEntity getOneConfig(Integer userId, String name){
        Example example = new Example(SuConfigEntity.class);
        example.createCriteria()
                .andEqualTo("userId",userId)
                .andEqualTo("name",name);
        return suConfigMapper.selectOneByExample(example);
    }
}
