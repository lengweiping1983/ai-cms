package com.ai.common.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 基础数据库操作类
 */
@NoRepositoryBean
public interface AbstractRepository<T, ID extends Serializable> extends
		JpaSpecificationExecutor<T>, JpaRepository<T, ID> {

}
