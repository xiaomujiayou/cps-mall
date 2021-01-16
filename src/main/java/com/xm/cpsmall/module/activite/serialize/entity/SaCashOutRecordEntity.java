package com.xm.cpsmall.module.activite.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sa_cash_out_record")
public class SaCashOutRecordEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 用户ID
	 */
	private Integer userId;

	private String openId;

	/**
	 * 提现流水号
	 */
	private String cashSn;

	/**
	 * 提现的账单id(","号间隔)
	 */
	private String billIds;

	/**
	 * 总提现金额
	 */
	private Integer money;

	/**
	 * 提现状态(1:待审核,2:提现成功,3:提现失败)
	 */
	private Integer state;

	/**
	 * 失败原因
	 */
	private String failReason;

	/**
	 * ip地址
	 */
	private String ip;

	private java.util.Date createTime;
}
