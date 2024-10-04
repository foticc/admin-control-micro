package com.foticc.auth.oauth2.repository;

import java.util.Optional;

import com.foticc.auth.oauth2.domain.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    Optional<Client> findByClientId(String clientId);

    /**
     * manager
     */
    Page<Client> findByClientNameLike(Pageable pageable,String clientName);


    /**
     * manager
     */
}
