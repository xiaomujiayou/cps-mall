package com.xm.cpsmall.module.pay.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sp_wx_order_notify")
public class SpWxOrderNotifyEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 微信分配的小程序ID
	 */
	private String appid;

	/**
	 * 微信支付分配的商户号
	 */
	private String mchId;

	/**
	 * 商户订单号
	 */
	private String outTradeNo;

	/**
	 * 订单金额
	 */
	private Integer totalFee;

	/**
	 * 附加数据
	 */
	private String attach;

	/**
	 * 微信支付分配的终端设备号
	 */
	private String deviceInfo;

	/**
	 * 随机字符串，不长于32位
	 */
	private String nonceStr;

	/**
	 * 签名
	 */
	private String sign;

	/**
	 * 签名类型
	 */
	private String signType;

	private String openid;

	/**
	 * 是否关注公众账号
	 */
	private String isSubscribe;

	/**
	 * 交易类型
	 */
	private String tradeType;

	/**
	 * 付款银行
	 */
	private String bankType;

	/**
	 * 应结订单金额
	 */
	private Integer settlementTotalFee;

	/**
	 * 货币种类
	 */
	private String feeType;

	/**
	 * 现金支付金额
	 */
	private Integer cashFee;

	/**
	 * 现金支付货币类型
	 */
	private String cashFeeType;

	/**
	 * 总代金券金额
	 */
	private Integer couponFee;

	/**
	 * 代金券使用数量
	 */
	private Integer couponCount;

	/**
	 * 微信支付订单号
	 */
	private String transactionId;

	/**
	 * 支付完成时间
	 */
	private String timeEnd;

	/**
	 * 业务结果
	 */
	private String resultCode;

	/**
	 * 错误代码
	 */
	private String errCode;

	/**
	 * 错误代码描述
	 */
	private String errCodeDes;

	/**
	 * 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	 */
	private String returnCode;

	/**
	 * 返回信息
	 */
	private String returnMsg;

	private java.util.Date createTime;
}
