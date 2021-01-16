# cps-mall (单体版)

### 另有微服务版 👉👉 [cps-mall-cloud](https://github.com/seata/seata "cps-mall-cloud")
优惠券商城后台，支持淘宝、拼多多、京东、唯品会、蘑菇街，预留接口，可以很方便的扩展其他优惠券平台。
精力有限前端仅适配了微信小程序，可根据接口自行适配小程序、PC、H5、App等平台。



##### 微信扫码预览：
![微信扫码预览](https://mall-share.oss-cn-shanghai.aliyuncs.com/share/untitled.png?x-oss-process=image/resize,h_200,w_200 "微信扫码预览")


------------

#### 开发环境、用到的框架和类库：
- 开发环境：
  - IDEA
  - JDK-1.8
  - maven-3.6.1
  - MySql-5.7
  - Redis-3.2.100
  - RabbitMq-3.7.14（需安装rabbitmq_delayed_message_exchange延时消息插件）
- 用到的框架：
  - SpringBoot-2.1.6
  - MyBatis-3.4.6
  - Shiro
  - [通用Mapper](https://github.com/abel533/Mapper "通用Mapper")
- 用到的类库：
  - druid
  - RedisLockRegistry：Spring分布式锁
  - [hutool](https://github.com/looly/hutool "hutool")
  - [ip2region：IP地址解析](https://github.com/lionsoul2014/ip2region "ip2region：IP地址解析")
  - [easyexcel：阿里POI](https://github.com/alibaba/easyexcel "easyexcel：阿里POI")
  - [HanLP：分词工具](https://github.com/hankcs/HanLP "HanLP：分词工具")
  - [PageHelper：MyBatis分页插件](https://github.com/pagehelper/Mybatis-PageHelper "PageHelper：MyBatis分页插件")
  - [weixin-java-miniapp：微信开发工具包](https://github.com/Wechat-Group/WxJava "weixin-java-miniapp：微信开发工具包")

#### 模块功能简介：
- mall: 商城模块，整合淘宝、拼多多、唯品会、蘑菇街平台的SDK，抹除各个平台间的差异，提供了统一的API
- user：用户模块，包含的具体内容如下：
  - 订单查询：包含自购订单、分享订单的查询。
  - 账单查询：包含用户自购、分享、锁定产生的收益账单。
  - 三级分销：提供各级用户查询、相关收益，各级佣金比例可调。
  - 信用体系：用户购买习惯良好、使用频繁、分享好友等操作则信用提升，反之信用下降，信用越高自动返现额度越大、速度越快，增强用户体验。
  - 浏览历史、收藏记录、意见反馈等功能的支持。
- pay: 支付/付款模块，为项目提供支付、付款的能力。
- activite: 活动模块，可按规则配置相关营销活动，如：高温补贴、看视频领红包、分享返现等。
- lottery: 道具商城，可配置添加一些功能道具，如：VIP、收益翻倍卡等营销道具。
- mini: 小程序功能模块，提供微信小程序平台独有的功能，小程序登录、消息推送、服务认证、生成分享海报、生成二维码等。
- cron: 定时任务模块，主要服务于用户订单，定时从cps平台拉取订单。
- wind: 风控模块，防止用户恶意操作对平台造成损失

------------

#### 使用前的准备
- 导入sql文件，生成表及初始化数据。
- 注册淘宝、拼多多、蘑菇街、唯品会任一平台联盟开发者账号，获取appId、secret等信息,
- 申请微信支付，并开通企业付款（不需要返现，则不需要）
- 浏览resources目录下包含“demo”配置文件，酌情修改，改好后删除文件名中的“-demo”


#### 部分依赖可能无法下载：

```
    <!--淘宝sdk-->
    <!-- ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓-->
    <dependency>
        <groupId>com.xm.tb</groupId>
        <artifactId>taobao-sdk</artifactId>
        <version>1.0.0</version>
    </dependency>
    <!--唯品会sdk-->
    <!-- ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓-->
    <dependency>
        <groupId>com.xm.wph</groupId>
        <artifactId>osp-sdk</artifactId>
        <version>1.0.0</version>
    </dependency>
    <dependency>
        <groupId>com.xm.wph</groupId>
        <artifactId>vop-sdk</artifactId>
        <version>1.0.0</version>
    </dependency>

    <!--蘑菇街sdk-->
    <!-- ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓-->
    <dependency>
        <groupId>com.xm.mgj</groupId>
        <artifactId>openapi-sdk</artifactId>
        <version>1.0.0</version>
    </dependency>

    <!--拼多多sdk-->
    <!-- ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓-->
    <dependency>
        <groupId>com.xm.pdd</groupId>
        <artifactId>pop-sdk</artifactId>
        <classifier>all</classifier>
        <version>1.6.1</version>
    </dependency>
```

依赖已打包：

`链接：https://pan.baidu.com/s/1VDY0n9SEIK3T0VY_hsFYyw 提取码：ug5y`

也可自行去官网下载。
       

------------
联系我获取前端代码：

![微信扫码](https://mall-share.oss-cn-shanghai.aliyuncs.com/share/my.jpg?x-oss-process=image/resize,h_200,w_200 "微信扫码")
