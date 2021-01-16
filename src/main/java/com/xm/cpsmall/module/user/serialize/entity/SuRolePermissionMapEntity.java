package com.xm.cpsmall.module.user.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_role_permission_map")
public class SuRolePermissionMapEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 角色id
	 */
	private Integer roleId;

	/**
	 * 权限id
	 */
	private Integer permissionId;

	private java.util.Date createTime;
}
