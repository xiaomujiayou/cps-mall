package com.xm.cpsmall.module.pay.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sp_wx_order_in")
public class SpWxOrderInEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 商户订单号
	 */
	private String outTradeNo;

	/**
	 * 用户标识(trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识)
	 */
	private String openid;

	/**
	 * 用户所属账单
	 */
	private Integer suBillId;

	/**
	 * 小程序id
	 */
	private String appid;

	/**
	 * 微信支付商户号
	 */
	private String mchId;

	/**
	 * 商品ID(trade_type=NATIVE时，此参数必传。此参数为二维码中包含的商品ID，商户自行定义。)
	 */
	private String productId;

	/**
	 * 附加数据
	 */
	private String attach;

	/**
	 * 订单总金额(单位为分)
	 */
	private Integer totalFee;

	/**
	 * 自定义参数(设备号)
	 */
	private String deviceInfo;

	/**
	 * 随机字符串
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

	/**
	 * 商品简单描述
	 */
	private String body;

	/**
	 * 商品详情
	 */
	private String detail;

	/**
	 * 符合ISO 4217标准的三位字母代码，默认人民币：CNY
	 */
	private String feeType;

	/**
	 * 终端IP(支持IPV4和IPV6两种格式的IP地址。调用微信支付API的机器IP)
	 */
	private String spbillCreateIp;

	/**
	 * 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
	 */
	private String timeStart;

	/**
	 * 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id
	 */
	private String timeExpire;

	/**
	 * 订单优惠标记，使用代金券或立减优惠功能时需要的参数
	 */
	private String goodsTag;

	/**
	 * 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
	 */
	private String notifyUrl;

	/**
	 * 交易类型(小程序取值如下：JSAPI)
	 */
	private String tradeType;

	/**
	 * 指定支付方式(上传此参数no_credit--可限制用户不能使用信用卡支付)
	 */
	private String limitPay;

	/**
	 * 电子发票入口开放标识
	 */
	private String receipt;

	/**
	 * 场景信息
	 */
	private String sceneInfo;

	/**
	 * 支付状态(0:待支付,1:支付成功,2:支付取消)
	 */
	private Integer state;

	/**
	 * 错误信息
	 */
	private String errMsg;

	/**
	 * 业务对象(SuBillToPayBo)
	 */
	private String reqBo;

	/**
	 * 预支付交易会话标识
	 */
	private String packageVal;

	private java.util.Date createTime;
}
