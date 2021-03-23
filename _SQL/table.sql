/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 5.7.28 : Database - cps_mall
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`cps_mall` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

/*Table structure for table `sa_active` */

DROP TABLE IF EXISTS `sa_active`;

CREATE TABLE `sa_active` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '活动表',
  `name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '活动名称',
  `des` text COLLATE utf8mb4_unicode_ci COMMENT '活动描述',
  `banner_img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '活动banner图',
  `detail_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '活动详情地址',
  `video_study_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '活动演示',
  `type` int(1) DEFAULT NULL COMMENT '活动类型(1:商品优惠,2:推广奖励,3:任务奖励)',
  `state` int(1) DEFAULT NULL COMMENT '活动状态(0:下线,1:上线)',
  `user_action` text COLLATE utf8mb4_unicode_ci COMMENT '触发活动的事件(多个","号间隔)',
  `reward_type` int(1) DEFAULT NULL COMMENT '奖励类型(1:现金)',
  `money` int(11) DEFAULT NULL COMMENT '活动奖励(分,参考值)',
  `weight` int(11) DEFAULT NULL COMMENT '排序权重(倒序)',
  `days` int(11) DEFAULT NULL COMMENT '间隔天数',
  `times` int(11) DEFAULT NULL COMMENT '次数',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sa_bill` */

DROP TABLE IF EXISTS `sa_bill`;

CREATE TABLE `sa_bill` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '活动账单',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `open_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ip` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `active_id` int(11) DEFAULT NULL COMMENT '所属活动',
  `type` int(1) DEFAULT NULL COMMENT '账单类型(1:现金)',
  `money` int(11) DEFAULT NULL COMMENT '奖励金额(分)',
  `attach` longtext COLLATE utf8mb4_unicode_ci COMMENT '账单生成相关数据',
  `attach_des` text COLLATE utf8mb4_unicode_ci COMMENT '相关简介',
  `state` int(1) DEFAULT NULL COMMENT '账单状态(0:待确认,1:待提现,2:审核中,3:已发放,4:发放失败,5:被风控,6:等待确认收货)',
  `fail_reason` text COLLATE utf8mb4_unicode_ci COMMENT '失败原因',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=945 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sa_cash_out_record` */

DROP TABLE IF EXISTS `sa_cash_out_record`;

CREATE TABLE `sa_cash_out_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '活动奖励提现记录表',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `open_id` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cash_sn` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '提现流水号',
  `bill_ids` longtext COLLATE utf8mb4_unicode_ci COMMENT '提现的账单id(","号间隔)',
  `money` int(11) DEFAULT NULL COMMENT '总提现金额',
  `state` int(1) DEFAULT NULL COMMENT '提现状态(1:待审核,2:提现成功,3:提现失败)',
  `fail_reason` text COLLATE utf8mb4_unicode_ci COMMENT '失败原因',
  `ip` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ip地址',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sa_config` */

DROP TABLE IF EXISTS `sa_config`;

CREATE TABLE `sa_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '键',
  `val` text COLLATE utf8mb4_unicode_ci COMMENT '值',
  `des` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简介',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sc_bill_pay` */

DROP TABLE IF EXISTS `sc_bill_pay`;

CREATE TABLE `sc_bill_pay` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `open_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户标识',
  `bill_ids` longtext COLLATE utf8mb4_unicode_ci COMMENT '相关账单',
  `total_money` int(11) DEFAULT NULL COMMENT '支付总额',
  `state` int(1) DEFAULT NULL COMMENT '状态(1:待处理,2:处理成功,3:处理失败)',
  `fail_reason` text COLLATE utf8mb4_unicode_ci COMMENT '失败原因',
  `process_id` int(11) DEFAULT NULL COMMENT '处理结果id',
  `process_sn` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '支付单号',
  `pay_sys_sn` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '支付平台单号',
  `update_time` datetime NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sc_mgj_order_record` */

DROP TABLE IF EXISTS `sc_mgj_order_record`;

CREATE TABLE `sc_mgj_order_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '蘑菇街订单记录',
  `order_sn` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单编号',
  `order_sub_sn` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '子订单编号',
  `state` int(1) DEFAULT NULL COMMENT '订单状态',
  `last_update` datetime NOT NULL COMMENT '最后更新时间',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sc_order_state_record` */

