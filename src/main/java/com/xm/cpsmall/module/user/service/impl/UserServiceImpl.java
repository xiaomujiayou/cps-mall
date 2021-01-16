package com.xm.cpsmall.module.user.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.PageUtil;
import com.xm.cpsmall.exception.GlobleException;
import com.xm.cpsmall.module.mall.constant.ConfigEnmu;
import com.xm.cpsmall.module.mall.constant.ConfigTypeConstant;
import com.xm.cpsmall.module.mall.service.MallConfigService;
import com.xm.cpsmall.module.user.constant.BillStateConstant;
import com.xm.cpsmall.module.user.constant.UserTypeConstant;
import com.xm.cpsmall.module.user.mapper.SuUserMapper;
import com.xm.cpsmall.module.user.mapper.custom.SuOrderMapperEx;
import com.xm.cpsmall.module.user.mapper.custom.SuRoleMapperEx;
import com.xm.cpsmall.module.user.mapper.custom.SuUserMapperEx;
import com.xm.cpsmall.module.user.serialize.bo.UserProfitBo;
import com.xm.cpsmall.module.user.serialize.dto.ProxyProfitDto;
import com.xm.cpsmall.module.user.serialize.entity.SuPidEntity;
import com.xm.cpsmall.module.user.serialize.entity.SuUserEntity;
import com.xm.cpsmall.module.user.serialize.ex.RolePermissionEx;
import com.xm.cpsmall.module.user.serialize.form.GetUserInfoForm;
import com.xm.cpsmall.module.user.serialize.form.UpdateUserInfoForm;
import com.xm.cpsmall.module.user.serialize.vo.ProxyInfoVo;
import com.xm.cpsmall.module.user.service.PidService;
import com.xm.cpsmall.module.user.service.UserService;
import com.xm.cpsmall.utils.MD5;
import com.xm.cpsmall.utils.lock.DoWorkWithResult;
import com.xm.cpsmall.utils.lock.LockUtil;
import com.xm.cpsmall.utils.mybatis.PageBean;
import com.xm.cpsmall.utils.response.MsgEnum;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Lazy
    @Autowired
    private UserService userService;
    @Autowired
    private SuUserMapper suUserMapper;
    @Autowired
    private SuUserMapperEx suUserMapperEx;
    @Autowired
    private SuRoleMapperEx suRoleMapperEx;
    @Autowired
    private WxMaService wxMaService;
    @Autowired
    private MallConfigService mallConfigService;
    @Autowired
    private PidService pidService;
    @Autowired
    private RedisLockRegistry redisLockRegistry;
    @Autowired
    private SuOrderMapperEx suOrderMapperEx;


    @Override
    public SuUserEntity getUserInfo(GetUserInfoForm getUserInfoForm) throws WxErrorException {
        if(StringUtils.isNotBlank(getUserInfoForm.getCode())){
            WxMaJscode2SessionResult wxMaJscode2SessionResult = wxMaService.getUserService().getSessionInfo(getUserInfoForm.getCode());
            final SuUserEntity[] record = {new SuUserEntity()};
            record[0].setOpenId(wxMaJscode2SessionResult.getOpenid());
            Lock lock = redisLockRegistry.obtain(this.getClass().getSimpleName() + ":" + wxMaJscode2SessionResult.getOpenid());
            return (SuUserEntity) LockUtil.lock(lock, new DoWorkWithResult<SuUserEntity>() {
                @Override
                public SuUserEntity dowork() {
                    record[0] = suUserMapper.selectOne(record[0]);
                    if(record[0] != null) {
                        record[0].setCurrentIp(getUserInfoForm.getIp());
                        SuUserEntity user = new SuUserEntity();
                        user.setId(record[0].getId());
                        user.setCurrentIp(getUserInfoForm.getIp());
                        suUserMapper.updateByPrimaryKeySelective(user);
                        return record[0];
                    }
                    return userService.addNewUser(wxMaJscode2SessionResult.getOpenid(),getUserInfoForm.getIp(),getUserInfoForm.getShareUserId(),getUserInfoForm.getFrom());
                }
            }).get();
        }else if(StringUtils.isNotBlank(getUserInfoForm.getOpenId())){
            SuUserEntity record = new SuUserEntity();
            record.setOpenId(getUserInfoForm.getOpenId());
            SuUserEntity user = suUserMapper.selectOne(record);
            record.setId(user.getId());
            record.setLastLogin(new Date());
            suUserMapper.updateByPrimaryKeySelective(record);
            return user;
        }else {
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR);
        }
    }

    /**
     * 添加一个新用户
     * @param openId
     * @return
     */
    public SuUserEntity addNewUser(String openId,String ip,Integer shareUserId,String from){
        SuPidEntity suPidEntity = pidService.generatePid();
        SuUserEntity user = new SuUserEntity();
        if(shareUserId != null)
            user.setParentId(shareUserId);
        user.setRegisterIp(ip);
        user.setCurrentIp(ip);
        user.setOpenId(openId);
        user.setUserSn(MD5.md5(openId,""));
        user.setNickname("Su_"+ user.getUserSn().substring(0,5));
        user.setSex(0);
        user.setFromWhare(from);
        user.setCreateTime(new Date());
        user.setLastLogin(new Date());
        user.setPid(suPidEntity.getId());
        suUserMapper.insertSelective(user);
        return user;
    }


    @Override
    public void updateUserInfo(Integer userId, UpdateUserInfoForm updateUserInfoForm){
        SuUserEntity suUserEntity = new SuUserEntity();
        suUserEntity.setId(userId);
        suUserEntity.setNickname(updateUserInfoForm.getNickName());
        suUserEntity.setHeadImg(updateUserInfoForm.getAvatarUrl());
        suUserEntity.setSex(updateUserInfoForm.getGender());
        suUserEntity.setLanguage(updateUserInfoForm.getLanguage());
        suUserEntity.setCity(updateUserInfoForm.getCity());
        suUserEntity.setProvince(updateUserInfoForm.getProvince());
        suUserEntity.setCountry(updateUserInfoForm.getCountry());
        suUserMapper.updateByPrimaryKeySelective(suUserEntity);
    }

    @Override
    public List<RolePermissionEx> getUserRole(Integer userId) {
        return suRoleMapperEx.getUserRoleEx(userId);
    }

    @Override
    public SuUserEntity getSuperUser(Integer userId, int userType) {
        SuUserEntity self = suUserMapper.selectByPrimaryKey(userId);
        if(self == null){
            throw new GlobleException(MsgEnum.USER_NOFOUND_ERROR,"id:"+userId);
        }
        switch (userType){
            case UserTypeConstant.SELF:{
                return self;
            }
            case UserTypeConstant.PARENT:{
                if(self.getParentId() == null){
                    throw new NullPointerException(String.format("用户：%s 不存在父用户",userId));
                }
                SuUserEntity parent = suUserMapper.selectByPrimaryKey(self.getParentId());
                if(parent == null){
                    throw new GlobleException(MsgEnum.USER_ID_ERROR);
                }
                return parent;
            }
            case UserTypeConstant.PROXY:{
                if(self.getParentId() == null || self.getParentId() == 0){
                    return null;
                }
                Integer level = Integer.valueOf(mallConfigService.getConfig(userId, ConfigEnmu.PROXY_LEVEL, ConfigTypeConstant.SYS_CONFIG).getVal());
                SuUserEntity target = null;
                for (int i = 0; i < level; i++) {
                    target = suUserMapper.selectByPrimaryKey(self.getParentId());
                    if(target.getParentId() == null || target.getParentId() == 0){
                        break;
                    }
                }
                return target;
            }
        }
        throw new GlobleException(MsgEnum.TYPE_NOTFOUND_ERROR);
    }

    @Override
    public PageBean<ProxyProfitDto> getProxyProfit(Integer userId, Integer state, Integer orderColumn, Integer orderBy, Integer pageNum, Integer pageSize) {
        String orderByStr = null;
        if(orderBy != null && orderColumn != null && orderBy != 0){
            String order = orderBy == 1?"asc":"desc";
            switch (orderColumn){
                case 1:{
                    orderByStr = "nickname " + order;
//                    OrderByHelper.orderBy("nickname " + order);
                    break;
                }
                case 2:{
                    orderByStr = "proxy_profit " + order;
//                    OrderByHelper.orderBy("proxy_profit " + order);
                    break;
                }
                case 3:{
                    orderByStr = "proxy_num " + order;
//                    OrderByHelper.orderBy("proxy_num " + order);
                    break;
                }
                case 4:{
                    orderByStr = "create_time " + order;
//                    OrderByHelper.orderBy("create_time " + order);
                    break;
                }
            }

        }
//        PageHelper.startPage(pageNum,pageSize);
        List<ProxyProfitDto> proxyProfitDtos = suUserMapperEx.getProxyProfit(userId,state,orderByStr,PageUtil.getStart(pageNum,pageSize),pageSize);
        return new PageBean<>(proxyProfitDtos);
    }

    @Override
    public ProxyInfoVo getProxyInfo(Integer userId) {
        ProxyInfoVo proxyInfoVo = new ProxyInfoVo();
        proxyInfoVo.setTotalIndirectProxy(suUserMapperEx.getIndirectUserCount(userId));
        SuUserEntity example = new SuUserEntity();
        example.setParentId(userId);
        proxyInfoVo.setTotalDirectProxy(suUserMapper.selectCount(example));
        return proxyInfoVo;
    }

    @Override
    public UserProfitBo getUserProftList(Integer userId) {
        UserProfitBo userProfitBo = new UserProfitBo();
        Map<String, BigDecimal> orderInfo = suOrderMapperEx.getUserOrderAbout(userId);
        userProfitBo.setTotalCoupon(orderInfo.get("totalCoupon").intValue());
        userProfitBo.setTotalConsumption(orderInfo.get("totalConsumption").intValue());
        userProfitBo.setTodayProfit(suOrderMapperEx.getUserTotalCommission(userId,null, DateUtil.parse(DateUtil.today()),new Date()).intValue());
        userProfitBo.setTotalProfit(suOrderMapperEx.getUserTotalCommission(userId,null,null,null).intValue());
        userProfitBo.setWaitProfit(suOrderMapperEx.getUserTotalCommission(userId, CollUtil.newArrayList(BillStateConstant.WAIT),null,null).intValue());
        userProfitBo.setReadyProfit(suOrderMapperEx.getUserTotalCommission(userId, CollUtil.newArrayList(BillStateConstant.READY),null,null).intValue());
        userProfitBo.setTotalShare(suOrderMapperEx.getUserShareOrderAbout(userId).intValue());
        ProxyInfoVo proxyInfoVo = getProxyInfo(userId);
        userProfitBo.setTotalProxyUser(proxyInfoVo.getTotalDirectProxy()+proxyInfoVo.getTotalIndirectProxy());
        return userProfitBo;
    }

    @Override
    public UserProfitBo getUserProftDesc(Integer userId) {
        UserProfitBo userProfitBo = new UserProfitBo();
        Map<String, BigDecimal> orderInfo = suOrderMapperEx.getUserOrderAbout(userId);
        userProfitBo.setTotalCoupon(orderInfo.get("totalCoupon").intValue());
        userProfitBo.setTotalConsumption(orderInfo.get("totalConsumption").intValue());
        userProfitBo.setTotalCommission(suOrderMapperEx.getUserTotalCommission(userId,CollUtil.newArrayList(3),null,null).intValue());
        return userProfitBo;
    }
}
