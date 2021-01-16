package com.xm.cpsmall.module.user.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_admin")
public class SuAdminEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 密码(MD5)
	 */
	private String password;

	/**
	 * 头像
	 */
	private String headImg;

	private java.util.Date createTime;
}