DROP TABLE IF EXISTS `sc_order_state_record`;

CREATE TABLE `sc_order_state_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单原始数据状态对照表',
  `order_sn` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单号',
  `order_sub_sn` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '子订单号',
  `platform_type` int(1) DEFAULT NULL COMMENT '所属平台',
  `origin_state` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单原始状态',
  `origin_state_des` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '原始状态简介',
  `state` int(11) DEFAULT NULL COMMENT '解析后的状态(0:未支付,1:已支付,2:确认收货,3:已结算,4:结算失败)',
  `res` longtext COLLATE utf8mb4_unicode_ci COMMENT '商品原始报文，最早的记录(json格式)',
  `update_time` datetime NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sc_order_sync_history` */

DROP TABLE IF EXISTS `sc_order_sync_history`;

CREATE TABLE `sc_order_sync_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `platform_type` int(11) DEFAULT NULL COMMENT '所属平台',
  `start_time` datetime DEFAULT NULL COMMENT '查询开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '查询结束时间',
  `total` int(11) DEFAULT NULL COMMENT '数据量',
  `current_page` int(11) DEFAULT NULL COMMENT '当前页',
  `page_size` int(11) DEFAULT NULL COMMENT '页面大小',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2381413 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sc_wait_pay_bill` */

DROP TABLE IF EXISTS `sc_wait_pay_bill`;

CREATE TABLE `sc_wait_pay_bill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `open_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bill_id` int(11) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  `state` int(1) DEFAULT NULL COMMENT '处理状态(1:待发放,2:发放中,3:已发放,4:发放失败)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sl_prop` */

DROP TABLE IF EXISTS `sl_prop`;

CREATE TABLE `sl_prop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(1) DEFAULT NULL COMMENT '道具类型(1:时间类,2:计量类)',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '道具名称',
  `img_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '道具图片链接',
  `detail` text COLLATE utf8mb4_unicode_ci COMMENT '道具详情(富文本)',
  `des` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '道具简介',
  `spec_des` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '规格简介',
  `on_sale` int(1) DEFAULT NULL COMMENT '上下架(0:下架,1:上架)',
  `unit_type` int(1) DEFAULT NULL COMMENT '道具计量单位(1:天,2:个)',
  `max_unit` int(11) DEFAULT NULL COMMENT '最大持有数量',
  `price` int(11) DEFAULT NULL COMMENT '参考价/分',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sl_prop_spec` */

DROP TABLE IF EXISTS `sl_prop_spec`;

CREATE TABLE `sl_prop_spec` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `prop_id` int(11) DEFAULT NULL COMMENT '所属道具id',
  `name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '规格名称',
  `type` int(1) DEFAULT NULL COMMENT '规格类型(1:购买,2:赠送)',
  `original_price` int(11) DEFAULT NULL COMMENT '原价(分)',
  `price` int(11) DEFAULT NULL COMMENT '现价(分)',
  `des` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简介',
  `unit` int(11) DEFAULT NULL COMMENT '该规格下产品单元数',
  `on_sale` int(1) DEFAULT NULL COMMENT '在售(0:下架,1:在售,2:禁用)',
  `choose` int(11) DEFAULT NULL COMMENT '是否选中(0:否,1:是)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sl_user_prop_map` */

DROP TABLE IF EXISTS `sl_user_prop_map`;

CREATE TABLE `sl_user_prop_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户->道具 映射',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `prop_id` int(11) DEFAULT NULL COMMENT '道具id',
  `prop_unit_current` int(11) DEFAULT NULL COMMENT '剩余道具单元数量',
  `prop_unit_total` int(11) DEFAULT NULL COMMENT '历史总量',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sl_user_prop_spec_map` */

DROP TABLE IF EXISTS `sl_user_prop_spec_map`;

CREATE TABLE `sl_user_prop_spec_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `prop_spec_id` int(11) DEFAULT NULL COMMENT '道具规格id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sm_banner` */

DROP TABLE IF EXISTS `sm_banner`;

