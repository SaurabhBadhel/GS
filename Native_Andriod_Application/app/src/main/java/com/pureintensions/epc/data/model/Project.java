package com.pureintensions.epc.data.model;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class Project {

    private String projectId;
    private String projectName;
    private String projectDate;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDate() {
        return projectDate;
    }

    public void setProjectDate(String projectDate) {
        this.projectDate = projectDate;
    }
}
