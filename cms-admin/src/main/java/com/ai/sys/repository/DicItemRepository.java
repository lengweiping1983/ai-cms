package com.ai.sys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.sys.entity.DicItem;

@Repository
public interface DicItemRepository extends AbstractRepository<DicItem, Long> {

    @Query("SELECT v FROM Dic as d left join d.dicItemList as v where d.id = :dicId and v.value = :val")
    List<DicItem> findByDicItem(@Param(value = "dicId") Long dicId, @Param(value = "val") String val);

}
