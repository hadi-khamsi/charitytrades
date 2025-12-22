package com.charitytrades.dto.globalgiving;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GGProjectsResponse {

    private ProjectsWrapper projects;

    public GGProjectsResponse() {}

    public ProjectsWrapper getProjects() { return projects; }
    public void setProjects(ProjectsWrapper projects) { this.projects = projects; }

    public List<GGProject> getProjectList() {
        if (projects != null && projects.getProject() != null) {
            return projects.getProject();
        }
        return List.of();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProjectsWrapper {
        @JsonProperty("project")
        private List<GGProject> project;
        private Integer numberFound;

        public List<GGProject> getProject() { return project; }
        public void setProject(List<GGProject> project) { this.project = project; }
        public Integer getNumberFound() { return numberFound; }
        public void setNumberFound(Integer numberFound) { this.numberFound = numberFound; }
    }
}
