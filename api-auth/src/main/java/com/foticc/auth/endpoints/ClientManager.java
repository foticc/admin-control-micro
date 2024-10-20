package com.foticc.auth.endpoints;

import com.foticc.auth.endpoints.service.ClientService;
import com.foticc.auth.oauth2.entity.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/client/manager")
@RestController
public class ClientManager {


    private final ClientService clientService;

    public ClientManager(ClientService clientService) {
        this.clientService = clientService;
    }


    @GetMapping("/page")
    public Page<Client> page(@PageableDefault Pageable page,
                             @RequestParam(value = "name",required = false) String name
                             ) {
        log.info("{}",page);
        return clientService.clientPage(page,name);
    }


}
