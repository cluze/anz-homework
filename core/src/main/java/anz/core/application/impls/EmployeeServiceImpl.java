package anz.core.application.impls;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import anz.core.application.EmployeeService;
import anz.core.domain.models.Department;
import anz.core.domain.models.Employee;
import anz.core.repositories.DepartmentRepository;
import anz.core.repositories.EmployeeRepository;

@Service
class EmployeeServiceImpl implements EmployeeService {
    private final DepartmentRepository departmentRepository;

    private final EmployeeRepository employeeRepository;

    @Autowired(required = true)
    EmployeeServiceImpl(final DepartmentRepository departmentRepository, final EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee create(final Employee employee) {
        Validate.notNull(employee, "employee cannot be null");
        final Long departmentId = employee.getDepartmentId();
        if (null != departmentId) {
            if (null == departmentRepository.findOne(departmentId)) {
                throw new EntityNotFoundException("Parent department with id [" + departmentId + "] cannot be found");
            }
        }
        final String ldapUsername = employee.getLdapUsername();
        final Employee existEmployee = employeeRepository.findByLdapUsername(ldapUsername);
        if (null != existEmployee) {
            throw new EntityExistsException("Employee with ldap username [" + ldapUsername + "] already exists");
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getById(final long id) {
        return employeeRepository.findOne(id);
    }

    @Override
    public Department getDepartmentOf(final long id) {
        final Employee employee = employeeRepository.findOne(id);
        if (null == employee) {
            throw new EntityNotFoundException("No such employee can be found, employee id: [" + id + "]");
        }
        final Long departmentId = employee.getDepartmentId();
        if (null == departmentId) {
            return null;
        }
        return departmentRepository.findOne(departmentId);
    }

    @Override
    public void removeById(final long id) {
        final Employee existEmployee = getById(id);
        if (null == existEmployee) {
            throw new EntityNotFoundException("No such employee can be found, employee id: [" + id + "]");
        }
        final List<Department> departments = departmentRepository.findByManagerId(existEmployee.getId());
        if (!departments.isEmpty()) {
            throw new EntityExistsException("Manager of some departments, cannot be removed");
        }
        employeeRepository.delete(id);
    }

}