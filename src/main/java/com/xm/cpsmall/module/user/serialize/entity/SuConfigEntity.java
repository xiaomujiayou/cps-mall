package com.xm.cpsmall.module.user.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_config")
public class SuConfigEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 用户id
	 */
	private Integer userId;

	/**
	 * 配置类型
	 */
	private String name;

	/**
	 * 配置值
	 */
	private String val;

	/**
	 * 创建日期
	 */
	private java.util.Date createTime;

	/**
	 * 更新日期
	 */
	private java.util.Date updateTime;
}
