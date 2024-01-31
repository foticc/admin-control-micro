package com.foitcc.apisys.apiclient;

import org.springframework.stereotype.Component;

@Component
public class ConfigServerClientFailCallBack implements ConfigServerClient {
    @Override
    public String data() {
        return "调用失败";
    }
}
