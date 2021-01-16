package com.xm.cpsmall.module.cron.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sc_bill_pay")
public class ScBillPayEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 用户id
	 */
	private Integer userId;

	/**
	 * 用户标识
	 */
	private String openId;

	/**
	 * 相关账单
	 */
	private String billIds;

	/**
	 * 支付总额
	 */
	private Integer totalMoney;

	/**
	 * 状态(1:待处理,2:处理成功,3:处理失败)
	 */
	private Integer state;

	/**
	 * 失败原因
	 */
	private String failReason;

	/**
	 * 处理结果id
	 */
	private Integer processId;

	/**
	 * 支付单号
	 */
	private String processSn;

	/**
	 * 支付平台单号
	 */
	private String paySysSn;

	private java.util.Date updateTime;

	private java.util.Date createTime;
}
