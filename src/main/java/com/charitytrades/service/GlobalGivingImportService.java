package com.charitytrades.service;

import com.charitytrades.dto.globalgiving.GGProject;
import com.charitytrades.entity.Charity;
import com.charitytrades.entity.Project;
import com.charitytrades.repository.CharityRepository;
import com.charitytrades.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class GlobalGivingImportService {

    private final GlobalGivingService globalGivingService;
    private final CharityRepository charityRepository;
    private final ProjectRepository projectRepository;

    public GlobalGivingImportService(
            GlobalGivingService globalGivingService,
            CharityRepository charityRepository,
            ProjectRepository projectRepository) {
        this.globalGivingService = globalGivingService;
        this.charityRepository = charityRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public Project importProject(Long globalGivingProjectId) {
        List<Project> existing = projectRepository.findAll().stream()
                .filter(p -> globalGivingProjectId.equals(p.getGlobalGivingProjectId()))
                .toList();
        if (!existing.isEmpty()) {
            return existing.get(0);
        }

        GGProject ggProject = globalGivingService.getProjectById(globalGivingProjectId);
        if (ggProject == null) {
            throw new RuntimeException("Project not found on GlobalGiving: " + globalGivingProjectId);
        }

        return createProjectFromGG(ggProject);
    }

    @Transactional
    public List<Project> importFeaturedProjects(int limit) {
        List<GGProject> ggProjects = globalGivingService.getFeaturedProjects();
        List<Project> imported = new ArrayList<>();

        for (int i = 0; i < Math.min(limit, ggProjects.size()); i++) {
            try {
                GGProject ggProject = ggProjects.get(i);
                if (ggProject.getId() == null) continue;

                List<Project> existing = projectRepository.findAll().stream()
                        .filter(p -> ggProject.getId().equals(p.getGlobalGivingProjectId()))
                        .toList();

                if (!existing.isEmpty()) {
                    imported.add(existing.get(0));
                } else {
                    Project project = createProjectFromGG(ggProject);
                    imported.add(project);
                }
            } catch (Exception e) {
                System.err.println("Failed to import project: " + e.getMessage());
            }
        }

        return imported;
    }

    @Transactional
    public List<Project> importProjectsByTheme(String themeId, int limit) {
        List<GGProject> ggProjects = globalGivingService.getProjectsByTheme(themeId);
        List<Project> imported = new ArrayList<>();

        for (int i = 0; i < Math.min(limit, ggProjects.size()); i++) {
            try {
                GGProject ggProject = ggProjects.get(i);
                if (ggProject.getId() == null) continue;

                List<Project> existing = projectRepository.findAll().stream()
                        .filter(p -> ggProject.getId().equals(p.getGlobalGivingProjectId()))
                        .toList();

                if (!existing.isEmpty()) {
                    imported.add(existing.get(0));
                } else {
                    Project project = createProjectFromGG(ggProject);
                    imported.add(project);
                }
            } catch (Exception e) {
                System.err.println("Failed to import project: " + e.getMessage());
            }
        }

        return imported;
    }

    private Project createProjectFromGG(GGProject ggProject) {
        Charity charity = findOrCreateCharity(ggProject);

        Project project = new Project();
        String title = ggProject.getTitle();
        if (title != null && title.length() > 250) {
            title = title.substring(0, 250) + "...";
        }
        project.setName(title);
        String summary = ggProject.getSummary();
        if (summary != null && summary.length() > 1900) {
            summary = summary.substring(0, 1900) + "...";
        }
        project.setDescription(summary);
        project.setImageUrl(ggProject.getImageLink());
        project.setGlobalGivingProjectId(ggProject.getId());
        project.setCharity(charity);
        project.setMinimumAmount(new BigDecimal("10.00"));

        return projectRepository.save(project);
    }

    private Charity findOrCreateCharity(GGProject ggProject) {
        if (ggProject.getOrganization() == null) {
            String charityName = ggProject.getContactName() != null
                    ? ggProject.getContactName()
                    : "Unknown Organization";
            return charityRepository.findByName(charityName)
                    .orElseGet(() -> {
                        Charity c = new Charity(charityName, "Imported from GlobalGiving");
                        return charityRepository.save(c);
                    });
        }

        Long ggOrgId = ggProject.getOrganization().getId();
        String orgName = ggProject.getOrganization().getName();

        List<Charity> existing = charityRepository.findAll().stream()
                .filter(c -> ggOrgId.equals(c.getGlobalGivingOrgId()))
                .toList();

        if (!existing.isEmpty()) {
            return existing.get(0);
        }

        Charity charity = new Charity();
        charity.setName(orgName);
        String mission = ggProject.getOrganization().getMission();
        if (mission != null && mission.length() > 900) {
            mission = mission.substring(0, 900) + "...";
        }
        charity.setDescription(mission);
        charity.setGlobalGivingOrgId(ggOrgId);

        return charityRepository.save(charity);
    }
}
