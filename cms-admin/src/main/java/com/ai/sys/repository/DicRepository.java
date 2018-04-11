package com.ai.sys.repository;

import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.sys.entity.Dic;

@Repository
public interface DicRepository extends AbstractRepository<Dic, Long> {

    Dic findByCode(String code);

}
