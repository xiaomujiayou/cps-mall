package com.xm.cpsmall.module.user.controller;

import com.xm.cpsmall.module.user.serialize.entity.SuPidEntity;
import com.xm.cpsmall.module.user.service.PidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 推广位相关接口
 */
@RestController
@RequestMapping("/api-user/pid")
public class PidController {

    @Autowired
    private PidService pidService;

    @GetMapping("/generate")
    public SuPidEntity generatePid() throws Exception {
        return pidService.generatePid();
    }
    @GetMapping
    public SuPidEntity getPid(Integer id) throws Exception {
        return pidService.getPid(id);
    }
}
