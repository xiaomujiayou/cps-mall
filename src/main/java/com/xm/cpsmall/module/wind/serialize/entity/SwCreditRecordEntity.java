package com.xm.cpsmall.module.wind.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sw_credit_record")
public class SwCreditRecordEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 用户id
	 */
	private Integer userId;

	/**
	 * 分数类型(0:新用户注册,1:每日登录,2:邀请好友,3:订单交易成功,4:恶意退单)
	 */
	private Integer type;

	/**
	 * 分数变动
	 */
	private Integer changeScores;

	/**
	 * 改变后的分数
	 */
	private Integer scores;

	/**
	 * 改变原因
	 */
	private String changeReason;

	/**
	 * 附件数据(订单sn)
	 */
	private String attached;

	/**
	 * 创建时间戳
	 */
	private String createStamp;

	private java.util.Date createTime;
}
