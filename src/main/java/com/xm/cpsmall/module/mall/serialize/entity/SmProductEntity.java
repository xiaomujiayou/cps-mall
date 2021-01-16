package com.xm.cpsmall.module.mall.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sm_product")
public class SmProductEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 商品类型(1:拼多多,2:京东,3:淘宝,4:蘑菇街)
	 */
	private Integer type;

	/**
	 * 商品id
	 */
	private String goodsId;

	/**
	 * 商品缩略图
	 */
	private String goodsThumbnailUrl;

	/**
	 * 商品轮播图
	 */
	private String goodsGalleryUrls;

	/**
	 * 商品标题
	 */
	private String name;

	/**
	 * 商品描述
	 */
	private String des;

	/**
	 * 原始价格(分)
	 */
	private Integer originalPrice;

	/**
	 * 优惠券价格(分)
	 */
	private Integer couponPrice;

	/**
	 * 优惠券使用门槛(分)
	 */
	private Integer couponStartFee;

	/**
	 * 返现价格(分)
	 */
	private Integer cashPrice;

	/**
	 * 店铺id
	 */
	private String mallId;

	/**
	 * 店铺名称
	 */
	private String mallName;

	/**
	 * 店铺类型
	 */
	private String mallType;

	/**
	 * 所在店铺是否参与全店推广，0：否，1：是
	 */
	private Integer mallCps;

	/**
	 * 商品售卖数量
	 */
	private String salesTip;

	/**
	 * 是否有优惠券(0:没有,1:有)
	 */
	private Integer hasCoupon;

	/**
	 * 优惠券id(蘑菇街专用)
	 */
	private String couponId;

	/**
	 * 佣金比例(万分比)
	 */
	private Integer promotionRate;

	/**
	 * 服务标签
	 */
	private String serviceTags;

	/**
	 * 活动类型
	 */
	private String activityType;

	/**
	 * 类目id
	 */
	private String optId;

	/**
	 * 所有类目id
	 */
	private String optIds;

	/**
	 * 淘宝购买链接
	 */
	private String tbBuyUrl;

	/**
	 * 商品折扣(唯品会)
	 */
	private String discount;

	/**
	 * 好评率(唯品会)
	 */
	private String commentsRate;

	/**
	 * 品牌LOGO链接(唯品会)
	 */
	private String brandLogoUrl;

	/**
	 * 品牌名称
	 */
	private String brandName;

	/**
	 * 创建时间
	 */
	private java.util.Date createTime;
}
