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
import anz.core.application.EmployeeService;
import anz.core.domain.models.Department;
import anz.core.domain.models.Employee;

@RestController
class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired(required = true)
    EmployeeController(final EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RequestMapping(value = "/employee", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    EmployeeDto create(@RequestBody final EmployeeDto employeeDto) {
        if (null != employeeDto.getId()) {
            throw new BadRequestException("id cannot be set while creating employee");
        }
        try {
            final Employee.Builder builder = Employee.Builder.newInstance(employeeDto.getFirstname(), employeeDto.getLastname(), employeeDto.getLdapUsername(),
                                                                          employeeDto.getTitle(), employeeDto.getGrade());
            builder.birthdate(employeeDto.getBirthdate()).departmentId(employeeDto.getDepartmentId()).gender(employeeDto.getGender());
            final Employee employee = employeeService.create(builder.build());
            return new EmployeeDto(employee);
        } catch (final NullPointerException | IllegalArgumentException ex) {
            throw new BadRequestException(ex.getMessage(), ex);
        } catch (final EntityExistsException ex) {
            throw new NotAcceptableException(ex.getMessage(), ex);
        } catch (final EntityNotFoundException ex) {
            throw new PreconditionFailedException(ex.getMessage(), ex);
        }
    }

    @RequestMapping(value = "/employees", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    List<EmployeeDto> list() {
        final List<EmployeeDto> result = new ArrayList<>();
        final List<Employee> employees = employeeService.getAll();
        for (final Employee employee : employees) {
            result.add(new EmployeeDto(employee));
        }
        return result;
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    EmployeeDto getById(@PathVariable(value = "id") final long id) {
        final Employee employee = employeeService.getById(id);
        return null == employee ? null : new EmployeeDto(employee);
    }

    @RequestMapping(value = "/employee/{id}/department", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    DepartmentDto getDepartmentOf(@PathVariable(value = "id") final long id) {
        try {
            final Department department = employeeService.getDepartmentOf(id);
            return department == null ? null : new DepartmentDto(department);
        } catch (final EntityNotFoundException ex) {
            throw new NotFoundException(ex.getMessage(), ex);
        }
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    void removeById(@PathVariable(value = "id") final long id) {
        try {
            employeeService.removeById(id);
        } catch (final EntityNotFoundException ex) {
            throw new NotFoundException(ex.getMessage(), ex);
        } catch (final EntityExistsException ex) {
            throw new PreconditionFailedException(ex.getMessage(), ex);
        }
    }
}