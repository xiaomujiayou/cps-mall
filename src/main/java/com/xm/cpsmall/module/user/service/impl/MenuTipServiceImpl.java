package com.xm.cpsmall.module.user.service.impl;

import com.xm.cpsmall.module.user.mapper.SuMenuTipsMapper;
import com.xm.cpsmall.module.user.serialize.entity.SuMenuTipsEntity;
import com.xm.cpsmall.module.user.serialize.vo.MenuTipVo;
import com.xm.cpsmall.module.user.service.MenuTipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("menuTipService")
public class MenuTipServiceImpl implements MenuTipService {

    @Autowired
    private SuMenuTipsMapper suMenuTipsMapper;

    @Override
    public void addNum(Integer userId, List<Integer> menuIds) {
        List<SuMenuTipsEntity> menuTipsEntities = getUserMenuTipsEntity(userId,menuIds);
        menuTipsEntities.stream().forEach(o->{
            o.setNum(o.getNum() == null ? 1 : o.getNum()+1);
        });
        saveOrUpdate(menuTipsEntities);
    }



    @Override
    public void addRedPoint(Integer userId, List<Integer> menuIds) {
        List<SuMenuTipsEntity> menuTipsEntities = getUserMenuTipsEntity(userId,menuIds);
        menuTipsEntities.stream().forEach(o->{
            o.setHot(1);
        });
        saveOrUpdate(menuTipsEntities);
    }

    @Override
    public void del(Integer userId, List<Integer> menuIds) {
        Example example = new Example(SuMenuTipsEntity.class);
        example.createCriteria()
                .andEqualTo("userId",userId)
                .andIn("menuId",menuIds);
        suMenuTipsMapper.deleteByExample(example);
    }

    @Override
    public List<MenuTipVo> get(Integer userId, List<Integer> menuIds) {
        Example example = new Example(SuMenuTipsEntity.class);
        example.createCriteria()
                .andEqualTo("userId",userId)
                .andIn("menuId",menuIds);
        List<SuMenuTipsEntity> menuTipsEntities = suMenuTipsMapper.selectByExample(example);
        return menuTipsEntities.stream().map(o-> convertVo(o)).collect(Collectors.toList());
    }

    private MenuTipVo convertVo(SuMenuTipsEntity suMenuTipsEntity) {
        MenuTipVo menuTipVo = new MenuTipVo();
        menuTipVo.setHot(suMenuTipsEntity.getHot() == null ? false : suMenuTipsEntity.getHot() == 1 ? true : false);
        menuTipVo.setMenuId(suMenuTipsEntity.getMenuId());
        menuTipVo.setNum(suMenuTipsEntity.getNum());
        return menuTipVo;
    }

    /**
     * 获取实体，不存在则新建
     * @param userId
     * @param menuIds
     * @return
     */
    private List<SuMenuTipsEntity> getUserMenuTipsEntity(Integer userId, List<Integer> menuIds){
        Example example = new Example(SuMenuTipsEntity.class);
        example.createCriteria()
                .andEqualTo("userId",userId)
                .andIn("menuId",menuIds);
        List<SuMenuTipsEntity> menuTipsEntities = suMenuTipsMapper.selectByExample(example);
        menuIds.stream().forEach(o -> {
            if(!menuTipsEntities.stream().filter(j -> {return j.getMenuId().equals(o);}).findFirst().isPresent()){
                SuMenuTipsEntity suMenuTipsEntity = new SuMenuTipsEntity();
                suMenuTipsEntity.setUserId(userId);
                suMenuTipsEntity.setMenuId(o);
                suMenuTipsEntity.setUpdateTime(new Date());
                suMenuTipsEntity.setCreateTime(new Date());
                menuTipsEntities.add(suMenuTipsEntity);
            }
        });
        return menuTipsEntities;
    }

    /**
     * 保存或更新实体
     * @param menuTipsEntities
     */
    private void saveOrUpdate(List<SuMenuTipsEntity> menuTipsEntities) {
        Map<String,List<SuMenuTipsEntity>> group = menuTipsEntities.stream().collect(Collectors.groupingBy((j)->{
            return j.getId() != null?"update":"insert";
        }));
        if(group.get("insert") != null && !group.get("insert").isEmpty())
            suMenuTipsMapper.insertList(group.get("insert"));
        if(group.get("update") != null && !group.get("update").isEmpty()){
            group.get("update").stream().forEach(o->{
                suMenuTipsMapper.updateByPrimaryKeySelective(o);
            });
        }
    }
}
