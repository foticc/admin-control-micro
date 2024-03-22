package com.foticc.auth.manager.repos;

import com.foticc.auth.manager.entity.SysRole;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysRoleRepository extends ListCrudRepository<SysRole,Long> {

    List<SysRole> findByUserId(Long userId);

}
