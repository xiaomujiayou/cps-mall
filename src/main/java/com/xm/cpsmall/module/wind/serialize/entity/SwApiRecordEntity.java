package com.xm.cpsmall.module.wind.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sw_api_record")
public class SwApiRecordEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 用户id
	 */
	private Integer userId;

	/**
	 * app类型
	 */
	private Integer appType;

	/**
	 * ip地址
	 */
	private String ip;

	/**
	 * ip解析
	 */
	private String ipAddr;

	/**
	 * 浏览器ua
	 */
	private String ua;

	/**
	 * 请求链接
	 */
	private String url;

	/**
	 * 请求类型
	 */
	private String method;

	/**
	 * 请求参数
	 */
	private String param;

	/**
	 * 响应结果
	 */
	private String result;

	/**
	 * 执行时间(毫秒)
	 */
	private Integer time;

	private java.util.Date createTime;
}