CREATE TABLE `sm_banner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_type` int(1) DEFAULT NULL COMMENT 'app类型',
  `platform_type` int(1) DEFAULT NULL,
  `type` int(1) DEFAULT NULL COMMENT '类型(1:首页轮播图,2:首页滑动列表)',
  `name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `url` text COLLATE utf8mb4_unicode_ci COMMENT '包含的信息',
  `img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片路径',
  `target` int(1) DEFAULT NULL COMMENT '目标(1:普通url跳转,2:小程序跳转,3:唤醒其他小程序,4:唤醒app,5:客服消息)',
  `app_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '目标小程序appId',
  `disable` int(1) DEFAULT '1' COMMENT '是否可用(0:不可用,1：可用)',
  `key_words` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关键字',
  `sort` int(11) DEFAULT NULL COMMENT '排序(从大到小)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sm_config` */

DROP TABLE IF EXISTS `sm_config`;

CREATE TABLE `sm_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '键',
  `val` text COLLATE utf8mb4_unicode_ci COMMENT '值',
  `des` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简介',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sm_help` */

DROP TABLE IF EXISTS `sm_help`;

CREATE TABLE `sm_help` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '页面多个","号间隔',
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `content` longtext COLLATE utf8mb4_unicode_ci COMMENT '富文本内容',
  `state` int(1) DEFAULT NULL COMMENT '状态(0:不可用,1:可用)',
  `width` int(11) DEFAULT NULL COMMENT '面板宽度',
  `height` int(11) DEFAULT NULL COMMENT '面板高度',
  `round` int(1) DEFAULT NULL COMMENT '是否为圆角(0:否,1:是)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sm_help_page` */

DROP TABLE IF EXISTS `sm_help_page`;

CREATE TABLE `sm_help_page` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '内容',
  `sort` int(2) DEFAULT NULL COMMENT '排序(由大到小)',
  `disable` int(1) DEFAULT NULL COMMENT '是否显示(0:不显示,1:显示)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sm_menu` */

DROP TABLE IF EXISTS `sm_menu`;

CREATE TABLE `sm_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '系统菜单',
  `app_type` int(1) DEFAULT NULL COMMENT '所属app',
  `name` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单名称',
  `type` int(1) DEFAULT NULL COMMENT '菜单类型(1:底部导航tab,2:设置菜单)',
  `before_img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '点击前图片链接',
  `after_img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '点击后图片链接',
  `target` int(1) DEFAULT NULL COMMENT '目标(1:普通url跳转,2:小程序跳转,3:唤醒其他小程序,4:唤醒app,5:客服消息)',
  `url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '跳转链接',
  `sort` int(11) DEFAULT NULL COMMENT '排序(从大到小)',
  `disable` int(1) DEFAULT NULL COMMENT '是否可用(0:不可用,1：可用)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sm_opt` */

DROP TABLE IF EXISTS `sm_opt`;

CREATE TABLE `sm_opt` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签名称',
  `pdd_opt_id` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '拼多多对应optid',
  `jd_opt_id` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '京东对应optid',
  `mgj_opt_id` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '蘑菇街对应optid',
  `tb_opt_id` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '淘宝对应optid',
  `parent_id` int(11) DEFAULT NULL COMMENT '父opt_id',
  `level` int(1) DEFAULT NULL COMMENT '级别(从1开始)',
  `img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简介图',
  `sort` int(11) DEFAULT '999' COMMENT '排序(从小到大)',
  `disable` int(1) DEFAULT '1' COMMENT '是否可用(0:不可用,1:可用)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7393 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sm_product` */

DROP TABLE IF EXISTS `sm_product`;

