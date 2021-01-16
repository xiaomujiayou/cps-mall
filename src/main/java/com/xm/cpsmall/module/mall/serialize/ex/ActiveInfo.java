package com.xm.cpsmall.module.mall.serialize.ex;

import cn.hutool.core.clone.Cloneable;
import lombok.Data;

@Data
public class ActiveInfo implements Cloneable {
    private Integer activeId;
    private Integer money;
    private String name;
    private String des;

    @Override
    public ActiveInfo clone() {
        ActiveInfo info = new ActiveInfo();
        info.setActiveId(activeId);
        info.setMoney(money);
        info.setName(name);
        info.setDes(des);
        return info;
    }
}
