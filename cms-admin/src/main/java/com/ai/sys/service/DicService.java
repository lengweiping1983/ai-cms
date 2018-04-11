package com.ai.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;
import com.ai.sys.entity.Dic;
import com.ai.sys.repository.DicRepository;

@Service
@Transactional(value = "sysTransactionManager", readOnly = true)
public class DicService extends AbstractService<Dic, Long> {

    @Autowired
    private DicRepository dicRepository;

    @Override
    public AbstractRepository<Dic, Long> getRepository() {
        return dicRepository;
    }

}
