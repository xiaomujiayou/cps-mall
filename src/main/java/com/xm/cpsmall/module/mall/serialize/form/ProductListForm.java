package com.xm.cpsmall.module.mall.serialize.form;

import com.xm.cpsmall.utils.form.AbsPageForm;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProductListForm extends AbsPageForm {
    //所属平台
    @NotNull(message = "platformType不能为空")
    private Integer platformType;
    //列表类型
    @NotNull(message = "listType不能为空")
    private Integer listType;
    //optionId
    private Integer optionId;
    //关键字
    private String keywords;
    //排序
    // 0-综合排序;
    // 1-按佣金比率升序;
    // 2-按佣金比例降序;
    // 3-按价格升序;
    // 4-按价格降序;
    // 5-按销量升序;
    // 6-按销量降序;
    // 7-优惠券金额排序升序;
    // 8-优惠券金额排序降序;
    // 9-券后价升序排序;
    // 10-券后价降序排序;
    private Integer sort;
    //最高价
    private Integer minPrice;
    //最低价
    private Integer maxPrice;
    //商品Id
    private String goodsId;
    //主题Id(pdd主题商品)
    private Integer themeId;
    //营销活动烈性
    private Integer channelType;
    private List<Integer> activityTags;
    //附加参数
    private Integer additional;
    //包含优惠券
    private Boolean hasCoupon;

}