CREATE TABLE `sm_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(1) DEFAULT NULL COMMENT '商品类型(1:拼多多,2:京东,3:淘宝,4:蘑菇街)',
  `goods_id` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品id',
  `goods_thumbnail_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品缩略图',
  `goods_gallery_urls` text COLLATE utf8mb4_unicode_ci COMMENT '商品轮播图',
  `name` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品标题',
  `des` text COLLATE utf8mb4_unicode_ci COMMENT '商品描述',
  `original_price` int(11) DEFAULT NULL COMMENT '原始价格(分)',
  `coupon_price` int(11) DEFAULT NULL COMMENT '优惠券价格(分)',
  `coupon_start_fee` int(11) DEFAULT NULL COMMENT '优惠券使用门槛(分)',
  `cash_price` int(11) DEFAULT NULL COMMENT '返现价格(分)',
  `mall_id` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺id',
  `mall_name` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺名称',
  `mall_type` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '店铺类型',
  `mall_cps` int(1) DEFAULT NULL COMMENT '所在店铺是否参与全店推广，0：否，1：是',
  `sales_tip` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品售卖数量',
  `has_coupon` int(1) DEFAULT NULL COMMENT '是否有优惠券(0:没有,1:有)',
  `coupon_id` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '优惠券id(蘑菇街专用)',
  `promotion_rate` int(11) DEFAULT NULL COMMENT '佣金比例(万分比)',
  `service_tags` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '服务标签',
  `activity_type` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '活动类型',
  `opt_id` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类目id',
  `opt_ids` text COLLATE utf8mb4_unicode_ci COMMENT '所有类目id',
  `tb_buy_url` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '淘宝购买链接',
  `discount` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品折扣(唯品会)',
  `comments_rate` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '好评率(唯品会)',
  `brand_logo_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '品牌LOGO链接(唯品会)',
  `brand_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '品牌名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sp_wx_ent_pay_order_in` */

DROP TABLE IF EXISTS `sp_wx_ent_pay_order_in`;

CREATE TABLE `sp_wx_ent_pay_order_in` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bill_pay_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `type` int(1) DEFAULT NULL COMMENT '记录类型(1:订单,2:活动提现,3:活动账单)',
  `bill_ids` longtext COLLATE utf8mb4_unicode_ci COMMENT '包含的订单id',
  `mch_appid` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '申请商户号的appid或商户号绑定的appid',
  `mchid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信支付分配的商户号',
  `device_info` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信支付分配的终端设备号',
  `nonce_str` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '随机字符串，不长于32位',
  `sign` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '签名',
  `partner_trade_no` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商户订单号',
  `openid` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'openid',
  `check_name` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'NO_CHECK：不校验真实姓名',
  `re_user_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收款用户真实姓名。',
  `amount` int(11) DEFAULT NULL COMMENT '企业付款金额，单位为分',
  `des` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业付款备注',
  `spbill_create_ip` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '该IP同在商户平台设置的IP白名单中的IP没有关联，该IP可传用户端或者服务端的IP',
  `return_code` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'SUCCESS/FAIL',
  `return_msg` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '返回信息，如非空，为错误原因',
  `result_code` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'SUCCESS/FAIL，注意：当状态为FAIL时，存在业务结果未明确的情况。如果状态为FAIL，请务必关注错误代码（err_code字段），通过查询查询接口确认此次付款的结果。',
  `err_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '错误码信息，注意：出现未明确的错误码时（SYSTEMERROR等），请务必用原商户订单号重试，或通过查询接口确认此次付款的结果。',
  `err_code_des` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '结果信息描述',
  `payment_no` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业付款成功，返回的微信付款单号',
  `payment_time` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业付款成功时间',
  `state` int(1) DEFAULT NULL COMMENT '状态(1:付款成功,2:付款失败)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=259 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sp_wx_order_in` */

DROP TABLE IF EXISTS `sp_wx_order_in`;

CREATE TABLE `sp_wx_order_in` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '支付订单(统一下单数据)',
  `out_trade_no` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商户订单号',
  `openid` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户标识(trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识)',
  `su_bill_id` int(11) DEFAULT NULL COMMENT '用户所属账单',
  `appid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '小程序id',
  `mch_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信支付商户号',
  `product_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品ID(trade_type=NATIVE时，此参数必传。此参数为二维码中包含的商品ID，商户自行定义。)',
  `attach` varchar(127) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '附加数据',
  `total_fee` int(16) DEFAULT NULL COMMENT '订单总金额(单位为分)',
  `device_info` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '自定义参数(设备号)',
  `nonce_str` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '随机字符串',
  `sign` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '签名',
  `sign_type` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '签名类型',
  `body` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品简单描述',
  `detail` text COLLATE utf8mb4_unicode_ci COMMENT '商品详情',
  `fee_type` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '符合ISO 4217标准的三位字母代码，默认人民币：CNY',
  `spbill_create_ip` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '终端IP(支持IPV4和IPV6两种格式的IP地址。调用微信支付API的机器IP)',
  `time_start` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010',
  `time_expire` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id',
  `goods_tag` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单优惠标记，使用代金券或立减优惠功能时需要的参数',
  `notify_url` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。',
  `trade_type` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交易类型(小程序取值如下：JSAPI)',
  `limit_pay` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '指定支付方式(上传此参数no_credit--可限制用户不能使用信用卡支付)',
  `receipt` varchar(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电子发票入口开放标识',
  `scene_info` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '场景信息',
  `state` int(1) DEFAULT NULL COMMENT '支付状态(0:待支付,1:支付成功,2:支付取消)',
  `err_msg` text COLLATE utf8mb4_unicode_ci COMMENT '错误信息',
  `req_bo` text COLLATE utf8mb4_unicode_ci COMMENT '业务对象(SuBillToPayBo)',
  `package_val` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '预支付交易会话标识',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sp_wx_order_notify` */

