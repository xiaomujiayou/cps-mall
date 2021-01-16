package com.xm.cpsmall.module.user.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_menu_tips")
public class SuMenuTipsEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 所属用户id
	 */
	private Integer userId;

	/**
	 * 菜单id
	 */
	private Integer menuId;

	/**
	 * 是否显示小红点(0:否,1:是)
	 */
	private Integer hot;

	/**
	 * 显示数字(少于四位数)
	 */
	private Integer num;

	private java.util.Date updateTime;

	private java.util.Date createTime;
}
