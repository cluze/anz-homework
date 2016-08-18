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
import anz.core.application.DepartmentService;
import anz.core.application.shared.InvalidEntityException;
import anz.core.domain.models.Department;
import anz.core.domain.models.Employee;

public class DepartmentControllerTest {
    private Mockery mockery = new Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    private DepartmentService departmentService = mockery.mock(DepartmentService.class);

    private DepartmentController target = new DepartmentController(departmentService);

    @Test(expected = BadRequestException.class)
    public void testCreateWhenDepartmentIdIsNotNull() {
        final DepartmentDto departmentDto = mockery.mock(DepartmentDto.class);

        mockery.checking(new Expectations() {
            {
                oneOf(departmentDto).getId();
                will(returnValue(1L));
            }
        });

        target.create(departmentDto);

        mockery.assertIsSatisfied();
    }

    @Test(expected = BadRequestException.class)
    public void testCreateWhenNullPointerExceptionThrown() {
        final Department department = Department.Builder.newInstance("test", 1).build();
        final DepartmentDto departmentDto = new DepartmentDto(department);

        mockery.checking(new Expectations() {
            {
                oneOf(departmentService).create(with(any(Department.class)));
                will(throwException(new NullPointerException()));
            }
        });

        target.create(departmentDto);

        mockery.assertIsSatisfied();
    }

    @Test(expected = BadRequestException.class)
    public void testCreateWhenIllegalArgumentExceptionThrown() {
        final Department department = Department.Builder.newInstance("test", 1).build();
        final DepartmentDto departmentDto = new DepartmentDto(department);

        mockery.checking(new Expectations() {
            {
                oneOf(departmentService).create(with(any(Department.class)));
                will(throwException(new IllegalArgumentException()));
            }
        });

        target.create(departmentDto);

        mockery.assertIsSatisfied();
    }

    @Test(expected = NotAcceptableException.class)
    public void testCreateWhenEntityExistsExceptionThrown() {
        final Department department = Department.Builder.newInstance("test", 1).build();
        final DepartmentDto departmentDto = new DepartmentDto(department);

        mockery.checking(new Expectations() {
            {
                oneOf(departmentService).create(with(any(Department.class)));
                will(throwException(new EntityExistsException()));
            }
        });

        target.create(departmentDto);

        mockery.assertIsSatisfied();
    }

    @Test(expected = PreconditionFailedException.class)
    public void testCreateWhenEntityNotFoundExceptionThrown() {
        final Department department = Department.Builder.newInstance("test", 1).build();
        final DepartmentDto departmentDto = new DepartmentDto(department);

        mockery.checking(new Expectations() {
            {
                oneOf(departmentService).create(with(any(Department.class)));
                will(throwException(new EntityNotFoundException()));
            }
        });

        target.create(departmentDto);

        mockery.assertIsSatisfied();
    }

    @Test
    public void testCreate() {
        final Department department = Department.Builder.newInstance("test", 1).build();
        final DepartmentDto departmentDto = new DepartmentDto(department);

        mockery.checking(new Expectations() {
            {
                oneOf(departmentService).create(with(any(Department.class)));
                will(returnValue(department));
            }
        });

        DepartmentDto actual = target.create(departmentDto);

        mockery.assertIsSatisfied();
        Assert.assertEquals(departmentDto.getName(), actual.getName());
        Assert.assertEquals(departmentDto.getOpenPositions(), actual.getOpenPositions());
    }

    @Test
    public void testList() {
        final List<Department> departments = new ArrayList<>();
        final Department department = Department.Builder.newInstance("test", 1).build();
        departments.add(department);

        mockery.checking(new Expectations() {
            {
                oneOf(departmentService).getAll();
                will(returnValue(departments));
            }
        });

        List<DepartmentDto> list = target.list();

        mockery.assertIsSatisfied();
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(department.getName(), list.get(0).getName());
        Assert.assertEquals(department.getOpenPositions(), list.get(0).getOpenPositions());
    }

