/*
 * Copyright (c) 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 */

package org.chip.ihl.surveymanager.service;

import org.chip.ihl.surveymanager.entity.RedcapProject;
import org.chip.ihl.surveymanager.repository.RedcapProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of RedcapProjectService
 * Created by sboykin on 12/29/2014.
 */
@Service
public class RedcapProjectWrapper implements RedcapProjectService {
    private RedcapProjectRepository repository;

    public RedcapProjectWrapper(RedcapProjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public RedcapProject saveProject(RedcapProject project) {
        if (project == null) {
            throw new IllegalArgumentException("Project must not be null");
        } else if (project.getUrl() == null || project.getUrl().isEmpty()) {
            throw new IllegalArgumentException("Invalid Project URL (must not be empty)");
        } else if (project.getApiToken() == null || project.getApiToken().isEmpty()) {
            throw new IllegalArgumentException("API token must not be null");
        } else {
            // if this is an update, get the old project and refresh
            // otherwise, insert as new
            RedcapProject persistedProject = repository.findOne(project.getId());
            if (persistedProject != null) {
                persistedProject.setName(project.getName());
                persistedProject.setUrl(project.getUrl());
                persistedProject.setApiToken(project.getApiToken());
            } else {
                persistedProject = project;
            }
            return repository.save(persistedProject);
        }
    }

    @Override
    public RedcapProject findProject(String projectUrl) {
        if (projectUrl == null || projectUrl.isEmpty()) {
            throw new IllegalArgumentException("Project ID must not be empty");
        } else {
            return repository.findFirstByUrl(projectUrl);
        }
    }

    @Override
    public List<RedcapProject> getAllProjects() {
        return repository.findAll();
    }

    @Override
    public long deleteProject(String projectUrl) {
        if (projectUrl == null || projectUrl.isEmpty()) {
            throw new IllegalArgumentException("Project ID must not be empty");
        } else {
            return repository.deleteByUrl(projectUrl);
        }
    }
}
