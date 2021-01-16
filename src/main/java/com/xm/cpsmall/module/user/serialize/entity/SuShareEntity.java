package com.xm.cpsmall.module.user.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_share")
public class SuShareEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 所属用户
	 */
	private Integer userId;

	/**
	 * 商品id
	 */
	private String goodsId;

	/**
	 * 所属平台
	 */
	private Integer platformType;

	/**
	 * 商品浏览次数
	 */
	private Integer watch;

	/**
	 * 商品下单次数
	 */
	private Integer sell;

	/**
	 * 预估收入(分)
	 */
	private Integer willMakeMoney;

	/**
	 * 删除(0:删除,1:未删除)
	 */
	private Integer del;

	/**
	 * 商品信息快照(json)
	 */
	private String goodsInfo;

	/**
	 * 更新时间
	 */
	private java.util.Date updateTime;

	/**
	 * 创建时间
	 */
	private java.util.Date createTime;
}
