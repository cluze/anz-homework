package anz.core.application.impls;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import anz.core.application.DepartmentService;
import anz.core.application.shared.InvalidEntityException;
import anz.core.domain.models.Department;
import anz.core.domain.models.Employee;
import anz.core.repositories.DepartmentRepository;
import anz.core.repositories.EmployeeRepository;

@Service
class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    private final EmployeeRepository employeeRepository;

    @Autowired(required = true)
    DepartmentServiceImpl(final DepartmentRepository departmentRepository, final EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Department create(final Department department) {
        Validate.notNull(department, "department cannot be null");
        final String name = department.getName();
        final Department existDepartment = departmentRepository.findByNameIgnoreCase(name);
        if (null != existDepartment) {
            throw new EntityExistsException("Department with name [" + name + "] already exists");
        }
        final Long managerId = department.getManagerId();
        if (null != managerId) {
            final Employee employee = employeeRepository.findOne(managerId);
            if (null == employee) {
                throw new EntityNotFoundException("Manager with id [" + managerId + "] cannot be found");
            }
        }

        final Long parentId = department.getParentDepartmentId();
        if (null != parentId) {
            final Department parent = getById(parentId);
            if (null == parent) {
                throw new EntityNotFoundException("Parent department with id [" + parentId + "] cannot be found");
            }
        }
        return departmentRepository.save(department);
    }

    @Override
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getById(final long id) {
        return departmentRepository.findOne(id);
    }

    @Override
    public Department getParentById(final long id) {
        final Department existDepartment = getById(id);
        if (null == existDepartment) {
            return null;
        }
        final Long parentId = existDepartment.getParentDepartmentId();
        if (null == parentId) {
            return null;
        }
        return getById(parentId);
    }

    @Override
    public List<Department> getSubDepartmentsById(final long id) {
        return departmentRepository.findByParentDepartmentId(id);
    }

    @Override
    public List<Employee> getEmployees(final long id) {
        return employeeRepository.findByDepartmentId(id);
    }

    @Override
    public Department update(final Department department) {
        Validate.notNull(department, "department cannot be null");
        final Long managerId = department.getManagerId();
        if (null != managerId) {
            final Employee employee = employeeRepository.findOne(managerId);
            if (null == employee) {
                throw new EntityNotFoundException("Manager with id [" + managerId + "] cannot be found");
            }
        }

        final Long id = department.getId();
        final Long parentId = department.getParentDepartmentId();
        if (null != parentId) {
            if (id.equals(parentId)) {
                throw new InvalidEntityException("Department cannot be it's own parent");
            }
            final Department parent = getById(parentId);
            if (null == parent) {
                throw new EntityNotFoundException("Parent department with id [" + parentId + "] cannot be found");
            }
            if (childOf(id, parentId)) {
                throw new InvalidEntityException("Parent of a department cannot be it's child");
            }
        }
        return departmentRepository.save(department);
    }

    @Override
    public void removeById(final long id) {
        final Department existDepartment = getById(id);
        if (null == existDepartment) {
            throw new EntityNotFoundException("No such department can be found, department id: [" + id + "]");
        }
        final List<Department> subDepartments = getSubDepartmentsById(id);
        if (!subDepartments.isEmpty()) {
            throw new EntityExistsException("Sub-departments exist, cannot remove");
        }
        departmentRepository.delete(id);
    }

    private boolean childOf(final Long id, final Long parentId) {
        if (id.equals(parentId)) {
            return true;
        }
        final Department department = getById(parentId);
        final Long parent = department.getParentDepartmentId();
        if (null == parent) {
            return false;
        }
        return childOf(id, parent);
    }
}