package com.xm.cpsmall.module.cron.task;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.xm.cpsmall.comm.mq.message.config.OrderMqConfig;
import com.xm.cpsmall.module.cron.mapper.ScMgjOrderRecordMapper;
import com.xm.cpsmall.module.cron.serialize.bo.OrderWithResBo;
import com.xm.cpsmall.module.cron.serialize.entity.ScMgjOrderRecordEntity;
import com.xm.cpsmall.module.cron.service.TaskService;
import com.xm.cpsmall.utils.mybatis.PageBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 同步蘑菇街订单
 */
@Slf4j
@Component
public class MgjOrderSyncTask {

    @Autowired
    private ScMgjOrderRecordMapper scMgjOrderRecordMapper;
    @Resource(name = "mgjTaskService")
    private TaskService mgjTaskService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发现新订单
     * 此任务无法更新订单
     */
//    @Async("threadPool")
    @Scheduled(cron = "0/30 * * * * ?")
    public void getNewOrder() {
        mgjTaskService.start();
    }


    /**
     * 更新订单状态
     * 蘑菇街不支持最新时间
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void updateOrder(){
        Integer pageNum = 1;
        Integer pageSize = 40;
        PageHelper.startPage(pageNum,pageSize);
        OrderByHelper.orderBy("last_update asc");
        Example example = new Example(ScMgjOrderRecordEntity.class);
        Date twoMonthAgo = DateUtil.offsetMonth(new Date(),-2);
        example.createCriteria().andGreaterThan("createTime",twoMonthAgo);
        List<ScMgjOrderRecordEntity> list = scMgjOrderRecordMapper.selectByExample(example);
        if(list != null)
            list.stream().forEach(o->{
                try {
                    update(o);
                    Thread.sleep(250);  //蘑菇街订单API限定，每秒五次
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        PageBean<ScMgjOrderRecordEntity> pageBean = new PageBean<>(list);
        while (pageNum * pageSize < pageBean.getTotal()){
            pageNum = pageNum + 1;
            PageHelper.startPage(pageNum,pageSize);
            OrderByHelper.orderBy("create_time asc");
            example.createCriteria().andGreaterThan("createTime",twoMonthAgo);
            list = scMgjOrderRecordMapper.selectByExample(example);
            if(list != null)
                list.stream().forEach(o->{
                    try {
                        update(o);
                        Thread.sleep(250);  //蘑菇街订单API限定，每秒五次
                    } catch (Exception e) {
                        log.error("蘑菇街 更新订单状态失败 无效单号：{} 异常：{}",o.getOrderSubSn(),e);
                    }
                });
            pageBean = new PageBean<>(list);
        }
    }

    private void update(ScMgjOrderRecordEntity scMgjOrderRecordEntity) throws Exception {
        try {
            List<OrderWithResBo> orderWithResBos = mgjTaskService.getOrderByNum(scMgjOrderRecordEntity.getOrderSn());
            if(orderWithResBos != null && !orderWithResBos.isEmpty()){
                for (OrderWithResBo orderWithResBo : orderWithResBos) {
                    if(!orderWithResBo.getSuOrderEntity().getOrderSubSn().equals(scMgjOrderRecordEntity.getOrderSubSn()))
                        continue;
                    if(scMgjOrderRecordEntity.getState().equals(orderWithResBo.getScOrderStateRecordEntity().getState()))
                        continue;
                    //更新订单状态
                    scMgjOrderRecordEntity.setState(orderWithResBo.getScOrderStateRecordEntity().getState());
                    rabbitTemplate.convertAndSend(OrderMqConfig.EXCHANGE,OrderMqConfig.KEY,orderWithResBo.getSuOrderEntity());
                }
            }
        }finally {
            scMgjOrderRecordEntity.setLastUpdate(new Date());
            scMgjOrderRecordMapper.updateByPrimaryKeySelective(scMgjOrderRecordEntity);
        }
    }
}
