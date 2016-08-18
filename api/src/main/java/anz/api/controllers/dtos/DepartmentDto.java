package anz.api.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import anz.core.domain.models.Department;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DepartmentDto {
    private Long id;

    private String name;

    private String location;

    private Long managerId;

    private int openPositions;

    private Long parentDepartmentId;

    DepartmentDto() {
    }

    public DepartmentDto(final Department department) {
        id = department.getId();
        name = department.getName();
        location = department.getLocation();
        managerId = department.getManagerId();
        openPositions = department.getOpenPositions();
        parentDepartmentId = department.getParentDepartmentId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Long getManagerId() {
        return managerId;
    }

    public int getOpenPositions() {
        return openPositions;
    }

    public Long getParentDepartmentId() {
        return parentDepartmentId;
    }
}