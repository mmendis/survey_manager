/*
 * Copyright (c) 2014, Boston Children's Hospital. All Rights Reserved.
 *
 * Developed by the Intelligent Health Lab at Children’s Hospital Informatics Program.
 * For more information, see http://chip.org/ihlab and https://github.com/chb
 */

package org.chip.ihl.surveymanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Holds REDCap project metadata (including token info)
 * Created by sboykin on 12/26/2014.
 */
@Entity
public class RedcapProject {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @Column(unique = true)
    private String url;
    @Column(nullable = false)
    private String apiToken;

    public RedcapProject() {
    }

    public RedcapProject(String url) {
        this.url = url;
    }

    public RedcapProject(String name, String url, String apiToken) {
        this.name = name;
        this.url = url;
        this.apiToken = apiToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RedcapProject that = (RedcapProject) o;

        if (apiToken != null ? !apiToken.equals(that.apiToken) : that.apiToken != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
