package com.ai.epg.subscriber.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.subscriber.entity.Subscriber;

@Repository
public interface SubscriberRepository extends AbstractRepository<Subscriber, Long> {

	@Query(" select s from Subscriber s where s.partnerUserId = :partnerUserId ")
	Subscriber findPartnerUserId(@Param("partnerUserId") String partnerUserId);
	
	@Query(" select s from Subscriber s where s.groupCode = :groupCode ")
	List<Subscriber> findByGroupCode(@Param("groupCode") String groupCode);
	
	@Query(" select s.partnerUserId from Subscriber s where s.groupCode in :groupCodes ")
	List<String> findInGroupCodes(@Param("groupCodes") List<String> groupCodes);
	
	@Query(" select s from Subscriber s where s.reserved5 = :reserved5 ")
	List<Subscriber> findReserved5(@Param("reserved5") String reserved5);
	
	@Query(" select s from Subscriber s where s.userId = :userId ")
	Subscriber findUserId(@Param("userId") String userId);

	@Query(" select s from Subscriber s where s.sn = :sn order by s.lastLoginTime desc ")
	List<Subscriber> findBySn(@Param("sn") String sn);

	@Query(" select s from Subscriber s where s.activateTime >= :activateTime and s.suspendTime < :suspendTime ")
	List<Subscriber> findByActivateTimeAndSuspendTime(@Param("activateTime") Date activateTime,
			@Param("suspendTime") Date suspendTime);
	
	@Query(" select s from Subscriber s")
	Page<Subscriber> findAllSubscriber(Pageable pageable);
	
	@Query(" select s from Subscriber s where s.createTime <= :createTime ")
	Page<Subscriber> findAllSubscriberByCreateTime(@Param("createTime") Date createTime,Pageable pageable);
	
	@Query(" select s from Subscriber s where s.openTime>= :startTime and s.openTime<= :endTime")
	Page<Subscriber> findSubscriberByTime(@Param("startTime") Date startTime,@Param("endTime") Date endTime,Pageable pageable);

}
