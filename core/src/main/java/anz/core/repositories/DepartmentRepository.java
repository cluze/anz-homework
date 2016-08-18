package anz.core.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import anz.core.domain.models.Department;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Long> {
    List<Department> findAll();

    Department findByNameIgnoreCase(final String name);

    List<Department> findByParentDepartmentId(final Long partentDepartmentId);

    List<Department> findByManagerId(final Long managerId);
}