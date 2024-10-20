package com.foticc.auth.endpoints.service;


import com.foticc.auth.oauth2.entity.Client;
import com.foticc.auth.oauth2.repository.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Page<Client> clientPage(Pageable pageable,String clientName) {
        if (!StringUtils.hasText(clientName)) {
            return clientRepository.findAll(pageable);
        }
        return clientRepository.findByClientNameLike(pageable,clientName);
    }
}
