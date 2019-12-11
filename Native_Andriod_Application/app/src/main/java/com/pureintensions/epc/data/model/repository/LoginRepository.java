package com.pureintensions.epc.data.model.repository;

import com.pureintensions.epc.data.model.LoggedInUser;
import com.pureintensions.epc.data.model.Project;
import com.pureintensions.epc.data.model.ProjectStatus;
import com.pureintensions.epc.data.model.Task;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoggedInUser user = null;

    private Project selectedProject;

    private ProjectStatus selectedProjectStatus;

    private Task selectedTask;

    public Task getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
    }

    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public ProjectStatus getSelectedProjectStatus() {
        return selectedProjectStatus;
    }

    public void setSelectedProjectStatus(ProjectStatus selectedProjectStatus) {
        this.selectedProjectStatus = selectedProjectStatus;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
    }

    public void setLoggedInUser(LoggedInUser user) {
        this.user = user;
    }

    public LoggedInUser getLoggedInUser(){
        return this.user;
    }

    public Project getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
    }

}
