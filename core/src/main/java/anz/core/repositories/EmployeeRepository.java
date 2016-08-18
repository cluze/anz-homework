package anz.core.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import anz.core.domain.models.Employee;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    List<Employee> findAll();

    Employee findByLdapUsername(final String ldapUsername);
    
    List<Employee> findByDepartmentId(final Long departmentId);
}