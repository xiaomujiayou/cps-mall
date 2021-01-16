package com.xm.cpsmall.module.cron.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sc_order_state_record")
public class ScOrderStateRecordEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 订单号
	 */
	private String orderSn;

	/**
	 * 子订单号
	 */
	private String orderSubSn;

	/**
	 * 所属平台
	 */
	private Integer platformType;

	/**
	 * 订单原始状态
	 */
	private String originState;

	/**
	 * 原始状态简介
	 */
	private String originStateDes;

	/**
	 * 解析后的状态(0:未支付,1:已支付,2:确认收货,3:已结算,4:结算失败)
	 */
	private Integer state;

	/**
	 * 商品原始报文(json格式)
	 */
	private String res;

	private java.util.Date updateTime;

	private java.util.Date createTime;
}
