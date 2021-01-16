package com.xm.cpsmall.module.wind.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sw_credit_conf")
public class SwCreditConfEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 配置名称
	 */
	private String name;

	/**
	 * 配置值
	 */
	private String val;

	/**
	 * 配置简介
	 */
	private String des;

	private java.util.Date createTime;
}
