package com.xm.cpsmall.comm.api.mgj;

import lombok.Data;

@Data
public class PromInfoQueryBean {
    /**
     * {
     *      "keyword": 字符串否搜索词（商品名称关键词或商品描述关键词）,
     *      "pageNo": 整型否页码,     "pageSize": 整型否每页数据个数,
     *      "sortType": 整型是0: 默认排序，11: 佣金升序，12: 佣金降序，21: 价格升序，22：价格降序，32: 销量降序，41: 优惠券升序，42: 优惠券降序,
     *      "tag": 可以不传，传1表示只查找小编精选的商品,
     *      "cid": 整型,蘑菇街商品类目id（可以通过xiaodian.item.getAllCategory api获取列表后再使用xiaodian.common.decryptID解码后得到）,
     *      "hasCoupon": 是否只返回有优惠券的商品，false返回所有商品，true只返回有优惠券的商品 }
     */

    private String keyword;
    private Integer pageNo;
    private Integer pageSize;
    private Integer sortType;
    private Integer tag;
    private Integer cid;
    private Boolean hasCoupon;
}
