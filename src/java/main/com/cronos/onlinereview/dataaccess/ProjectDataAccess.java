/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.dataaccess;

import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectPropertyType;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.shared.dataAccess.resultSet.ResultSetContainer;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>A simple DAO for projects backed up by Query Tool.</p>
 *
 * @author isv
 * @version 1.0
 */
public class ProjectDataAccess extends BaseDataAccess {

    /**
     * <p>Constructs new <code>ProjectDataAccess</code> instance. This implementation does nothing.</p>
     */
    public ProjectDataAccess() {
    }

    /**
     * <p>Gets all active projects.</p>
     *
     * @param projectStatuses a <code>ProjectStatus</code> array listing the available project statuses.
     * @param projectCategories a <code>ProjectCategory</code> array listing available project categories.
     * @param projectInfoTypes a <code>ProjectPropertyType</code> lising available project info types.
     * @return a <code>Project</code> array listing the projects of specified status.
     * @throws DataAccessException if an unexpected error occurs while running the query via Query Tool.
     */
    public Project[] searchActiveProjects(ProjectStatus[] projectStatuses, ProjectCategory[] projectCategories,
                                          ProjectPropertyType[] projectInfoTypes) {
        return searchProjectsByQueryTool("tcs_projects_by_status", "tcs_project_infos_by_status", "stid",
                                         String.valueOf(PROJECT_STATUS_ACTIVE_ID),
                                         projectStatuses, projectCategories, projectInfoTypes);
    }

    /**
     * <p>Gets all inactive projects.</p>
     *
     * @param projectStatuses a <code>ProjectStatus</code> array listing the available project statuses.
     * @param projectCategories a <code>ProjectCategory</code> array listing available project categories.
     * @param projectInfoTypes a <code>ProjectPropertyType</code> lising available project info types.
     * @return a <code>Project</code> array listing the projects of specified status.
     * @throws DataAccessException if an unexpected error occurs while running the query via Query Tool.
     */
    public Project[] searchInactiveProjects(ProjectStatus[] projectStatuses, ProjectCategory[] projectCategories,
                                            ProjectPropertyType[] projectInfoTypes) {
        return searchProjectsByQueryTool("tcs_projects_by_status", "tcs_project_infos_by_status", "stid",
                                         String.valueOf(PROJECT_STATUS_INACTIVE_ID),
                                         projectStatuses, projectCategories, projectInfoTypes);
    }

    /**
     * <p>Gets all active projects assigned to specified user.</p>
     *
     * @param userId a <code>long</code> providing the user ID.
     * @param projectStatuses a <code>ProjectStatus</code> array listing the available project statuses.
     * @param projectCategories a <code>ProjectCategory</code> array listing available project categories.
     * @param projectInfoTypes a <code>ProjectPropertyType</code> lising available project info types.
     * @return a <code>Project</code> array listing the projects of specified status.
     * @throws DataAccessException if an unexpected error occurs while running the query via Query Tool.
     */
    public Project[] searchUserActiveProjects(long userId, ProjectStatus[] projectStatuses,
                                              ProjectCategory[] projectCategories,
                                              ProjectPropertyType[] projectInfoTypes) {
        return searchProjectsByQueryTool("tcs_projects_by_user", "tcs_project_infos_by_user", "uid",
                                         String.valueOf(userId),
                                         projectStatuses, projectCategories, projectInfoTypes);
    }

    /**
     * <p>Gets the list of projects of specified status.</p>
     *
     * @param projectQuery a <code>String</code> providing the name of the query to be run for getting the project
     *        details.
     * @param projectInfoQuery a <code>String</code> providing the name of the query to be run for getting the project
     *        info details.
     * @param paramName a <code>String</code> providing the name of the query parameter for customization.
     * @param paramValue a <code>String</code> providing the value of the query parameter for customization.
     * @param projectStatuses a <code>ProjectStatus</code> array listing the available project statuses.
     * @param projectCategories a <code>ProjectCategory</code> array listing available project categories.
     * @param projectInfoTypes a <code>ProjectPropertyType</code> lising available project info types.
     * @return a <code>Project</code> array listing the projects of specified status.
     * @throws DataAccessException if an unexpected error occurs while running the query via Query Tool.
     * @since 1.7
     */
    private Project[] searchProjectsByQueryTool(String projectQuery, String projectInfoQuery, String paramName,
                                                String paramValue, ProjectStatus[] projectStatuses,
                                                ProjectCategory[] projectCategories,
                                                ProjectPropertyType[] projectInfoTypes) {

        // Build the cache of project categories and project statuses for faster lookup by ID
        Map<Long, ProjectCategory> categoriesMap = buildProjectCategoriesLookupMap(projectCategories);
        Map<Long, ProjectStatus> statusesMap = buildProjectStatusesLookupMap(projectStatuses);

        // Get project details by status using Query Tool
        Map<String, ResultSetContainer> results = runQuery(projectQuery, paramName, paramValue);

        // Convert returned data into Project objects
        ResultSetContainer projectsData = results.get(projectQuery);
        Map<Long, Project> projects = new HashMap<Long, Project>(projectsData.size());
        int recordNum = projectsData.size();
        Project[] resultingProjects = new Project[recordNum];
        for (int i = 0; i < recordNum; i++) {
            long projectId = projectsData.getLongItem(i, "project_id");
            long projectCategoryId = projectsData.getLongItem(i, "project_category_id");
            long projectStatusId = projectsData.getLongItem(i, "project_status_id");
            String createUser = projectsData.getStringItem(i, "create_user");
            Timestamp createDate = projectsData.getTimestampItem(i, "create_date");
            String modifyUser = projectsData.getStringItem(i, "modify_user");
            Timestamp modifyDate = projectsData.getTimestampItem(i, "modify_date");

            Project project = new Project(projectId, categoriesMap.get(projectCategoryId),
                                          statusesMap.get(projectStatusId));
            project.setCreationUser(createUser);
            project.setCreationTimestamp(createDate);
            project.setModificationUser(modifyUser);
            project.setModificationTimestamp(modifyDate);

            resultingProjects[i] = project;
            projects.put(projectId, project);
        }

        // Build the cache of project info types for faster lookup by ID
        Map<Long, ProjectPropertyType> infoTypesMap = buildProjectPropertyTypesLookupMap(projectInfoTypes);

        ResultSetContainer projectInfosData = results.get(projectInfoQuery);
        recordNum = projectInfosData.size();
        for (int i = 0; i < recordNum; i++) {
            long projectId = projectInfosData.getLongItem(i, "project_id");
            long projectInfoTypeId = projectInfosData.getLongItem(i, "project_info_type_id");
            String value = projectInfosData.getStringItem(i, "value");
            Project project = projects.get(projectId);
            project.setProperty(infoTypesMap.get(projectInfoTypeId).getName(), value);
        }

        return resultingProjects;
    }
}
