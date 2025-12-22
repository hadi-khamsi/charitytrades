package com.charitytrades.repository;

import com.charitytrades.entity.CentralBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CentralBookRepository extends JpaRepository<CentralBook, Long> {
    Optional<CentralBook> findByProjectId(Long projectId);
}
