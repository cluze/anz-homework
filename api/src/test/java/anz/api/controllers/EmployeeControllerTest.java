package anz.api.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Assert;
import org.junit.Test;

import anz.api.controllers.dtos.DepartmentDto;
import anz.api.controllers.dtos.EmployeeDto;
import anz.api.controllers.exceptions.BadRequestException;
import anz.api.controllers.exceptions.NotAcceptableException;
import anz.api.controllers.exceptions.NotFoundException;
import anz.api.controllers.exceptions.PreconditionFailedException;
import anz.core.application.EmployeeService;
import anz.core.domain.models.Department;
import anz.core.domain.models.Employee;

public class EmployeeControllerTest {
    private Mockery mockery = new Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    private EmployeeService employeeService = mockery.mock(EmployeeService.class);

    private EmployeeController target = new EmployeeController(employeeService);

    @Test(expected = BadRequestException.class)
    public void testCreateWhenDepartmentIdIsNotNull() {
        final EmployeeDto employeeDto = mockery.mock(EmployeeDto.class);

        mockery.checking(new Expectations() {
            {
                oneOf(employeeDto).getId();
                will(returnValue(1L));
            }
        });

        target.create(employeeDto);

        mockery.assertIsSatisfied();
    }

    @Test(expected = BadRequestException.class)
    public void testCreateWhenNullPointerExceptionThrown() {
        final Employee employee = Employee.Builder.newInstance("firstname", "lastname", "ldapUsername", "title", "grade").build();
        final EmployeeDto employeeDto = new EmployeeDto(employee);

        mockery.checking(new Expectations() {
            {
                oneOf(employeeService).create(with(any(Employee.class)));
                will(throwException(new NullPointerException()));
            }
        });

        target.create(employeeDto);

        mockery.assertIsSatisfied();
    }

    @Test(expected = BadRequestException.class)
    public void testCreateWhenIllegalArgumentExceptionThrown() {
        final Employee employee = Employee.Builder.newInstance("firstname", "lastname", "ldapUsername", "title", "grade").build();
        final EmployeeDto employeeDto = new EmployeeDto(employee);

        mockery.checking(new Expectations() {
            {
                oneOf(employeeService).create(with(any(Employee.class)));
                will(throwException(new IllegalArgumentException()));
            }
        });

        target.create(employeeDto);

        mockery.assertIsSatisfied();
    }

    @Test(expected = NotAcceptableException.class)
    public void testCreateWhenEntityExistsExceptionThrown() {
        final Employee employee = Employee.Builder.newInstance("firstname", "lastname", "ldapUsername", "title", "grade").build();
        final EmployeeDto employeeDto = new EmployeeDto(employee);

        mockery.checking(new Expectations() {
            {
                oneOf(employeeService).create(with(any(Employee.class)));
                will(throwException(new EntityExistsException()));
            }
        });

        target.create(employeeDto);

        mockery.assertIsSatisfied();
    }

    @Test(expected = PreconditionFailedException.class)
    public void testCreateWhenEntityNotFoundExceptionThrown() {
        final Employee employee = Employee.Builder.newInstance("firstname", "lastname", "ldapUsername", "title", "grade").build();
        final EmployeeDto employeeDto = new EmployeeDto(employee);

        mockery.checking(new Expectations() {
            {
                oneOf(employeeService).create(with(any(Employee.class)));
                will(throwException(new EntityNotFoundException()));
            }
        });

        target.create(employeeDto);

        mockery.assertIsSatisfied();
    }

    @Test
    public void testCreate() {
        final Employee employee = Employee.Builder.newInstance("firstname", "lastname", "ldapUsername", "title", "grade").build();
        final EmployeeDto employeeDto = new EmployeeDto(employee);

        mockery.checking(new Expectations() {
            {
                oneOf(employeeService).create(with(any(Employee.class)));
                will(returnValue(employee));
            }
        });

        EmployeeDto actual = target.create(employeeDto);

        mockery.assertIsSatisfied();
        Assert.assertEquals(employeeDto.getFirstname(), actual.getFirstname());
        Assert.assertEquals(employeeDto.getLastname(), actual.getLastname());
        Assert.assertEquals(employeeDto.getLdapUsername(), actual.getLdapUsername());
        Assert.assertEquals(employeeDto.getTitle(), actual.getTitle());
        Assert.assertEquals(employeeDto.getGrade(), actual.getGrade());
    }

    @Test
    public void testList() {
        final List<Employee> employees = new ArrayList<>();
        final Employee employee = Employee.Builder.newInstance("firstname", "lastname", "ldapUsername", "title", "grade").build();
        employees.add(employee);

        mockery.checking(new Expectations() {
            {
                oneOf(employeeService).getAll();
                will(returnValue(employees));
            }
        });

        List<EmployeeDto> list = target.list();

        mockery.assertIsSatisfied();
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(employee.getFirstname(), list.get(0).getFirstname());
        Assert.assertEquals(employee.getLastname(), list.get(0).getLastname());
    }

    @Test
    public void testGetById() {
        final Employee employee = Employee.Builder.newInstance("firstname", "lastname", "ldapUsername", "title", "grade").build();
        final EmployeeDto employeeDto = new EmployeeDto(employee);
        final Long id = 1L;

        mockery.checking(new Expectations() {
            {
                oneOf(employeeService).getById(id);
                will(returnValue(employee));
            }
        });

        final EmployeeDto actual = target.getById(id);

        mockery.assertIsSatisfied();
        Assert.assertEquals(employeeDto.getFirstname(), actual.getFirstname());
        Assert.assertEquals(employeeDto.getLastname(), actual.getLastname());
    }

    @Test(expected = NotFoundException.class)
    public void testGetDepartmentOfWhenEntityNotFoundExceptionThrown() {
        final Long id = 1L;

        mockery.checking(new Expectations() {
            {
                oneOf(employeeService).getDepartmentOf(id);
                will(throwException(new EntityNotFoundException()));
            }
        });

        target.getDepartmentOf(id);

        mockery.assertIsSatisfied();
    }

    @Test
    public void testGetDepartmentOf() {
        final Long id = 1L;
        final Department department = Department.Builder.newInstance("test", 1).build();
        final DepartmentDto departmentDto = new DepartmentDto(department);

        mockery.checking(new Expectations() {
            {
                oneOf(employeeService).getDepartmentOf(id);
                will(returnValue(department));
            }
        });

        DepartmentDto actual = target.getDepartmentOf(id);

        mockery.assertIsSatisfied();
        Assert.assertEquals(departmentDto.getName(), actual.getName());
        Assert.assertEquals(departmentDto.getOpenPositions(), actual.getOpenPositions());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveByIdWhenEntityNotFoundExceptionThrow() {
        final Long id = 1L;

        mockery.checking(new Expectations() {
            {
                oneOf(employeeService).removeById(id);
                will(throwException(new EntityNotFoundException()));
            }
        });

        target.removeById(id);

        mockery.assertIsSatisfied();
    }

    @Test(expected = PreconditionFailedException.class)
    public void testRemoveByIdWhenEntityExistsExceptionThrow() {
        final Long id = 1L;

        mockery.checking(new Expectations() {
            {
                oneOf(employeeService).removeById(id);
                will(throwException(new EntityExistsException()));
            }
        });

        target.removeById(id);

        mockery.assertIsSatisfied();
    }

    @Test
    public void testRemoveById() {
        final Long id = 1L;

        mockery.checking(new Expectations() {
            {
                oneOf(employeeService).removeById(id);
            }
        });

        target.removeById(id);

        mockery.assertIsSatisfied();
    }
}