package com.xm.cpsmall.module.wind.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sw_credit_bill_bind_record")
public class SwCreditBillBindRecordEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 用户id
	 */
	private Integer userId;

	/**
	 * 商品名称
	 */
	private String goodsName;

	/**
	 * 绑定的订单
	 */
	private String billSn;

	/**
	 * 账单金额
	 */
	private Integer billMoney;

	/**
	 * 发放时间
	 */
	private java.util.Date payTime;

	/**
	 * 绑定状态(0:已释放,1:绑定中)
	 */
	private Integer state;

	/**
	 * 绑定时的信用
	 */
	private Integer bindScores;

	/**
	 * 解绑时的信用
	 */
	private Integer unBindScores;

	/**
	 * 解绑原因
	 */
	private String unBindReason;

	private java.util.Date createTime;
}
