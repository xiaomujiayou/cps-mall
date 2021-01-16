package com.xm.cpsmall.module.cron.service.impl;

import com.alibaba.fastjson.JSON;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddTimeGetRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddTimeGetResponse;
import com.xm.cpsmall.comm.mq.message.config.OrderMqConfig;
import com.xm.cpsmall.module.cron.mapper.ScOrderStateRecordMapper;
import com.xm.cpsmall.module.cron.mapper.ScOrderSyncHistoryMapper;
import com.xm.cpsmall.module.cron.serialize.bo.OrderWithResBo;
import com.xm.cpsmall.module.cron.serialize.entity.ScOrderStateRecordEntity;
import com.xm.cpsmall.module.cron.serialize.entity.ScOrderSyncHistoryEntity;
import com.xm.cpsmall.module.cron.service.TaskService;
import com.xm.cpsmall.module.mall.constant.PlatformTypeEnum;
import com.xm.cpsmall.utils.mybatis.PageBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Slf4j
public abstract class AbsTask implements TaskService {

    @Autowired
    private ScOrderSyncHistoryMapper scOrderSyncHistoryMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private PopHttpClient popHttpClient;
    @Autowired
    private ScOrderStateRecordMapper scOrderStateRecordMapper;

    @Override
    public void start() {

    }

    protected abstract PlatformTypeEnum getPlatform();

    /**
     * 处理订单
     */
    protected void processOrders(Date startTime, Date endTime,Integer pageNum,Integer pageSize){
        PageBean<OrderWithResBo> orders = null;
        try{
            orders = getOrderByIncrement(startTime,endTime,pageNum,pageSize);
            log.debug("{} 订单同步 - 开始时间：{} 结束时间：{} 当前页：{} 页面大小：{} 总数据：{}",getPlatform().getName() , startTime,endTime,pageNum,pageSize,orders == null ? 0 : orders.getTotal());
            saveHistory(startTime,endTime,pageNum,pageSize,orders);
            if(orders == null)
                return;
            orders.getList().stream().forEach(o->{
                boolean check = stateIsChange(o);
                if(check){
                    log.debug("{} 订单信息：{}" ,getPlatform().getName(), JSON.toJSONString(o));
                    rabbitTemplate.convertAndSend(OrderMqConfig.EXCHANGE,OrderMqConfig.KEY,o.getSuOrderEntity());
                }
            });
        }catch (Exception e){
            log.error("{} 订单同步出错：{}",getPlatform().getName(), e);

        }finally {
            if(orders != null && orders.hasNext())
                processOrders(startTime,endTime,pageNum + 1,pageSize);
        }

    }

    /**
     * 查询订单状态是否已更新
     * @param orderWithResBo
     * @return
     */
    private boolean stateIsChange(OrderWithResBo orderWithResBo){
        ScOrderStateRecordEntity entity = orderWithResBo.getScOrderStateRecordEntity();
        ScOrderStateRecordEntity rocord = new ScOrderStateRecordEntity();
        rocord.setOrderSubSn(entity.getOrderSubSn());
        rocord = scOrderStateRecordMapper.selectOne(rocord);
        //记录不存在则保存
        if(rocord == null || rocord.getId() == null) {
            entity.setUpdateTime(new Date());
            entity.setCreateTime(entity.getUpdateTime());
            scOrderStateRecordMapper.insertSelective(entity);
            return true;
        }
        //记录存在则对比状态
        if(!rocord.getState().equals(entity.getState())){
            rocord.setOriginState(entity.getOriginState());
            rocord.setOriginStateDes(entity.getOriginStateDes());
            rocord.setState(entity.getState());
            rocord.setUpdateTime(new Date());
            scOrderStateRecordMapper.updateByPrimaryKeySelective(rocord);
            return true;
        }
        //更新原始状态信息
        if(!rocord.getOriginState().equals(entity.getOriginState()) || rocord.getOriginStateDes().equals(rocord.getOriginStateDes())){
            rocord.setOriginState(entity.getOriginState());
            rocord.setOriginStateDes(entity.getOriginStateDes());
            rocord.setUpdateTime(new Date());
            scOrderStateRecordMapper.updateByPrimaryKeySelective(rocord);
        }
        return false;
    }

    protected void saveHistory(Date startUpdateTime, Date endUpdateTime,Integer pageNum,Integer pageSize,PageBean<OrderWithResBo> orders){
        ScOrderSyncHistoryEntity entity = new ScOrderSyncHistoryEntity();
        entity.setPlatformType(getPlatform().getType());
        entity.setStartTime(startUpdateTime);
        entity.setEndTime(endUpdateTime);
        entity.setTotal(orders == null ? 0 : Double.valueOf(orders.getTotal()).intValue());
        entity.setCurrentPage(pageNum);
        entity.setPageSize(pageSize);
        entity.setCreateTime(new Date());
        scOrderSyncHistoryMapper.insertSelective(entity);
    }

    @Override
    public PageBean<OrderWithResBo> getOrderByIncrement(Date startUpdateTime, Date endUpdateTime, Integer pageNum, Integer pageSize) throws Exception {
        return null;
    }

    @Override
    public List<OrderWithResBo> getOrderByNum(String orderNum) throws Exception {
        return null;
    }

    @Override
    public Date getTime() throws Exception {
        PddTimeGetRequest request = new PddTimeGetRequest();
        PddTimeGetResponse response = popHttpClient.syncInvoke(request);
        return DateUtils.parseDate(response.getTimeGetResponse().getTime(),"yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 测试
     */
    public void test(String orderJsonStr){

    }
}
