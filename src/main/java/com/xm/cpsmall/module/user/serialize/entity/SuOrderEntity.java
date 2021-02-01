package com.xm.cpsmall.module.user.serialize.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_order")
public class SuOrderEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 所属用户
	 */
	private Integer userId;

	/**
	 * 订单编号
	 */
	private String orderSn;

	/**
	 * 子订单号
	 */
	private String orderSubSn;

	/**
	 * 商品id
	 */
	private String productId;

	/**
	 * 商品名称
	 */
	private String productName;

	/**
	 * 商品简介
	 */
	private String imgUrl;

	/**
	 * 所属平台
	 */
	private Integer platformType;

	/**
	 * 订单状态(-1:无效订单,0:未支付,1:已支付,2:确认收货,3:已结算,4:结算失败)
	 */
	private Integer state;

	/**
	 * 审核失败原因
	 */
	private String failReason;

	/**
	 * 推广位id
	 */
	private String pId;

	/**
	 * 单品原始价格(分)
	 */
	private Integer originalPrice;

	/**
	 * 购买数量
	 */
	private Integer quantity;

	/**
	 * 实际支付金额(分)
	 */
	private Integer amount;

	/**
	 * 佣金比例(千分比)
	 */
	private Integer promotionRate;

	/**
	 * 佣金(分)
	 */
	private Integer promotionAmount;

	/**
	 * 比价状态(0:正常,2:比价)
	 */
	private Integer priceCompareStatus;

	/**
	 * 订单类型(0:领券页面,1:红包页,2:领券页,3:题页)
	 */
	private Integer type;

	/**
	 * 来源类型(1:普通自购,2:分享自购)
	 */
	private Integer formType;

	/**
	 * 分享订单分享的用户id
	 */
	private Integer shareUserId;

	/**
	 * 订单来源(1:微信小程序)
	 */
	private Integer fromApp;

	/**
	 * 自定义参数(userId:购买用户,shareUserId:分享的用户,fromApp:app来源)
	 */
	private String customParameters;

	/**
	 * 购物车所有商品ID(,号分割)
	 */
	private String cart;

	/**
	 * 最后更新时间
	 */
	private java.util.Date orderModifyAt;

	/**
	 * 创建时间
	 */
	private java.util.Date createTime;
}
