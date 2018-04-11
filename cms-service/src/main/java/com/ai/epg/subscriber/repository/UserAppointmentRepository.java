package com.ai.epg.subscriber.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.subscriber.entity.UserAppointment;

@Repository
public interface UserAppointmentRepository extends AbstractRepository<UserAppointment, Long> {

    List<UserAppointment> findByIdIn(List<Long> idList);

    @Modifying
    @Query(" delete from UserAppointment t where t.userId = :userId and t.itemId = :itemId and t.appCode = :appCode")
    void deleteByUserIdAndItemId(@Param("userId") Long userId, @Param("itemId") Long itemId, @Param("appCode") String appCode);

    UserAppointment findOneByUserIdAndItemIdAndAppCode(Long userId, Long itemId, String appCode);

    @Modifying
    @Query(" select t from UserAppointment t where t.userId = :userId and t.itemType = :itemType and t.appCode = :appCode and t.appointmentTime>:appointmentTime ORDER BY appointmentTime ASC")
    List<UserAppointment> getListUserAppointment(@Param("appointmentTime") Date appointmentTime, @Param("userId") Long userId, @Param("itemType") int itemType,
            @Param("appCode") String appCode);

}
