package com.foticc.upms.repos;

import com.foticc.upms.entity.SysRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleEntityRepository extends JpaRepository<SysRoleEntity, Long> {


}