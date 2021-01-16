package com.xm.cpsmall.comm.api.mgj;

import lombok.Data;

@Data
public class OrderInfoQueryBean {
    /**
     * {
     *      "start": 20170101 Integer开始日期必填(订单创建时间),
     *      "end": 20170110 Integer结束日期必填(订单创建时间。建议起止时间跨度不超过10天,
     *      "page": 1 Integer页数 必填,
     *      "pagesize": 20 必填 Integer每页大小,
     *      "orderNo": 123456789 Long订单号，根据订单号查询订单时，可以不填前面的参数
     *  }
     */

    private Integer start;
    private Integer end;
    private Integer page;
    private Integer pagesize;
    private Long orderNo;
}
