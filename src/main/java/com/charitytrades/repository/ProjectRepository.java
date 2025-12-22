package com.charitytrades.repository;

import com.charitytrades.entity.Project;
import com.charitytrades.entity.Charity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByCharity(Charity charity);
    List<Project> findByCharityId(Long charityId);
}
