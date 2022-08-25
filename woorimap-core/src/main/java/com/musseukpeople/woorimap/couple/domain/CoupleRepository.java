package com.musseukpeople.woorimap.couple.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.musseukpeople.woorimap.couple.infrastructure.CoupleQueryRepository;

public interface CoupleRepository extends JpaRepository<Couple, Long>, CoupleQueryRepository {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Couple c SET c.deleted = true WHERE c.id = :coupleId")
    void updateDeletedById(@Param("coupleId") Long coupleId);

}
