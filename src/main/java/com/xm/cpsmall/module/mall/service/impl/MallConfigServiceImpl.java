package com.xm.cpsmall.module.mall.service.impl;

import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.mall.constant.ConfigEnmu;
import com.xm.cpsmall.module.mall.constant.ConfigTypeConstant;
import com.xm.cpsmall.module.mall.mapper.SmConfigMapper;
import com.xm.cpsmall.module.mall.serialize.entity.SmConfigEntity;
import com.xm.cpsmall.module.mall.service.MallConfigService;
import com.xm.cpsmall.module.user.constant.UserTypeConstant;
import com.xm.cpsmall.module.user.controller.UserConfigController;
import com.xm.cpsmall.module.user.serialize.entity.SuConfigEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import com.xm.cpsmall.module.user.service.UserService;
import com.xm.cpsmall.utils.response.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service("mallConfigServiceImpl")
public class MallConfigServiceImpl implements MallConfigService {

    @Autowired
    private SmConfigMapper smConfigMapper;
    @Autowired
    private UserConfigController userConfigController;
    @Lazy
    @Autowired
    private UserService userService;

    @Override
    public SmConfigEntity getConfig(Integer userId, ConfigEnmu configEnmu, int configType) {
        if(userId == null || userId == 0){
            Example example = new Example(SmConfigEntity.class);
            example.createCriteria().andEqualTo("name",configEnmu.getName());
            SmConfigEntity smConfigEntity = smConfigMapper.selectOneByExample(example);
            return smConfigEntity;
        }
        switch (configType){
            case ConfigTypeConstant.SYS_CONFIG:{
                Example example = new Example(SmConfigEntity.class);
                example.createCriteria().andEqualTo("name",configEnmu.getName());
                SmConfigEntity smConfigEntity = smConfigMapper.selectOneByExample(example);
                return smConfigEntity;
            }
            case ConfigTypeConstant.SELF_CONFIG:{
                Example example = new Example(SmConfigEntity.class);
                example.createCriteria().andEqualTo("name",configEnmu.getName());
                SmConfigEntity smConfigEntity = smConfigMapper.selectOneByExample(example);
                if(userId == null)
                    return smConfigEntity;
                SuConfigEntity suConfigEntity = userConfigController.getOneConfig(userId,configEnmu.getName());
                if(suConfigEntity != null){
                    smConfigEntity.setVal(suConfigEntity.getVal());
                }
                return smConfigEntity;
            }
            case ConfigTypeConstant.PARENT_CONFIG:{
                SuUserEntity parent = userService.getSuperUser(userId, UserTypeConstant.PARENT);
                if(parent == null){
                    Example example = new Example(SmConfigEntity.class);
                    example.createCriteria().andEqualTo("name",configEnmu.getName());
                    SmConfigEntity smConfigEntity = smConfigMapper.selectOneByExample(example);
                    return smConfigEntity;
                }else {
                    Example example = new Example(SmConfigEntity.class);
                    example.createCriteria().andEqualTo("name",configEnmu.getName());
                    SmConfigEntity smConfigEntity = smConfigMapper.selectOneByExample(example);
                    SuConfigEntity suConfigEntity = userConfigController.getOneConfig(parent.getId(),configEnmu.getName());
                    if(suConfigEntity != null){
                        smConfigEntity.setVal(suConfigEntity.getVal());
                    }
                    return smConfigEntity;
                }
            }
            case ConfigTypeConstant.PROXY_CONFIG:{

                SuUserEntity proxy = userService.getSuperUser(userId, UserTypeConstant.PROXY);
                if(proxy == null){
                    Example example = new Example(SmConfigEntity.class);
                    example.createCriteria().andEqualTo("name",configEnmu.getName());
                    SmConfigEntity smConfigEntity = smConfigMapper.selectOneByExample(example);
                    return smConfigEntity;
                }else {
                    Example example = new Example(SmConfigEntity.class);
                    example.createCriteria().andEqualTo("name",configEnmu.getName());
                    SmConfigEntity smConfigEntity = smConfigMapper.selectOneByExample(example);
                    SuConfigEntity suConfigEntity = userConfigController.getOneConfig(proxy.getId(),configEnmu.getName());
                    if(suConfigEntity != null){
                        smConfigEntity.setVal(suConfigEntity.getVal());
                    }
                    return smConfigEntity;
                }
            }
        }
        throw new GlobleException(MsgEnum.TYPE_NOTFOUND_ERROR);
    }

    @Override
    public List<SmConfigEntity> getAllConfig(Integer userId,int configType) {
        List<SmConfigEntity> smConfigEntitys = smConfigMapper.selectAll();
        switch (configType){
            case ConfigTypeConstant.SYS_CONFIG:{
                return smConfigEntitys;
            }
            case ConfigTypeConstant.SELF_CONFIG:{
                List<SuConfigEntity> suConfigEntities = userConfigController.getAllConfig(userId);
                if(suConfigEntities == null)
                    return smConfigEntitys;
                smConfigEntitys.forEach(o ->{
                    SuConfigEntity suConfigEntity = suConfigEntities.stream().filter(i ->{
                        return i.getName().equals(o.getName());
                    }).findFirst().get();
                    if(suConfigEntity != null){
                        o.setVal(suConfigEntity.getVal());
                    }
                });
                return smConfigEntitys;
            }
            case ConfigTypeConstant.PARENT_CONFIG:{
                SuUserEntity parent = userService.getSuperUser(userId, UserTypeConstant.PARENT);
                if(parent == null){
                    return smConfigEntitys;
                }else {
                    List<SuConfigEntity> suConfigEntities = userConfigController.getAllConfig(parent.getId());
                    if(suConfigEntities == null)
                        return smConfigEntitys;
                    smConfigEntitys = unionConfig(smConfigEntitys,suConfigEntities);
                    return smConfigEntitys;
                }
            }
            case ConfigTypeConstant.PROXY_CONFIG:{
                SuUserEntity proxy = userService.getSuperUser(userId, UserTypeConstant.PROXY);
                if(proxy == null){
                    return smConfigEntitys;
                }else {
                    List<SuConfigEntity> suConfigEntities = userConfigController.getAllConfig(proxy.getId());
                    if(suConfigEntities == null)
                        return smConfigEntitys;
                    smConfigEntitys = unionConfig(smConfigEntitys,suConfigEntities);
                    return smConfigEntitys;
                }
            }
        }
        throw new GlobleException(MsgEnum.TYPE_NOTFOUND_ERROR);
    }

    /**
     * 合并配置
     * @param smConfigEntities  :系统配置
     * @param suConfigEntities  :目标配置
     * @return
     */
    private List<SmConfigEntity> unionConfig(List<SmConfigEntity> smConfigEntities,List<SuConfigEntity> suConfigEntities){
        smConfigEntities.forEach(o ->{
            SuConfigEntity suConfigEntity = suConfigEntities.stream().filter(i ->{
                return i.getName().equals(o.getName());
            }).findFirst().get();
            if(suConfigEntity != null){
                o.setVal(suConfigEntity.getVal());
            }
        });
        return smConfigEntities;
    }
}
