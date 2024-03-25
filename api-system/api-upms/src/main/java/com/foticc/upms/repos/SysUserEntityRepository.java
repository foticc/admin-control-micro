package com.foticc.upms.repos;

import com.foticc.upms.entity.SysUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysUserEntityRepository extends JpaRepository<SysUserEntity, Long> {

    Optional<SysUserEntity> findByUsername(String username);
}