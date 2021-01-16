package com.xm.cpsmall.module.mall.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sm_config")
public class SmConfigEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 键
	 */
	private String name;

	/**
	 * 值
	 */
	private String val;

	/**
	 * 简介
	 */
	private String des;

	private java.util.Date createTime;
}
