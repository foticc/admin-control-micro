package com.foitcc.apisys.apiclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "config-server",fallback = ConfigServerClientFailCallBack.class)
public interface ConfigServerClient {

    @GetMapping("/data")
    String data();
}
