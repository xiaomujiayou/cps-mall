package com.xm.cpsmall.module.mall.serialize.ex;

import com.xm.cpsmall.module.mall.serialize.entity.SmOptEntity;
import lombok.Data;

import java.util.List;

@Data
public class OptEx extends SmOptEntity {
    private List<OptEx> childs;
}
