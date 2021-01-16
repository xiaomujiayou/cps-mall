package com.xm.cpsmall.module.activite.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sa_active")
public class SaActiveEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 活动名称
	 */
	private String name;

	/**
	 * 活动描述
	 */
	private String des;

	/**
	 * 活动banner图
	 */
	private String bannerImg;

	/**
	 * 活动详情地址
	 */
	private String detailUrl;

	/**
	 * 活动演示
	 */
	private String videoStudyUrl;

	/**
	 * 活动类型(1:商品优惠,2:推广奖励)
	 */
	private Integer type;

	/**
	 * 活动状态(0:下线,1:上线)
	 */
	private Integer state;

	/**
	 * 触发活动的事件(多个","号间隔)
	 */
	private String userAction;

	/**
	 * 奖励类型(1:现金)
	 */
	private Integer rewardType;

	/**
	 * 活动奖励(分,参考值)
	 */
	private Integer money;

	/**
	 * 排序权重(倒序)
	 */
	private Integer weight;

	/**
	 * 间隔天数
	 */
	private Integer days;

	/**
	 * 次数
	 */
	private Integer times;

	private java.util.Date createTime;
}
