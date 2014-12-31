/*
 * Copyright (c) 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 */

package org.chip.ihl.surveymanager.service;

import org.chip.ihl.surveymanager.entity.RedcapProject;

import java.util.List;

/**
 * Service to handle RedcapProjects, interface with persistent storage
 * Created by sboykin on 12/29/2014.
 */
public interface RedcapProjectService {
    /**
     * Creates/updates project metadata
     * @param project - the project to persist
     * @return the updated/saved project
     */
    public RedcapProject saveProject(RedcapProject project);

    /**
     * Retrieve a RedcapProject
     * @param projectUrl - the url of the project to retrieve
     * @return - the stored project
     */
    public RedcapProject findProject(String projectUrl);

    /**
     * get all projects in repo
     * @return the projects
     */
    List<RedcapProject> getAllProjects();

    /**
     * Delete a RedcapProject
     * @param projectUrl - the project to delete
     */
    public long deleteProject(String projectUrl);
}
