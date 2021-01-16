package com.xm.cpsmall.exception;

import cn.hutool.core.util.StrUtil;
import com.xm.cpsmall.utils.response.Msg;
import com.xm.cpsmall.utils.response.MsgEnum;
import com.xm.cpsmall.utils.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

/**
 * 异常处理
 */
@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Msg handle(Exception e) throws Exception {
        e.printStackTrace();
        if(e instanceof GlobleException) {
            GlobleException ge = (GlobleException) e;
            return praseGlobleException(ge);
        }else if((e instanceof InvocationTargetException && ((InvocationTargetException) e).getTargetException() instanceof GlobleException)){
            GlobleException ge = (GlobleException)((InvocationTargetException) e).getTargetException();
            return praseGlobleException(ge);
        }else {
            return R.error(MsgEnum.UNKNOWN_ERROR);
        }

    }

    /**
     * 处理自定义异常
     * @param e
     * @return
     */
    private Msg praseGlobleException(GlobleException e){
        MsgEnum msgEnum = e.getMsgEnum();
        if (msgEnum == null)
            msgEnum = MsgEnum.UNKNOWN_ERROR;
        Msg msg = R.error(msgEnum);
        if (e.getMsgEnum() != null && StrUtil.isNotBlank(e.getMsg()))
            msg.setMsg(e.getMsg());
        return msg;
    }
}