DROP TABLE IF EXISTS `sp_wx_order_notify`;

CREATE TABLE `sp_wx_order_notify` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信分配的小程序ID',
  `mch_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信支付分配的商户号',
  `out_trade_no` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商户订单号',
  `total_fee` int(11) DEFAULT NULL COMMENT '订单金额',
  `attach` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '附加数据',
  `device_info` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信支付分配的终端设备号',
  `nonce_str` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '随机字符串，不长于32位',
  `sign` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '签名',
  `sign_type` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '签名类型',
  `openid` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_subscribe` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否关注公众账号',
  `trade_type` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交易类型',
  `bank_type` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '付款银行',
  `settlement_total_fee` int(11) DEFAULT NULL COMMENT '应结订单金额',
  `fee_type` varchar(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '货币种类',
  `cash_fee` int(11) DEFAULT NULL COMMENT '现金支付金额',
  `cash_fee_type` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '现金支付货币类型',
  `coupon_fee` int(10) DEFAULT NULL COMMENT '总代金券金额',
  `coupon_count` int(10) DEFAULT NULL COMMENT '代金券使用数量',
  `transaction_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信支付订单号',
  `time_end` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '支付完成时间',
  `result_code` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '业务结果',
  `err_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '错误代码',
  `err_code_des` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '错误代码描述',
  `return_code` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断',
  `return_msg` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '返回信息',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_admin` */

DROP TABLE IF EXISTS `su_admin`;

CREATE TABLE `su_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '后台管理员',
  `user_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码(MD5)',
  `head_img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT 'https://mall-share.oss-cn-shanghai.aliyuncs.com/comment/img/manage-head-img.jpg' COMMENT '头像',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_bill` */

DROP TABLE IF EXISTS `su_bill`;

CREATE TABLE `su_bill` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户账单',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `bill_sn` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账单流水',
  `from_user_id` int(11) DEFAULT NULL COMMENT '账单来源用户，因为哪个用户而产生的订单(订单类型为2/3时)',
  `money` int(11) DEFAULT NULL COMMENT '账单金额(分)',
  `type` int(1) DEFAULT NULL COMMENT '账单类型(1:普通自购,2:代理收益,3:分享自购,4:分享收益,5:购买道具)',
  `attach` int(11) DEFAULT NULL COMMENT '相关id(账单类型为[1,2,3,4]为orderId,5:道具规格id)',
  `promotion_rate` int(11) DEFAULT NULL COMMENT '佣金比例(千分比 账单类型为[1,2,3,4])',
  `state` int(1) DEFAULT NULL COMMENT '账单状态(1:等待确认,2:准备发放,3:已发放,4:已失效,5:已处理,6:待支付,7:已支付)',
  `income` int(1) DEFAULT NULL COMMENT '收支类型(1:收入账单,2:支出)',
  `des` text COLLATE utf8mb4_unicode_ci COMMENT '账单简介(微信支付body)',
  `fail_reason` text COLLATE utf8mb4_unicode_ci COMMENT '账单失效原因',
  `credit_state` int(1) DEFAULT NULL COMMENT '信用支付状态(0:非信用支付,1:达到信用支付条件,2:信用下降解除绑定)',
  `pay_time` datetime DEFAULT NULL COMMENT '打款时间',
  `update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_config` */

DROP TABLE IF EXISTS `su_config`;

CREATE TABLE `su_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户配置(会覆盖系统配置)',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置类型',
  `val` text COLLATE utf8mb4_unicode_ci COMMENT '配置值',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_feedback` */

DROP TABLE IF EXISTS `su_feedback`;

CREATE TABLE `su_feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户反馈',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `des` text COLLATE utf8mb4_unicode_ci COMMENT '问题描述',
  `images` text COLLATE utf8mb4_unicode_ci COMMENT '相关图片',
  `type` int(1) DEFAULT '1' COMMENT '意见类型(1:默认)',
  `state` int(1) DEFAULT '1' COMMENT '状态(1:待处理,2:已处理)',
  `accept` int(1) DEFAULT '0' COMMENT '是否采纳(0:默认,1:未采纳,2:已采纳)',
  `reply` text COLLATE utf8mb4_unicode_ci COMMENT '回复',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_menu_tips` */

DROP TABLE IF EXISTS `su_menu_tips`;

CREATE TABLE `su_menu_tips` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户菜单按钮提示',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户id',
  `menu_id` int(11) DEFAULT NULL COMMENT '菜单id',
  `hot` int(1) DEFAULT NULL COMMENT '是否显示小红点(0:否,1:是)',
  `num` int(11) DEFAULT NULL COMMENT '显示数字(少于四位数)',
  `update_time` datetime NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15085 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_order` */

DROP TABLE IF EXISTS `su_order`;

CREATE TABLE `su_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `order_sn` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单编号',
  `order_sub_sn` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '子订单号',
  `product_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品id',
  `product_name` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品名称',
  `img_url` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品简介',
  `platform_type` int(1) DEFAULT NULL COMMENT '所属平台',
  `state` int(1) DEFAULT NULL COMMENT '订单状态(-1:无效订单,0:未支付,1:已支付,2:确认收货,3:已结算,4:结算失败)',
  `fail_reason` text COLLATE utf8mb4_unicode_ci COMMENT '审核失败原因',
  `p_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '推广位id',
  `original_price` int(11) DEFAULT NULL COMMENT '单品原始价格(分)',
  `quantity` int(1) DEFAULT NULL COMMENT '购买数量',
  `amount` int(11) DEFAULT NULL COMMENT '实际支付金额(分)',
  `promotion_rate` int(11) DEFAULT NULL COMMENT '佣金比例(千分比)',
  `promotion_amount` int(11) DEFAULT NULL COMMENT '佣金(分)',
  `price_compare_status` int(1) DEFAULT NULL COMMENT '比价状态(0:正常,2:比价)',
  `type` int(1) DEFAULT NULL COMMENT '订单类型(0:领券页面,1:红包页,2:领券页,3:题页)',
  `form_type` int(1) DEFAULT NULL COMMENT '来源类型(1:普通自购,2:分享自购)',
  `share_user_id` int(11) DEFAULT NULL COMMENT '分享订单分享的用户id',
  `from_app` int(1) DEFAULT NULL COMMENT '订单来源(1:微信小程序)',
  `custom_parameters` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '自定义参数(userId:购买用户,shareUserId:分享的用户,fromApp:app来源)',
  `cart` text COLLATE utf8mb4_unicode_ci COMMENT '购物车所有商品ID(,号分割)',
  `order_modify_at` datetime DEFAULT NULL COMMENT '最后更新时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE` (`order_sub_sn`,`platform_type`),
  KEY `EXIST` (`order_sub_sn`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_permission` */

DROP TABLE IF EXISTS `su_permission`;

CREATE TABLE `su_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限名称',
  `des` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简介',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_pid` */

DROP TABLE IF EXISTS `su_pid`;

CREATE TABLE `su_pid` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pdd` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '拼多多pid',
  `jd` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '京东',
  `mgj` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '蘑菇街',
  `tb` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '淘宝',
  `wph` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '唯品会',
  `state` int(1) DEFAULT '1' COMMENT '状态(0:不完善,1:可用,2:已被使用)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9781 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_product` */

DROP TABLE IF EXISTS `su_product`;

CREATE TABLE `su_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `app_type` int(1) DEFAULT NULL,
  `goods_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品id',
  `platform_type` int(1) DEFAULT NULL COMMENT '所属平台',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `is_collect` int(1) DEFAULT NULL COMMENT '是否被搜藏(0:否,1:是)',
  `share_user_id` int(11) DEFAULT NULL COMMENT '分享用户id',
  `coupon_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '优惠券id(淘宝用)',
  `goods_info` text COLLATE utf8mb4_unicode_ci COMMENT '商品信息快照(json格式)',
  `del` int(1) DEFAULT NULL COMMENT '是否删除(0:已删除,1:未删除)',
  `collect_time` datetime DEFAULT NULL COMMENT '搜藏时间',
  `update_time` datetime NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE` (`goods_id`,`platform_type`,`user_id`),
  KEY `EXIST` (`goods_id`,`platform_type`,`user_id`),
  KEY `QUEUE` (`user_id`,`is_collect`,`del`,`collect_time`,`update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=366987 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_role` */

DROP TABLE IF EXISTS `su_role`;

CREATE TABLE `su_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色名称',
  `des` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简介',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_role_permission_map` */

DROP TABLE IF EXISTS `su_role_permission_map`;

CREATE TABLE `su_role_permission_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `permission_id` int(11) DEFAULT NULL COMMENT '权限id',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_search` */

DROP TABLE IF EXISTS `su_search`;

CREATE TABLE `su_search` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户搜索历史记录',
  `user_id` int(11) DEFAULT NULL,
  `keyword` text COLLATE utf8mb4_unicode_ci COMMENT '搜索内容',
  `del` int(1) DEFAULT NULL COMMENT '是否删除(0:已删除,1未删除)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=391 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_share` */

DROP TABLE IF EXISTS `su_share`;

CREATE TABLE `su_share` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户分享记录',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `goods_id` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品id',
  `platform_type` int(1) DEFAULT NULL COMMENT '所属平台',
  `watch` int(11) DEFAULT '0' COMMENT '商品浏览次数',
  `sell` int(11) DEFAULT '0' COMMENT '商品下单次数',
  `will_make_money` int(11) DEFAULT '0' COMMENT '预估收入(分)',
  `del` int(1) DEFAULT NULL COMMENT '删除(0:删除,1:未删除)',
  `goods_info` text COLLATE utf8mb4_unicode_ci COMMENT '商品信息快照(json)',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE` (`user_id`,`goods_id`,`platform_type`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_summary` */

DROP TABLE IF EXISTS `su_summary`;

CREATE TABLE `su_summary` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户数据汇总',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `profit_today` int(11) DEFAULT NULL COMMENT '今日收益(分)',
  `profit_today_last_update` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '今日收益最后一次更新日期',
  `profit_history` int(11) DEFAULT NULL COMMENT '历史收益',
  `profit_wait` int(11) DEFAULT NULL COMMENT '等待确认',
  `profit_ready` int(11) DEFAULT NULL COMMENT '准备发放',
  `profit_cash` int(11) DEFAULT NULL COMMENT '已返',
  `total_coupon` int(11) DEFAULT NULL COMMENT '使用优惠劵总量',
  `total_buy` int(11) DEFAULT NULL COMMENT '自购成交额',
  `total_share` int(11) DEFAULT NULL COMMENT '分享成交额',
  `total_proxy_user` int(11) DEFAULT NULL COMMENT '锁定用户总量',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE` (`user_id`),
  KEY `EXIST` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=786 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_user` */

DROP TABLE IF EXISTS `su_user`;

CREATE TABLE `su_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_sn` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL COMMENT '父用户id',
  `nickname` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信昵称',
  `head_img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT 'https://mall-share.oss-cn-shanghai.aliyuncs.com/comment/img/head-img%20.png' COMMENT '头像',
  `sex` int(1) DEFAULT '0' COMMENT '性别(0:未知,1:男,2:女)',
  `open_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tel` varchar(13) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `state` int(1) DEFAULT '1' COMMENT '账号状态(0:封禁,1:正常)',
  `reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '封禁理由',
  `city` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '城市',
  `province` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省',
  `country` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '国家',
  `language` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '语言',
  `pid` int(11) DEFAULT NULL COMMENT '推广位id',
  `from_whare` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户来源',
  `register_ip` varchar(18) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '注册ip',
  `current_ip` varchar(18) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '当前IP',
  `last_login` datetime NOT NULL COMMENT '最后登录时间',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3379 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_user_role_map` */

DROP TABLE IF EXISTS `su_user_role_map`;

CREATE TABLE `su_user_role_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `creator` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建者',
  `reason` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建原因',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sw_api_record` */

DROP TABLE IF EXISTS `sw_api_record`;

CREATE TABLE `sw_api_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户API请求记录',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `app_type` int(11) DEFAULT NULL COMMENT 'app类型',
  `ip` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ip地址',
  `ip_addr` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ip解析',
  `ua` text COLLATE utf8mb4_unicode_ci COMMENT '浏览器ua',
  `url` text COLLATE utf8mb4_unicode_ci COMMENT '请求链接',
  `method` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '请求类型',
  `param` text COLLATE utf8mb4_unicode_ci COMMENT '请求参数',
  `result` longtext COLLATE utf8mb4_unicode_ci COMMENT '响应结果',
  `time` int(11) DEFAULT NULL COMMENT '执行时间(毫秒)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`id`),
  KEY `sort` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=467159 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sw_credit_bill_bind_record` */

DROP TABLE IF EXISTS `sw_credit_bill_bind_record`;

CREATE TABLE `sw_credit_bill_bind_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '信用账单绑定记录',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `goods_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品名称',
  `bill_sn` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '绑定的订单',
  `bill_money` int(11) DEFAULT NULL COMMENT '账单金额',
  `pay_time` datetime DEFAULT NULL COMMENT '发放时间',
  `state` int(1) DEFAULT NULL COMMENT '绑定状态(0:已释放,1:绑定中)',
  `bind_scores` int(11) DEFAULT NULL COMMENT '绑定时的信用',
  `un_bind_scores` int(11) DEFAULT NULL COMMENT '解绑时的信用',
  `un_bind_reason` text COLLATE utf8mb4_unicode_ci COMMENT '解绑原因',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `EXIST` (`user_id`,`state`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sw_credit_bill_conf` */

DROP TABLE IF EXISTS `sw_credit_bill_conf`;

CREATE TABLE `sw_credit_bill_conf` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户信用付款阶梯配置',
  `scores` int(11) DEFAULT NULL COMMENT '分数',
  `advance_count` int(11) DEFAULT NULL COMMENT '可以预支的次数',
  `max_quota` int(11) DEFAULT NULL COMMENT '最大预支的总额度(分)',
  `quota` int(11) DEFAULT NULL COMMENT '每笔限额',
  `pay_days` int(11) DEFAULT NULL COMMENT '打款间隔(天)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sw_credit_bill_pay_record` */

DROP TABLE IF EXISTS `sw_credit_bill_pay_record`;

CREATE TABLE `sw_credit_bill_pay_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '账单付款风控检测记录',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `bill_sn` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账单号',
  `bill_money` int(11) DEFAULT NULL COMMENT '账单金额',
  `check_result` int(1) DEFAULT NULL COMMENT '检测结果(0:未通过,1:通过,2:人工处理)',
  `fail_reason` text COLLATE utf8mb4_unicode_ci COMMENT '未通过原因',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sw_credit_conf` */

DROP TABLE IF EXISTS `sw_credit_conf`;

CREATE TABLE `sw_credit_conf` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '信用配置',
  `name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置名称',
  `val` text COLLATE utf8mb4_unicode_ci COMMENT '配置值',
  `des` text COLLATE utf8mb4_unicode_ci COMMENT '配置简介',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sw_credit_record` */

DROP TABLE IF EXISTS `sw_credit_record`;

CREATE TABLE `sw_credit_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户信用记录',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `type` int(1) DEFAULT NULL COMMENT '分数类型(0:新用户注册,1:每日登录,2:邀请好友,3:订单交易成功,4:恶意退单)',
  `change_scores` int(11) DEFAULT NULL COMMENT '分数变动',
  `scores` int(11) DEFAULT NULL COMMENT '改变后的分数',
  `change_reason` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '改变原因',
  `attached` text COLLATE utf8mb4_unicode_ci COMMENT '附件数据(订单sn)',
  `create_stamp` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建时间戳',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `EXIST` (`user_id`,`type`,`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=13057 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sw_login_record` */

DROP TABLE IF EXISTS `sw_login_record`;

CREATE TABLE `sw_login_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户登录记录表',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `app_type` int(11) DEFAULT NULL COMMENT 'app类型',
  `ip` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录ip',
  `ip_addr` text COLLATE utf8mb4_unicode_ci COMMENT 'ip地址解析',
  `ua` text COLLATE utf8mb4_unicode_ci COMMENT '浏览器UA',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=137854 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
