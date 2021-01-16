package com.xm.cpsmall.module.wind.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sw_login_record")
public class SwLoginRecordEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 用户ID
	 */
	private Integer userId;

	/**
	 * app类型
	 */
	private Integer appType;

	/**
	 * 登录ip
	 */
	private String ip;

	/**
	 * ip地址解析
	 */
	private String ipAddr;

	/**
	 * 浏览器UA
	 */
	private String ua;

	private java.util.Date createTime;
}