    @Test
    public void testGetById() {
        final Department department = Department.Builder.newInstance("test", 1).build();
        final DepartmentDto expect = new DepartmentDto(department);
        final Long id = 1L;

        mockery.checking(new Expectations() {
            {
                oneOf(departmentService).getById(id);
                will(returnValue(department));
            }
        });

        final DepartmentDto actual = target.getById(id);

        mockery.assertIsSatisfied();
        Assert.assertEquals(expect.getName(), actual.getName());
        Assert.assertEquals(expect.getOpenPositions(), actual.getOpenPositions());
    }

    @Test
    public void testGetParentOf() {
        final Department department = Department.Builder.newInstance("test", 1).build();
        final DepartmentDto expect = new DepartmentDto(department);
        final Long id = 1L;

        mockery.checking(new Expectations() {
            {
                oneOf(departmentService).getParentById(id);
                will(returnValue(department));
            }
        });

        final DepartmentDto actual = target.getParentOf(id);

        mockery.assertIsSatisfied();
        Assert.assertEquals(expect.getName(), actual.getName());
        Assert.assertEquals(expect.getOpenPositions(), actual.getOpenPositions());
    }

    @Test
    public void testGetChildrenOf() {
        final List<Department> departments = new ArrayList<>();
        Department department = Department.Builder.newInstance("test", 1).build();
        departments.add(department);
        final Long id = 1L;

        mockery.checking(new Expectations() {
            {
                oneOf(departmentService).getSubDepartmentsById(id);
                will(returnValue(departments));
            }
        });

        final List<DepartmentDto> actual = target.getChildrenOf(id);

        mockery.assertIsSatisfied();
        Assert.assertEquals(1, actual.size());
        Assert.assertEquals(department.getName(), actual.get(0).getName());
        Assert.assertEquals(department.getOpenPositions(), actual.get(0).getOpenPositions());
    }

    @Test
    public void testGetEmployeesOf() {
        final Long id = 1L;
        final List<Employee> employees = new ArrayList<>();
        Employee employee = Employee.Builder.newInstance("firstname", "lastname", "ldapUsername", "title", "grade").build();
        employees.add(employee);

        mockery.checking(new Expectations() {
            {
                oneOf(departmentService).getEmployees(id);
                will(returnValue(employees));
            }
        });

        final List<EmployeeDto> actual = target.getEmployeesOf(id);

        mockery.assertIsSatisfied();
        Assert.assertEquals(employees.size(), actual.size());
    }

