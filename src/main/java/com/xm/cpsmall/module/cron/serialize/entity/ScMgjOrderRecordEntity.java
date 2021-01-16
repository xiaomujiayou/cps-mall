package com.xm.cpsmall.module.cron.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sc_mgj_order_record")
public class ScMgjOrderRecordEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 订单编号
	 */
	private String orderSn;

	/**
	 * 子订单编号
	 */
	private String orderSubSn;

	/**
	 * 订单状态
	 */
	private Integer state;

	/**
	 * 最后更新时间
	 */
	private java.util.Date lastUpdate;

	private java.util.Date createTime;
}
