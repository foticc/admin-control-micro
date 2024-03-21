package com.foticc.upms.client.feign;

import com.foticc.upms.client.dto.SysAuthUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "api-upms")
public interface RemoteUserService {

    @Nullable
    @PostMapping("/user/load")
    public SysAuthUserDTO loadUserByUsername(String username);
}
