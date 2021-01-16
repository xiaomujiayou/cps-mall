package com.xm.cpsmall.module.lottery.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sl_prop")
public class SlPropEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 道具类型(1:时间类,2:计量类)
	 */
	private Integer type;

	/**
	 * 道具名称
	 */
	private String name;

	/**
	 * 道具图片链接
	 */
	private String imgUrl;

	/**
	 * 道具详情(富文本)
	 */
	private String detail;

	/**
	 * 道具简介
	 */
	private String des;

	/**
	 * 规格简介
	 */
	private String specDes;

	/**
	 * 上下架(0:下架,1:上架)
	 */
	private Integer onSale;

	/**
	 * 道具计量单位(1:天,2:个)
	 */
	private Integer unitType;

	/**
	 * 最大持有数量
	 */
	private Integer maxUnit;

	/**
	 * 参考价/分
	 */
	private Integer price;

	private java.util.Date createTime;
}
