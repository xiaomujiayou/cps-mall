package com.xm.cpsmall.module.mall.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.xm.cpsmall.annotation.LoginUser;
import com.xm.cpsmall.comm.api.config.OSSConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RequestMapping("/api-mall/upload")
@RestController
public class FileUploadController {

    @Autowired
    private OSSConfig ossConfig;

    @Resource(name = "ossClient")
    private OSS ossClient;

    @GetMapping("/sign")
    public Object getSign(@LoginUser Integer userId) throws UnsupportedEncodingException {

        //限定文件目录
        String dir = "user/"+userId+"/feedback/";
        //限定文件大小
        Integer maxSize = 10;
        return createSign(30,dir,maxSize);
    }

    /**
     * 获取上传文件sign
     * @param expire    :有效时间(s)
     * @param dir       :上传路径
     * @param maxSize   :文件大小(M)
     * @return
     * @throws UnsupportedEncodingException
     */
    private Map<String, String> createSign(Integer expire,String dir,Integer maxSize) throws UnsupportedEncodingException {
        String host = "https://" + ossConfig.getBucketName() + "." + ossConfig.getEndpoint();
        long expireEndTime = System.currentTimeMillis() + expire * 1000;
        Date expiration = new Date(expireEndTime);
        PolicyConditions policyConds = new PolicyConditions();
//        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxSize * 1024 * 1024);
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
        String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
        byte[] binaryData = postPolicy.getBytes("utf-8");
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossClient.calculatePostSignature(postPolicy);
        Map<String, String> respMap = new LinkedHashMap<String, String>();
        respMap.put("accessid", ossConfig.getAccessKeyId());
        respMap.put("policy", encodedPolicy);
        respMap.put("signature", postSignature);
        respMap.put("dir", dir);
        respMap.put("host", host);
        respMap.put("expire", String.valueOf(expireEndTime / 1000));
        return respMap;
    }

}
