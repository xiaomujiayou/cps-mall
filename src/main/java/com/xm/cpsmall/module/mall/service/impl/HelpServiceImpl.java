package com.xm.cpsmall.module.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.xm.cpsmall.module.mall.constant.ConfigEnmu;
import com.xm.cpsmall.module.mall.constant.ConfigTypeConstant;
import com.xm.cpsmall.module.mall.mapper.SmHelpMapper;
import com.xm.cpsmall.module.mall.serialize.entity.SmConfigEntity;
import com.xm.cpsmall.module.mall.serialize.entity.SmHelpEntity;
import com.xm.cpsmall.module.mall.service.HelpService;
import com.xm.cpsmall.module.mall.service.MallConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service("helpService")
public class HelpServiceImpl implements HelpService {

    @Autowired
    private MallConfigService mallConfigService;
    @Autowired
    private SmHelpMapper smHelpMapper;

    @Override
    public SmHelpEntity getHelp(Integer userId, String url) {
        boolean helpConfig = false;
        SmConfigEntity configEntity = mallConfigService.getConfig(userId, ConfigEnmu.PAGE_HELP_STATE, ConfigTypeConstant.SELF_CONFIG);
        if(configEntity == null)
            return null;
        helpConfig = configEntity.getVal().equals("1");
        if(!helpConfig)
            return null;

        PageHelper.startPage(1,1).count(false);
        Example example = new Example(SmHelpEntity.class);
        example.createCriteria().andLike("url","%" + url + "%")
                .andNotEqualTo("state",0);
        SmHelpEntity smHelpEntity = smHelpMapper.selectOneByExample(example);
        return smHelpEntity;
    }
}
