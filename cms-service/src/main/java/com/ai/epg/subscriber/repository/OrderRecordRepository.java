package com.ai.epg.subscriber.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.subscriber.entity.OrderRecord;

@Repository
public interface OrderRecordRepository extends AbstractRepository<OrderRecord, Long> {

	@Query(" select s from OrderRecord s where s.orderCode = :orderCode ")
	OrderRecord findOneByOrderCode(@Param("orderCode") String orderCode);

	@Query(" select s from OrderRecord s where s.transactionCode = :transactionCode ")
	OrderRecord findOneByTransactionCode(@Param("transactionCode") String transactionCode);

	@Query(" select s from OrderRecord s where s.partnerUserId = :partnerUserId and s.productCode in :productCodes "
			+ " and s.paymentStatus = 1 and s.expiredTime > :expiredTime and s.unsubscribeTime is null ")
	List<OrderRecord> findByOrderedRecord(@Param("partnerUserId") String partnerUserId,
			@Param("productCodes") List<String> productCodes, @Param("expiredTime") Date expiredTime);

	@Query(" select s from OrderRecord s where s.partnerUserId = :partnerUserId  "
			+ " and s.paymentStatus = 1 and s.expiredTime > :expiredTime and s.unsubscribeTime is null ")
	List<OrderRecord> findByOrderedRecordAll(@Param("partnerUserId") String partnerUserId, @Param("expiredTime") Date expiredTime);

	@Query(" select s from OrderRecord s where s.partnerUserId = :partnerUserId and s.productCode = :productCode and s.paymentStatus !=1 and s.unsubscribeTime is null ")
	List<OrderRecord> findByPartnerUserIdAndProductCode(@Param("partnerUserId") String partnerUserId,
			@Param("productCode") String productCode);

	@Query(" select s from OrderRecord s where s.partnerUserId = :partnerUserId and s.productCode = :productCode and s.contentCode = :contentCode"
			+ " and s.paymentStatus !=1 and s.unsubscribeTime is null ")
	List<OrderRecord> findByPartnerUserIdAndProductCode(@Param("partnerUserId") String partnerUserId,
			@Param("productCode") String productCode, @Param("contentCode") String contentCode);

	@Query(" select s from OrderRecord s where s.partnerUserId = :partnerUserId and s.productCode = :productCode and s.paymentStatus = 1 and s.unsubscribeTime is null "
			+ " and s.expiredTime > :expiredTime ")
	List<OrderRecord> findByPartnerUserIdAndProductCodeAndOrdered(@Param("partnerUserId") String partnerUserId,
			@Param("productCode") String productCode, @Param("expiredTime") Date expiredTime);

	@Query(" select s from OrderRecord s where s.partnerUserId = :partnerUserId and s.paymentStatus = 1 and s.expiredTime > :expiredTime and s.unsubscribeTime is null ")
	List<OrderRecord> findByPartnerUserIdAndExpiredTime(@Param("partnerUserId") String partnerUserId,
			@Param("expiredTime") Date expiredTime);
		
	@Query(" select s from OrderRecord s where s.subscriptionTime >= :subscriptionTime and s.unsubscribeTime < :unsubscribeTime and s.paymentStatus = 1")
	List<OrderRecord> findListByOrderTimeAndUnSubscribeTime(@Param("subscriptionTime") Date subscriptionTime,
			@Param("unsubscribeTime") Date unsubscribeTime);
	
	@Query(" select s from OrderRecord s where s.partnerUserId = :partnerUserId and s.paymentStatus = 1 and s.unsubscribeTime is null ")
	List<OrderRecord> findByPartnerUserId(@Param("partnerUserId") String partnerUserId);
	
	@Query(" select s from OrderRecord s where s.paymentStatus = 1 ")
	Page<OrderRecord> findAllOrderSuccess(Pageable pageable);
	
	@Query(" select s from OrderRecord s where s.paymentStatus = 1 and s.subscriptionTime>= :startTime and s.subscriptionTime<= :endTime")
	Page<OrderRecord> findOrderSuccessByTime(@Param("startTime") Date startTime,@Param("endTime") Date endTime,Pageable pageable);

	@Query(" select s from OrderRecord s where s.paymentStatus = 1 and s.appCode= :appCode and s.expiredTime> :expiredTime ")
	List<OrderRecord> findListByAppCodeAndExpiredTime(@Param("appCode") String appCode,@Param("expiredTime") Date expiredTime);

	@Query(" select s from OrderRecord s where s.appCode = :appCode and s.partnerUserId = :partnerUserId and s.paymentStatus = 1 and s.unsubscribeTime is null ")
	List<OrderRecord> findByPartnerUserIdAndAppCode(@Param("appCode") String appCode, @Param("partnerUserId") String partnerUserId);
}
