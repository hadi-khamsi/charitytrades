package com.charitytrades.service;

import com.charitytrades.entity.Project;
import com.charitytrades.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    public List<Project> findByCharityId(Long charityId) {
        return projectRepository.findByCharityId(charityId);
    }

    public Project save(Project project) {
        return projectRepository.save(project);
    }
}
