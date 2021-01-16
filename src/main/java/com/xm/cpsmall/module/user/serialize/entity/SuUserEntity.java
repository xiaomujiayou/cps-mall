package com.xm.cpsmall.module.user.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_user")
public class SuUserEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String userSn;

	/**
	 * 父用户id
	 */
	private Integer parentId;

	/**
	 * 微信昵称
	 */
	private String nickname;

	/**
	 * 头像
	 */
	private String headImg;

	/**
	 * 性别(0:未知,1:男,2:女)
	 */
	private Integer sex;

	private String openId;

	/**
	 * 手机号
	 */
	private String tel;

	/**
	 * 账号状态(0:封禁,1:正常)
	 */
	private Integer state;

	/**
	 * 封禁理由
	 */
	private String reason;

	/**
	 * 城市
	 */
	private String city;

	/**
	 * 省
	 */
	private String province;

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 语言
	 */
	private String language;

	/**
	 * 推广位id
	 */
	private Integer pid;

	/**
	 * 用户来源
	 */
	private String fromWhare;

	/**
	 * 注册ip
	 */
	private String registerIp;

	/**
	 * 当前IP
	 */
	private String currentIp;

	/**
	 * 最后登录时间
	 */
	private java.util.Date lastLogin;

	private java.util.Date createTime;
}
