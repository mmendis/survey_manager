/*
 * Copyright (c) 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 */

package org.chip.ihl.surveymanager.repository;

import org.chip.ihl.surveymanager.entity.RedcapProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring data repo for RedcapProject
 * Created by sboykin on 12/29/2014.
 */
public interface RedcapProjectRepository extends JpaRepository<RedcapProject, Integer>{
    public List<RedcapProject> findByUrl(String url);
    public RedcapProject findFirstByUrl(String projectUrl);
    public Long deleteByUrl(String projectUrl);
}
