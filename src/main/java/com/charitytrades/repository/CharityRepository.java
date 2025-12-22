package com.charitytrades.repository;

import com.charitytrades.entity.Charity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharityRepository extends JpaRepository<Charity, Long> {
    Optional<Charity> findByName(String name);
}
