package com.ai.common.service;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.ai.common.repository.AbstractRepository;

/**
 * 增删改查的基础服务类
 */
public abstract class AbstractService<T, ID extends Serializable> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public abstract AbstractRepository<T, ID> getRepository();

    /**
     * 根据id查找对象
     *
     * @param id
     * @return
     */
    public T findById(ID id) {
        return (T) getRepository().findOne(id);
    }

    /**
     * 查询所有的对象
     *
     * @return
     */
    public List<T> findAll() {
        return (List<T>) getRepository().findAll();
    }

    /**
     * 分页查询相关对象
     *
     * @param pageRequest
     * @return
     */
    public Page<T> findAll(PageRequest pageRequest) {
        return getRepository().findAll(pageRequest);
    }

    /**
     * 条件分页查询相关对象
     *
     * @param specification
     * @param pageRequest
     * @return
     */
    public Page<T> findAll(Specification<T> specification, PageRequest pageRequest) {
        return getRepository().findAll(specification, pageRequest);
    }

    /**
     * 增加或修改对象
     *
     * @param object
     * @return
     */
    @Transactional(readOnly = false)
    public T save(T object) {
        return getRepository().save(object);
    }

    /**
     * 删除对象
     *
     * @param object
     */
    @Transactional(readOnly = false)
    public void delete(T object) {
        getRepository().delete(object);
    }

    /**
     * 删除对象
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = false)
    public void deleteById(ID id) {
        getRepository().delete(id);
    }

}
