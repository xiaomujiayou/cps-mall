package com.xm.cpsmall.module.cron.utils;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.Date;

@Data
public class BillInfoExcel {
    @ColumnWidth(25)
    @ExcelProperty(value = "账单单号", index = 0)
    private String billSn;
    @ColumnWidth(10)
    @ExcelProperty(value = "账单类型", index = 1)
    private String billType;
    @ColumnWidth(10)
    @ExcelProperty(value = "金额", index = 2)
    private String money;
    @ColumnWidth(25)
    @ExcelProperty(value = "订单单号", index = 3)
    private String orderSn;
    @ColumnWidth(65)
    @ExcelProperty(value = "商品名称", index = 4)
    private String productName;
    @ColumnWidth(15)
    @ExcelProperty(value = "来源用户", index = 5)
    private String userName;
    @ColumnWidth(20)
    @ExcelProperty(value = "账单创建时间", index = 6)
    private Date createTime;
}
