package com.foticc.upms.client.feign;

import com.foitcc.common.model.CommonResult;
import com.foticc.upms.client.dto.SysAuthUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "api-upms")
public interface RemoteUserService {

    @Nullable
    @PostMapping("/user/load")
    public CommonResult<SysAuthUserDTO> loadUserByUsername(@RequestParam(name = "username") String username);
}
