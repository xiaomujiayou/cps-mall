package com.xm.cpsmall.module.mall.service.api.impl.pdd;

import cn.hutool.core.util.ObjectUtil;
import com.xm.cpsmall.module.mall.constant.ConfigEnmu;
import com.xm.cpsmall.module.mall.constant.ConfigTypeConstant;
import com.xm.cpsmall.module.mall.mapper.SmOptMapper;
import com.xm.cpsmall.module.mall.serialize.entity.SmOptEntity;
import com.xm.cpsmall.module.mall.serialize.ex.OptEx;
import com.xm.cpsmall.module.mall.serialize.form.OptionForm;
import com.xm.cpsmall.module.mall.service.MallConfigService;
import com.xm.cpsmall.module.mall.service.api.OptionService;
import com.xm.cpsmall.module.user.constant.UserTypeConstant;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import com.xm.cpsmall.module.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("pddOptionService")
public class OptionServiceImpl implements OptionService {
    @Autowired
    private SmOptMapper smOptMapper;
    @Autowired
    private MallConfigService mallConfigService;
    @Autowired
    private UserService userService;

    @Override
    public List<OptEx> list(OptionForm optionForm) {
        OrderByHelper.orderBy("sort asc");
        SmOptEntity criteria = new SmOptEntity();
        criteria.setLevel(1);
        List<SmOptEntity> smOptEntities = smOptMapper.select(criteria);
        List<OptEx> optExes = new ArrayList<>();
        for (SmOptEntity smOptEntity : smOptEntities) {
            OrderByHelper.orderBy("sort asc");
            SmOptEntity record = new SmOptEntity();
            record.setParentId(smOptEntity.getId());
            record.setLevel(2);
            List<SmOptEntity> childOpts = smOptMapper.select(record);
            OptEx optEx = new OptEx();
            BeanUtils.copyProperties(smOptEntity,optEx);
            optEx.setChilds(childOpts.stream().map(item ->{
                OptEx childOptEx = new OptEx();
                BeanUtils.copyProperties(smOptEntity,childOptEx);
                return childOptEx;
            }).collect(Collectors.toList()));
            optExes.add(optEx);
        }
        return optExes;
    }

    @Override
    public List<SmOptEntity> childList(OptionForm optionForm) {
        SuUserEntity suUserEntity = null;
        if(optionForm.getUserId() != null)
            suUserEntity = userService.getSuperUser(optionForm.getUserId(), UserTypeConstant.SELF);
        List<SmOptEntity> smOptEntities = null;
        Example example = new Example(SmOptEntity.class);
        if(optionForm.getTargetOptId() == null || optionForm.getTargetOptId() == 0){
            example.createCriteria()
                    .andEqualTo("level",1)
                    .andEqualTo("disable",1);
            smOptEntities = smOptMapper.selectByExample(example);

            String sortStr = null;
            if(suUserEntity == null || suUserEntity.getSex() == 0){
                sortStr = mallConfigService.getConfig(optionForm.getUserId(), ConfigEnmu.MAIN_OPTION_SORT, ConfigTypeConstant.PROXY_CONFIG).getVal();
            }else {
                sortStr = mallConfigService.getConfig(optionForm.getUserId(), suUserEntity.getSex() == 1?ConfigEnmu.MAIN_OPTION_SORT_MAN:ConfigEnmu.MAIN_OPTION_SORT_WOMAN, ConfigTypeConstant.PROXY_CONFIG).getVal();
            }

            List<Integer> sort = Arrays.asList(sortStr.split(",")).stream().map(o->{return Integer.valueOf(o);}).collect(Collectors.toList());
            List<SmOptEntity> sorted = new ArrayList<>();
            for (Integer id: sort) {
                SmOptEntity smOptEntity = smOptEntities.stream().filter(o->{return o.getId().equals(id);}).findFirst().orElse(null);
                if(smOptEntity != null){
                    sorted.add(smOptEntity);
                }
            }
            smOptEntities = sorted;
        }else{
            OrderByHelper.orderBy("sort asc");
            example.createCriteria().andEqualTo("parentId",optionForm.getTargetOptId());
            smOptEntities = smOptMapper.selectByExample(example);
        }
        return smOptEntities;
    }

    @Override
    public List<SmOptEntity> allParentList(OptionForm optionForm) {
        List<SmOptEntity> smOptEntities = new ArrayList<>();
        SmOptEntity smOptEntity = smOptMapper.selectByPrimaryKey(optionForm.getTargetOptId());
        while (ObjectUtil.isNotEmpty(smOptEntity) && smOptEntity.getLevel() >= 1){
            smOptEntities.add(smOptEntity);
            if(smOptEntity.getParentId() == null || smOptEntity.getParentId() == 0)
                break;
            smOptEntity = smOptMapper.selectByPrimaryKey(smOptEntity.getParentId());
        }
        return smOptEntities;
    }

    @Override
    public boolean check(String optId) {
        Example example = new Example(SmOptEntity.class);
        example.createCriteria()
                .andEqualTo("pddOptId",optId)
                .andEqualTo("disable",1);
        return smOptMapper.selectCountByExample(example) >= 1;
    }
}
