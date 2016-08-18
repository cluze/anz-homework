package anz.core.application;

import java.util.List;

import anz.core.domain.models.Department;
import anz.core.domain.models.Employee;

public interface EmployeeService {
    Employee create(final Employee employee);

    List<Employee> getAll();

    Employee getById(final long id);

    Department getDepartmentOf(final long id);

    void removeById(final long id);
}