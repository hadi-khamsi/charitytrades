package com.charitytrades.repository;

import com.charitytrades.entity.MatchingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchingOrderRepository extends JpaRepository<MatchingOrder, Long> {

    List<MatchingOrder> findByProjectIdAndActiveTrue(Long projectId);

    List<MatchingOrder> findByCorporateUserId(Long userId);

    @Query("SELECT m FROM MatchingOrder m WHERE m.project.id = :projectId AND m.active = true AND m.remainingAmount > 0 ORDER BY m.createdAt ASC")
    List<MatchingOrder> findAvailableMatchersForProject(@Param("projectId") Long projectId);
}
