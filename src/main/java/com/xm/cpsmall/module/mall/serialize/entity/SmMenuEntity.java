package com.xm.cpsmall.module.mall.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sm_menu")
public class SmMenuEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 所属app
	 */
	private Integer appType;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 菜单类型(1:底部导航tab,2:设置菜单)
	 */
	private Integer type;

	/**
	 * 点击前图片链接
	 */
	private String beforeImg;

	/**
	 * 点击后图片链接
	 */
	private String afterImg;

	/**
	 * 目标(1:普通url跳转,2:小程序跳转,3:唤醒其他小程序,4:唤醒app,5:客服消息)
	 */
	private Integer target;

	/**
	 * 跳转链接
	 */
	private String url;

	/**
	 * 排序(从大到小)
	 */
	private Integer sort;

	/**
	 * 是否可用(0:不可用,1：可用)
	 */
	private Integer disable;

	private java.util.Date createTime;
}
