package com.xm.cpsmall.comm.mq.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户事件消息载体
 */
@Data
public class AbsUserActionMessage implements Serializable {
    public AbsUserActionMessage() {}

    public AbsUserActionMessage(Integer userId) {
        this.userId = userId;
    }
    //所属用户
    private Integer userId;

}
