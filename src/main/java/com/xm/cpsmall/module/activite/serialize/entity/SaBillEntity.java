package com.xm.cpsmall.module.activite.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sa_bill")
public class SaBillEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 所属用户
	 */
	private Integer userId;

	private String openId;

	private String ip;

	/**
	 * 所属活动
	 */
	private Integer activeId;

	/**
	 * 账单类型(1:现金)
	 */
	private Integer type;

	/**
	 * 奖励金额(分)
	 */
	private Integer money;

	/**
	 * 账单生成相关数据
	 */
	private String attach;

	/**
	 * 相关简介
	 */
	private String attachDes;

	/**
	 * 账单状态(0:待确认,1:待提现,2:审核中,3:已发放,4:发放失败,5:被风控,6:等待确认收货)
	 */
	private Integer state;

	/**
	 * 失败原因
	 */
	private String failReason;

	private java.util.Date createTime;
}
