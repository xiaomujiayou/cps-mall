package com.xm.cpsmall.module.user.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_summary")
public class SuSummaryEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 所属用户
	 */
	private Integer userId;

	/**
	 * 今日收益(分)
	 */
	private Integer profitToday;

	/**
	 * 今日收益最后一次更新日期
	 */
	private String profitTodayLastUpdate;

	/**
	 * 历史收益
	 */
	private Integer profitHistory;

	/**
	 * 等待确认
	 */
	private Integer profitWait;

	/**
	 * 准备发放
	 */
	private Integer profitReady;

	/**
	 * 已返
	 */
	private Integer profitCash;

	/**
	 * 使用优惠劵总量
	 */
	private Integer totalCoupon;

	/**
	 * 自购成交额
	 */
	private Integer totalBuy;

	/**
	 * 分享成交额
	 */
	private Integer totalShare;

	/**
	 * 锁定用户总量
	 */
	private Integer totalProxyUser;

	/**
	 * 最后更新时间
	 */
	private java.util.Date updateTime;

	/**
	 * 创建时间
	 */
	private java.util.Date createTime;
}