    @Test(expected = BadRequestException.class)
    public void testUpdateWhenDepartmentIdIsNotNull() {
        final DepartmentDto departmentDto = mockery.mock(DepartmentDto.class);

        mockery.checking(new Expectations() {
            {
                oneOf(departmentDto).getId();
                will(returnValue(null));
            }
        });

        target.update(departmentDto);

        mockery.assertIsSatisfied();
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateWhenDepartmentNotExist() {
        final DepartmentDto departmentDto = mockery.mock(DepartmentDto.class);
        final Long id = 1L;

        mockery.checking(new Expectations() {
            {
                oneOf(departmentDto).getId();
                will(returnValue(id));

                oneOf(departmentService).getById(id);
                will(returnValue(null));
            }
        });

        target.update(departmentDto);

        mockery.assertIsSatisfied();
    }

    @Test(expected = NotAcceptableException.class)
    public void testUpdateWhenInvalidEntityExceptionThrown() {
        final DepartmentDto departmentDto = mockery.mock(DepartmentDto.class);
        final Long id = 1L;
        final Department department = mockery.mock(Department.class);
        final int openPositions = 1;
        final String location = "test";
        final Long managerId = 1L;
        final Long parentDepartmentId = 2L;

        mockery.checking(new Expectations() {
            {
                oneOf(departmentDto).getId();
                will(returnValue(id));

                oneOf(departmentService).getById(id);
                will(returnValue(department));

                oneOf(departmentDto).getOpenPositions();
                will(returnValue(openPositions));

                oneOf(departmentDto).getLocation();
                will(returnValue(location));

                oneOf(departmentDto).getManagerId();
                will(returnValue(managerId));

                oneOf(departmentDto).getParentDepartmentId();
                will(returnValue(parentDepartmentId));

                oneOf(department).update(openPositions, location, managerId, parentDepartmentId);

                oneOf(departmentService).update(department);
                will(throwException(new InvalidEntityException("")));
            }
        });

        target.update(departmentDto);

        mockery.assertIsSatisfied();
    }

    @Test(expected = NotAcceptableException.class)
    public void testUpdateWhenEntityExistsExceptionThrown() {
        final DepartmentDto departmentDto = mockery.mock(DepartmentDto.class);
        final Long id = 1L;
        final Department department = mockery.mock(Department.class);
        final int openPositions = 1;
        final String location = "test";
        final Long managerId = 1L;
        final Long parentDepartmentId = 2L;

        mockery.checking(new Expectations() {
            {
                oneOf(departmentDto).getId();
                will(returnValue(id));

                oneOf(departmentService).getById(id);
                will(returnValue(department));

                oneOf(departmentDto).getOpenPositions();
                will(returnValue(openPositions));

                oneOf(departmentDto).getLocation();
                will(returnValue(location));

                oneOf(departmentDto).getManagerId();
                will(returnValue(managerId));

                oneOf(departmentDto).getParentDepartmentId();
                will(returnValue(parentDepartmentId));

                oneOf(department).update(openPositions, location, managerId, parentDepartmentId);

                oneOf(departmentService).update(department);
                will(throwException(new EntityExistsException()));
            }
        });

        target.update(departmentDto);

        mockery.assertIsSatisfied();
    }

    @Test(expected = PreconditionFailedException.class)
    public void testUpdateWhenEntityNotFoundExceptionThrown() {
        final DepartmentDto departmentDto = mockery.mock(DepartmentDto.class);
        final Long id = 1L;
        final Department department = mockery.mock(Department.class);
        final int openPositions = 1;
        final String location = "test";
        final Long managerId = 1L;
        final Long parentDepartmentId = 2L;

        mockery.checking(new Expectations() {
            {
                oneOf(departmentDto).getId();
                will(returnValue(id));

                oneOf(departmentService).getById(id);
                will(returnValue(department));

                oneOf(departmentDto).getOpenPositions();
                will(returnValue(openPositions));

                oneOf(departmentDto).getLocation();
                will(returnValue(location));

                oneOf(departmentDto).getManagerId();
                will(returnValue(managerId));

                oneOf(departmentDto).getParentDepartmentId();
                will(returnValue(parentDepartmentId));

                oneOf(department).update(openPositions, location, managerId, parentDepartmentId);

                oneOf(departmentService).update(department);
                will(throwException(new EntityNotFoundException()));
            }
        });

        target.update(departmentDto);

        mockery.assertIsSatisfied();
    }

    @Test
    public void testUpdate() {
        final DepartmentDto departmentDto = mockery.mock(DepartmentDto.class);
        final Long id = 1L;
        final Department department = Department.Builder.newInstance("test", 0).build();
        final int openPositions = 1;
        final String location = "test";
        final Long managerId = 1L;
        final Long parentDepartmentId = 2L;

        mockery.checking(new Expectations() {
            {
                oneOf(departmentDto).getId();
                will(returnValue(id));

                oneOf(departmentService).getById(id);
                will(returnValue(department));

                oneOf(departmentDto).getOpenPositions();
                will(returnValue(openPositions));

                oneOf(departmentDto).getLocation();
                will(returnValue(location));

                oneOf(departmentDto).getManagerId();
                will(returnValue(managerId));

                oneOf(departmentDto).getParentDepartmentId();
                will(returnValue(parentDepartmentId));

                oneOf(departmentService).update(department);
                will(returnValue(department));
            }
        });

        DepartmentDto actual = target.update(departmentDto);

        mockery.assertIsSatisfied();
        Assert.assertEquals(department.getName(), actual.getName());
        Assert.assertEquals(openPositions, actual.getOpenPositions());
        Assert.assertEquals(location, actual.getLocation());
        Assert.assertEquals(managerId, actual.getManagerId());
        Assert.assertEquals(parentDepartmentId, actual.getParentDepartmentId());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveByIdWhenEntityNotFoundExceptionThrow() {
        final Long id = 1L;

        mockery.checking(new Expectations() {
            {
                oneOf(departmentService).removeById(id);
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
                oneOf(departmentService).removeById(id);
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
                oneOf(departmentService).removeById(id);
            }
        });

        target.removeById(id);

        mockery.assertIsSatisfied();
    }
}