package com.xm.cpsmall.module.wind.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sw_credit_bill_pay_record")
public class SwCreditBillPayRecordEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 所属用户
	 */
	private Integer userId;

	/**
	 * 账单号
	 */
	private String billSn;

	/**
	 * 账单金额
	 */
	private Integer billMoney;

	/**
	 * 检测结果(0:未通过,1:通过,2:人工处理)
	 */
	private Integer checkResult;

	/**
	 * 未通过原因
	 */
	private String failReason;

	private java.util.Date createTime;
}
