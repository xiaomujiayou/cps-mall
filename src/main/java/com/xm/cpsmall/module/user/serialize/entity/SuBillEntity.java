package com.xm.cpsmall.module.user.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_bill")
public class SuBillEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 所属用户
	 */
	private Integer userId;

	/**
	 * 账单流水
	 */
	private String billSn;

	/**
	 * 账单来源用户，因为哪个用户而产生的订单(订单类型为2/3时)
	 */
	private Integer fromUserId;

	/**
	 * 账单金额(分)
	 */
	private Integer money;

	/**
	 * 账单类型(1:普通自购,2:代理收益,3:分享自购,4:分享收益,5:购买道具)
	 */
	private Integer type;

	/**
	 * 相关id(账单类型为[1,2,3,4]为orderId,5:道具规格id)
	 */
	private Integer attach;

	/**
	 * 佣金比例(千分比 账单类型为[1,2,3,4])
	 */
	private Integer promotionRate;

	/**
	 * 账单状态(1:等待确认,2:准备发放,3:已发放,4:已失效,5:已处理,6:待支付,7:已支付)
	 */
	private Integer state;

	/**
	 * 收支类型(1:收入账单,2:支出)
	 */
	private Integer income;

	/**
	 * 账单简介(微信支付body)
	 */
	private String des;

	/**
	 * 账单失效原因
	 */
	private String failReason;

	/**
	 * 信用支付状态(0:非信用支付,1:达到信用支付条件,3:信用支付失败)
	 */
	private Integer creditState;

	/**
	 * 打款时间
	 */
	private java.util.Date payTime;

	/**
	 * 最后更新时间
	 */
	private java.util.Date updateTime;

	private java.util.Date createTime;
}
