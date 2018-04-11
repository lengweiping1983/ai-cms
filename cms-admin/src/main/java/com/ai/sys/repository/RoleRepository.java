package com.ai.sys.repository;

import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.sys.entity.Role;

@Repository
public interface RoleRepository extends AbstractRepository<Role, Long> {

}
