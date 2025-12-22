package com.charitytrades.controller;

import com.charitytrades.dto.ProjectDTO;
import com.charitytrades.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<ProjectDTO> getAllProjects() {
        return projectService.findAll().stream()
                .map(ProjectDTO::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        return projectService.findById(id)
                .map(project -> ResponseEntity.ok(ProjectDTO.fromEntity(project)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/charity/{charityId}")
    public List<ProjectDTO> getProjectsByCharity(@PathVariable Long charityId) {
        return projectService.findByCharityId(charityId).stream()
                .map(ProjectDTO::fromEntity)
                .toList();
    }
}
