package com.xm.cpsmall.module.lottery.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sl_prop_spec")
public class SlPropSpecEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 所属道具id
	 */
	private Integer propId;

	/**
	 * 规格名称
	 */
	private String name;

	/**
	 * 规格类型(1:购买,2:赠送)
	 */
	private Integer type;

	/**
	 * 原价(分)
	 */
	private Integer originalPrice;

	/**
	 * 现价(分)
	 */
	private Integer price;

	/**
	 * 简介
	 */
	private String des;

	/**
	 * 该规格下产品单元数
	 */
	private Integer unit;

	/**
	 * 在售(0:下架,1:在售,2:禁用)
	 */
	private Integer onSale;

	/**
	 * 是否选中(0:否,1:是)
	 */
	private Integer choose;

	private java.util.Date createTime;
}
