package com.foticc.upms.repos;

import com.foticc.upms.entity.SysUserDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysUserDetailEntityRepository extends JpaRepository<SysUserDetailEntity, Long> {

    Optional<SysUserDetailEntity> findByUsername(String username);
}