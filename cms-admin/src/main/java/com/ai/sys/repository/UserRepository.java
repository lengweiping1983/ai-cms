package com.ai.sys.repository;

import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.sys.entity.User;

@Repository
public interface UserRepository extends AbstractRepository<User, Long> {

    User findOneByLoginName(String loginName);

}
