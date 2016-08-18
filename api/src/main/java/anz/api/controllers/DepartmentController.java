package anz.api.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import anz.api.controllers.dtos.DepartmentDto;
import anz.api.controllers.dtos.EmployeeDto;
import anz.api.controllers.exceptions.BadRequestException;
import anz.api.controllers.exceptions.NotAcceptableException;
import anz.api.controllers.exceptions.NotFoundException;
import anz.api.controllers.exceptions.PreconditionFailedException;
import anz.core.application.DepartmentService;
import anz.core.application.shared.InvalidEntityException;
import anz.core.domain.models.Department;
import anz.core.domain.models.Employee;

@RestController
class DepartmentController {
    private final DepartmentService departmentService;

    @Autowired(required = true)
    DepartmentController(final DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @RequestMapping(value = "/department", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    DepartmentDto create(@RequestBody final DepartmentDto departmentDto) {
        if (null != departmentDto.getId()) {
            throw new BadRequestException("id cannot be set while creating deparment");
        }
        try {
            final Department.Builder builder = Department.Builder.newInstance(departmentDto.getName(), departmentDto.getOpenPositions());
            builder.location(departmentDto.getLocation()).managerId(departmentDto.getManagerId()).parentDepartment(departmentDto.getParentDepartmentId());
            final Department department = departmentService.create(builder.build());
            return new DepartmentDto(department);
        } catch (final NullPointerException | IllegalArgumentException ex) {
            throw new BadRequestException(ex.getMessage(), ex);
        } catch (final EntityExistsException ex) {
            throw new NotAcceptableException(ex.getMessage(), ex);
        } catch (final EntityNotFoundException ex) {
            throw new PreconditionFailedException(ex.getMessage(), ex);
        }
    }

    @RequestMapping(value = "/departments", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    List<DepartmentDto> list() {
        final List<Department> departments = departmentService.getAll();
        return toDepartmentDtoList(departments);
    }

    @RequestMapping(value = "/department/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    DepartmentDto getById(@PathVariable(value = "id") final long id) {
        final Department department = departmentService.getById(id);
        return toDepartmentDto(department);
    }

    @RequestMapping(value = "/department/{id}/parent", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    DepartmentDto getParentOf(@PathVariable(value = "id") final long id) {
        final Department department = departmentService.getParentById(id);
        return toDepartmentDto(department);
    }

    // only the direct children will be retrieved
    @RequestMapping(value = "/department/{id}/children", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    List<DepartmentDto> getChildrenOf(@PathVariable(value = "id") final long id) {
        final List<Department> departments = departmentService.getSubDepartmentsById(id);
        return toDepartmentDtoList(departments);
    }

    @RequestMapping(value = "/department/{id}/employees", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    List<EmployeeDto> getEmployeesOf(@PathVariable(value = "id") final long id) {
        final List<EmployeeDto> result = new ArrayList<>();
        final List<Employee> employees = departmentService.getEmployees(id);
        for (final Employee employee : employees) {
            result.add(new EmployeeDto(employee));
        }
        return result;
    }

    @RequestMapping(value = "/department", method = RequestMethod.PUT, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    DepartmentDto update(@RequestBody final DepartmentDto departmentDto) {
        final Long id = departmentDto.getId();
        if (null == id) {
            throw new BadRequestException("id must be provided while updating deparment");
        }
        final Department existDepartment = departmentService.getById(id);
        if (null == existDepartment) {
            throw new NotFoundException("No such department can be found, department id: [" + id + "]");
        }
        try {
            existDepartment.update(departmentDto.getOpenPositions(), departmentDto.getLocation(), departmentDto.getManagerId(), departmentDto.getParentDepartmentId());
            final Department department = departmentService.update(existDepartment);
            return new DepartmentDto(department);
        } catch (final InvalidEntityException ex) {
            throw new NotAcceptableException(ex.getMessage(), ex);
        } catch (final EntityExistsException ex) {
            throw new NotAcceptableException(ex.getMessage(), ex);
        } catch (final EntityNotFoundException ex) {
            throw new PreconditionFailedException(ex.getMessage(), ex);
        }
    }

    @RequestMapping(value = "/department/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    void removeById(@PathVariable(value = "id") final long id) {
        try {
            departmentService.removeById(id);
        } catch (final EntityNotFoundException ex) {
            throw new NotFoundException(ex.getMessage(), ex);
        } catch (final EntityExistsException ex) {
            throw new PreconditionFailedException(ex.getMessage(), ex);
        }
    }

    private DepartmentDto toDepartmentDto(final Department department) {
        return null == department ? null : new DepartmentDto(department);
    }

    private List<DepartmentDto> toDepartmentDtoList(final List<Department> departments) {
        final List<DepartmentDto> result = new ArrayList<>();
        for (final Department department : departments) {
            result.add(new DepartmentDto(department));
        }
        return result;
    }
}