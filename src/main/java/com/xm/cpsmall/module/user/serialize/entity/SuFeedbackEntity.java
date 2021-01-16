package com.xm.cpsmall.module.user.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_feedback")
public class SuFeedbackEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 所属用户
	 */
	private Integer userId;

	/**
	 * 问题描述
	 */
	private String des;

	/**
	 * 相关图片
	 */
	private String images;

	/**
	 * 意见类型(1:默认)
	 */
	private Integer type;

	/**
	 * 状态(1:待处理,2:已处理)
	 */
	private Integer state;

	/**
	 * 是否采纳(0:默认,1:未采纳,2:已采纳)
	 */
	private Integer accept;

	/**
	 * 回复
	 */
	private String reply;

	private java.util.Date createTime;
}
