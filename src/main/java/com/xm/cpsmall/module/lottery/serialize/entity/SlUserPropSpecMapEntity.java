package com.xm.cpsmall.module.lottery.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sl_user_prop_spec_map")
public class SlUserPropSpecMapEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 道具规格id
	 */
	private Integer propSpecId;

	/**
	 * 用户id
	 */
	private Integer userId;

	private java.util.Date createTime;
}
