package com.xm.cpsmall.module.user.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_user_role_map")
public class SuUserRoleMapEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 用户id
	 */
	private Integer userId;

	/**
	 * 角色id
	 */
	private Integer roleId;

	/**
	 * 创建者
	 */
	private String creator;

	/**
	 * 创建原因
	 */
	private String reason;

	private java.util.Date createTime;
}
