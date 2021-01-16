package com.xm.cpsmall.module.mall.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sm_help_page")
public class SmHelpPageEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 排序(由大到小)
	 */
	private Integer sort;

	/**
	 * 是否显示(0:不显示,1:显示)
	 */
	private Integer disable;

	private java.util.Date createTime;
}
