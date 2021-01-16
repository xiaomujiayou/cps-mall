package com.xm.cpsmall.module.pay.serialize.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sp_wx_ent_pay_order_in")
public class SpWxEntPayOrderInEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer billPayId;

	/**
	 * 所属用户
	 */
	private Integer userId;

	/**
	 * 记录类型(1:订单,2:活动)
	 */
	private Integer type;

	/**
	 * 包含的订单id
	 */
	private String billIds;

	/**
	 * 申请商户号的appid或商户号绑定的appid
	 */
	private String mchAppid;

	/**
	 * 微信支付分配的商户号
	 */
	private String mchid;

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
	 * 商户订单号
	 */
	private String partnerTradeNo;

	/**
	 * openid
	 */
	private String openid;

	/**
	 * NO_CHECK：不校验真实姓名
	 */
	private String checkName;

	/**
	 * 收款用户真实姓名。
	 */
	private String reUserName;

	/**
	 * 企业付款金额，单位为分
	 */
	private Integer amount;

	/**
	 * 企业付款备注
	 */
	private String des;

	/**
	 * 该IP同在商户平台设置的IP白名单中的IP没有关联，该IP可传用户端或者服务端的IP
	 */
	private String spbillCreateIp;

	/**
	 * SUCCESS/FAIL
	 */
	private String returnCode;

	/**
	 * 返回信息，如非空，为错误原因
	 */
	private String returnMsg;

	/**
	 * SUCCESS/FAIL，注意：当状态为FAIL时，存在业务结果未明确的情况。如果状态为FAIL，请务必关注错误代码（err_code字段），通过查询查询接口确认此次付款的结果。
	 */
	private String resultCode;

	/**
	 * 错误码信息，注意：出现未明确的错误码时（SYSTEMERROR等），请务必用原商户订单号重试，或通过查询接口确认此次付款的结果。
	 */
	private String errCode;

	/**
	 * 结果信息描述
	 */
	private String errCodeDes;

	/**
	 * 企业付款成功，返回的微信付款单号
	 */
	private String paymentNo;

	/**
	 * 企业付款成功时间
	 */
	private String paymentTime;

	/**
	 * 状态(1:付款成功,2:付款失败)
	 */
	private Integer state;

	private java.util.Date createTime;
}
