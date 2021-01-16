package com.xm.cpsmall.comm.api.client;

import com.taobao.api.DefaultTaobaoClient;

public class MyTaobaoClient extends DefaultTaobaoClient {
    public MyTaobaoClient(String serverUrl, String appKey, String appSecret) {
        super(serverUrl, appKey, appSecret);
    }

    public MyTaobaoClient(String serverUrl, String appKey, String appSecret, String format) {
        super(serverUrl, appKey, appSecret, format);
    }

    public MyTaobaoClient(String serverUrl, String appKey, String appSecret, String format, int connectTimeout, int readTimeout) {
        super(serverUrl, appKey, appSecret, format, connectTimeout, readTimeout);
    }

    public MyTaobaoClient(String serverUrl, String appKey, String appSecret, String format, int connectTimeout, int readTimeout, String signMethod) {
        super(serverUrl, appKey, appSecret, format, connectTimeout, readTimeout, signMethod);
    }
}
