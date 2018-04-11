package com.ai.epg.seq.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.seq.entity.Sequence;

@Repository
public interface SequenceRepository extends AbstractRepository<Sequence, Long> {

    // @Query(" select t.currentValue from Sequence t where t.name = :name ")
    // Integer findSeq(@Param("name") String name);
    //
    // @Query(value = " select nextval('StarSeq') ", nativeQuery = true)
    // Integer findStarSeq();
    //
    // @Query(value = " select nextval('UriSeq') ", nativeQuery = true)
    // Integer findUriSeq();

    @Query(value = " select nextval('TransactionIDSeq') ", nativeQuery = true)
    Long findTransactionIDSeq();
    
    @Query(value = " select nextval('UserIDSeq') ", nativeQuery = true)
    Long findUserIDSeq();
    
    @Query(value = " select nextval('OrderCodeSeq') ", nativeQuery = true)
    Long findOrderCodeSeq();
    
}