package anz.api.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import anz.core.domain.models.Employee;
import anz.core.domain.models.Gender;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDto {
    private Long id;

    private String firstname;

    private String lastname;

    private String ldapUsername;

    private String gender;

    private long birthdate;

    private String title;

    private String grade;

    private Long departmentId;

    EmployeeDto() {
    }

    public EmployeeDto(final Employee employee) {
        id = employee.getId();
        firstname = employee.getFirstname();
        lastname = employee.getLastname();
        ldapUsername = employee.getLdapUsername();
        gender = employee.getGender() == null ? null : employee.getGender().toString();
        birthdate = employee.getBirthdate();
        title = employee.getTitle();
        grade = employee.getGrade();
        departmentId = employee.getDepartmentId();
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getLdapUsername() {
        return ldapUsername;
    }

    public Gender getGender() {
        return Gender.fromValue(gender);
    }

    public long getBirthdate() {
        return birthdate;
    }

    public String getTitle() {
        return title;
    }

    public String getGrade() {
        return grade;
    }

    public Long getDepartmentId() {
        return departmentId;
    }
}