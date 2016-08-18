package anz.core.application;

import java.util.List;

import anz.core.domain.models.Department;
import anz.core.domain.models.Employee;

public interface DepartmentService {
    Department create(final Department department);

    List<Department> getAll();

    Department getById(final long id);

    Department getParentById(final long id);

    List<Department> getSubDepartmentsById(final long id);

    List<Employee> getEmployees(long id);

    Department update(final Department department);

    void removeById(final long id);
}