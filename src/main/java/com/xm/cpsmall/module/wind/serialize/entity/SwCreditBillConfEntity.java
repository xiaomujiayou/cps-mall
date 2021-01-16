package com.xm.cpsmall.module.wind.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sw_credit_bill_conf")
public class SwCreditBillConfEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 分数
	 */
	private Integer scores;

	/**
	 * 可以预支的次数
	 */
	private Integer advanceCount;

	/**
	 * 最大预支的总额度(分)
	 */
	private Integer maxQuota;

	/**
	 * 每笔限额
	 */
	private Integer quota;

	/**
	 * 打款间隔(天)
	 */
	private Integer payDays;

	private java.util.Date createTime;
}
