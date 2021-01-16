package com.xm.cpsmall.module.mall.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sm_opt")
public class SmOptEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 标签名称
	 */
	private String name;

	/**
	 * 拼多多对应optid
	 */
	private String pddOptId;

	/**
	 * 京东对应optid
	 */
	private String jdOptId;

	/**
	 * 蘑菇街对应optid
	 */
	private String mgjOptId;

	/**
	 * 淘宝对应optid
	 */
	private String tbOptId;

	/**
	 * 父opt_id
	 */
	private Integer parentId;

	/**
	 * 级别(从1开始)
	 */
	private Integer level;

	/**
	 * 简介图
	 */
	private String img;

	/**
	 * 排序(从小到大)
	 */
	private Integer sort;

	/**
	 * 是否可用(0:不可用,1:可用)
	 */
	private Integer disable;

	private java.util.Date createTime;
}
