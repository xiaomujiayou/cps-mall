package com.xm.cpsmall.module.user.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_product")
public class SuProductEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String ip;

	private Integer appType;

	/**
	 * 商品id
	 */
	private String goodsId;

	/**
	 * 所属平台
	 */
	private Integer platformType;

	/**
	 * 所属用户
	 */
	private Integer userId;

	/**
	 * 是否被搜藏(0:否,1:是)
	 */
	private Integer isCollect;

	/**
	 * 分享用户id
	 */
	private Integer shareUserId;

	/**
	 * 优惠券id(淘宝用)
	 */
	private String couponId;

	/**
	 * 商品信息快照(json格式)
	 */
	private String goodsInfo;

	/**
	 * 是否删除(0:已删除,1:未删除)
	 */
	private Integer del;

	/**
	 * 搜藏时间
	 */
	private java.util.Date collectTime;

	private java.util.Date updateTime;

	private java.util.Date createTime;
}
