package com.xm.cpsmall.module.cron.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sc_wait_pay_bill")
public class ScWaitPayBillEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer userId;

	private String openId;

	private Integer billId;

	private Integer money;

	/**
	 * 处理状态(1:待发放,2:已发放)
	 */
	private Integer state;

	private java.util.Date createTime;
}
