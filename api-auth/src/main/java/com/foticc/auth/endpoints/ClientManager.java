package com.foticc.auth.endpoints;

import com.foticc.auth.endpoints.service.ClientService;
import com.foticc.auth.oauth2.domain.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/client/manager")
@RestController
public class ClientManager {


    private final ClientService clientService;

    public ClientManager(ClientService clientService) {
        this.clientService = clientService;
    }


    @GetMapping("/page")
    public Page<Client> page(@RequestParam(value = "name",required = false) String name,
                                @RequestParam("page") Integer page,
                                @RequestParam("size") Integer size
                             ) {
        return clientService.clientPage(PageRequest.of(page,size),name);
    }


}
