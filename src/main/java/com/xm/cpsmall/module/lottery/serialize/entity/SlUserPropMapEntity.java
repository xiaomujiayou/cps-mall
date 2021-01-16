package com.xm.cpsmall.module.lottery.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sl_user_prop_map")
public class SlUserPropMapEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 用户id
	 */
	private Integer userId;

	/**
	 * 道具id
	 */
	private Integer propId;

	/**
	 * 剩余道具单元数量
	 */
	private Integer propUnitCurrent;

	/**
	 * 历史总量
	 */
	private Integer propUnitTotal;

	/**
	 * 更新时间
	 */
	private java.util.Date updateTime;

	private java.util.Date createTime;
}
