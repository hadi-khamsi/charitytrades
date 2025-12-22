package com.charitytrades.controller;

import com.charitytrades.dto.ProjectDTO;
import com.charitytrades.dto.globalgiving.GGProject;
import com.charitytrades.dto.globalgiving.GGTheme;
import com.charitytrades.entity.Project;
import com.charitytrades.service.GlobalGivingImportService;
import com.charitytrades.service.GlobalGivingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/globalgiving")
public class GlobalGivingController {

    private final GlobalGivingService globalGivingService;
    private final GlobalGivingImportService importService;

    public GlobalGivingController(
            GlobalGivingService globalGivingService,
            GlobalGivingImportService importService) {
        this.globalGivingService = globalGivingService;
        this.importService = importService;
    }

    @GetMapping("/projects/featured")
    public List<GGProject> getFeaturedProjects() {
        return globalGivingService.getFeaturedProjects();
    }

    @GetMapping("/projects/theme/{themeId}")
    public List<GGProject> getProjectsByTheme(@PathVariable String themeId) {
        return globalGivingService.getProjectsByTheme(themeId);
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<GGProject> getProjectById(@PathVariable Long projectId) {
        GGProject project = globalGivingService.getProjectById(projectId);
        return project != null ? ResponseEntity.ok(project) : ResponseEntity.notFound().build();
    }

    @GetMapping("/organizations/{orgId}/projects")
    public List<GGProject> getProjectsByOrganization(@PathVariable Long orgId) {
        return globalGivingService.getProjectsByOrganization(orgId);
    }

    @GetMapping("/themes")
    public List<GGTheme> getThemes() {
        return globalGivingService.getAvailableThemes();
    }

    @PostMapping("/import/{projectId}")
    public ResponseEntity<ProjectDTO> importProject(@PathVariable Long projectId) {
        try {
            Project project = importService.importProject(projectId);
            return ResponseEntity.ok(ProjectDTO.fromEntity(project));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/import/featured")
    public List<ProjectDTO> importFeaturedProjects(@RequestParam(defaultValue = "5") int limit) {
        return importService.importFeaturedProjects(limit).stream()
                .map(ProjectDTO::fromEntity)
                .toList();
    }

    @PostMapping("/import/theme/{themeId}")
    public List<ProjectDTO> importProjectsByTheme(
            @PathVariable String themeId,
            @RequestParam(defaultValue = "5") int limit) {
        return importService.importProjectsByTheme(themeId, limit).stream()
                .map(ProjectDTO::fromEntity)
                .toList();
    }
}
