package com.xm.cpsmall.module.mall.service.api.impl.mgj;

import com.xm.cpsmall.module.mall.serialize.entity.SmOptEntity;
import com.xm.cpsmall.module.mall.serialize.ex.OptEx;
import com.xm.cpsmall.module.mall.serialize.form.OptionForm;
import com.xm.cpsmall.module.mall.service.api.OptionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mgjOptionService")
public class OptionServiceImpl implements OptionService {
    @Override
    public List<OptEx> list(OptionForm optionForm) {
        return null;
    }

    @Override
    public List<SmOptEntity> childList(OptionForm optionForm) {
        return null;
    }

    @Override
    public List<SmOptEntity> allParentList(OptionForm optionForm) {
        return null;
    }

    @Override
    public boolean check(String optId) {
        return false;
    }
}
