package com.foticc.auth.manager.repos;

import com.foticc.auth.manager.entity.SysUser;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysUserRepository extends ListCrudRepository<SysUser,Long> {

    Optional<SysUser> findByUsername(String username);
}
