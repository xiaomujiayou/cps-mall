package com.xm.cpsmall.module.cron.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sc_order_sync_history")
public class ScOrderSyncHistoryEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 所属平台
	 */
	private Integer platformType;

	/**
	 * 查询开始时间
	 */
	private java.util.Date startTime;

	/**
	 * 查询结束时间
	 */
	private java.util.Date endTime;

	/**
	 * 数据量
	 */
	private Integer total;

	/**
	 * 当前页
	 */
	private Integer currentPage;

	/**
	 * 页面大小
	 */
	private Integer pageSize;

	private java.util.Date createTime;
}
