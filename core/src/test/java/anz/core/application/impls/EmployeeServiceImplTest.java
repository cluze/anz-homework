package anz.core.application.impls;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Assert;
import org.junit.Test;

import anz.core.domain.models.Department;
import anz.core.domain.models.Employee;
import anz.core.domain.models.Gender;
import anz.core.repositories.DepartmentRepository;
import anz.core.repositories.EmployeeRepository;

public class EmployeeServiceImplTest {
    private Mockery mockery = new Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    private DepartmentRepository departmentRepository = mockery.mock(DepartmentRepository.class);

    private EmployeeRepository employeeRepository = mockery.mock(EmployeeRepository.class);

    private EmployeeServiceImpl target = new EmployeeServiceImpl(departmentRepository, employeeRepository);

    @Test(expected = EntityNotFoundException.class)
    public void testCreateWhenDepartmentNotExist() {
        final Long departmentId = 1L;
        Employee employee = Employee.Builder.newInstance("firstname", "lastname", "ldapUsername", "title", "grade").departmentId(departmentId).build();

        mockery.checking(new Expectations() {
            {
                oneOf(departmentRepository).findOne(departmentId);
                will(returnValue(null));
            }
        });

        target.create(employee);

        mockery.assertIsSatisfied();
    }

    @Test(expected = EntityExistsException.class)
    public void testCreateWhenLdapUsernameExist() {
        final String ldapUsername = "name";
        Employee employee = Employee.Builder.newInstance("firstname", "lastname", ldapUsername, "title", "grade").build();
        final Employee exist = mockery.mock(Employee.class);

        mockery.checking(new Expectations() {
            {
                oneOf(employeeRepository).findByLdapUsername(ldapUsername);
                will(returnValue(exist));
            }
        });

        target.create(employee);

        mockery.assertIsSatisfied();
    }

    @Test
    public void testCreate() {
        final String ldapUsername = "name";
        final Employee employee = Employee.Builder.newInstance("firstname", "lastname", ldapUsername, "title", "grade").build();
        final Employee exist = mockery.mock(Employee.class);

        mockery.checking(new Expectations() {
            {
                oneOf(employeeRepository).findByLdapUsername(ldapUsername);
                will(returnValue(null));

                oneOf(employeeRepository).save(employee);
                will(returnValue(exist));
            }
        });

        Employee actual = target.create(employee);

        mockery.assertIsSatisfied();

        Assert.assertNotNull(actual);
    }

    @Test
    public void testGetAll() {
        final List<Employee> employees = new ArrayList<>();
        mockery.checking(new Expectations() {
            {
                oneOf(employeeRepository).findAll();
                will(returnValue(employees));
            }
        });

        List<Employee> actual = target.getAll();

        mockery.assertIsSatisfied();
        Assert.assertEquals(employees.size(), actual.size());
    }

    @Test
    public void testGetById() {
        final Long id = 1L;
        final Employee employee = Employee.Builder.newInstance(id, "firstname", "lastname", "ldapUsername", "title", "grade").build();
        mockery.checking(new Expectations() {
            {
                oneOf(employeeRepository).findOne(id);
                will(returnValue(employee));
            }
        });

        Employee actual = target.getById(id);

        mockery.assertIsSatisfied();
        Assert.assertEquals(employee.getId(), actual.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetDepartmentOfWhenEmployeeNotExist() {
        final Long id = 1L;
        mockery.checking(new Expectations() {
            {
                oneOf(employeeRepository).findOne(id);
                will(returnValue(null));
            }
        });

        Department actual = target.getDepartmentOf(id);

        mockery.assertIsSatisfied();
        Assert.assertNull(actual);
    }

    @Test
    public void testGetDepartmentOfWhenNoDepartmentSet() {
        final Long id = 1L;
        final Employee employee = Employee.Builder.newInstance(id, "firstname", "lastname", "ldapUsername", "title", "grade").build();
        mockery.checking(new Expectations() {
            {
                oneOf(employeeRepository).findOne(id);
                will(returnValue(employee));
            }
        });

        Department actual = target.getDepartmentOf(id);

        mockery.assertIsSatisfied();
        Assert.assertNull(actual);
    }

    @Test
    public void testGetDepartment() {
        final Long id = 1L;
        final Long departmentId = 2L;
        final Employee employee = Employee.Builder.newInstance(id, "firstname", "lastname", "ldapUsername", "title", "grade").departmentId(departmentId).build();
        final Department department = Department.Builder.newInstance(departmentId, "name", 1).build();

        mockery.checking(new Expectations() {
            {
                oneOf(employeeRepository).findOne(id);
                will(returnValue(employee));

                oneOf(departmentRepository).findOne(departmentId);
                will(returnValue(department));
            }
        });

        Department actual = target.getDepartmentOf(id);

        mockery.assertIsSatisfied();
        Assert.assertEquals(department.getName(), actual.getName());
        Assert.assertEquals(department.getOpenPositions(), actual.getOpenPositions());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testRemoveByIdWhenEmployeeNotExist() {
        final Long id = 3L;

        mockery.checking(new Expectations() {
            {
                oneOf(employeeRepository).findOne(id);
                will(returnValue(null));
            }
        });

        target.removeById(id);

        mockery.assertIsSatisfied();
    }

    @Test(expected = EntityExistsException.class)
    public void testRemoveByIdWhenIsDepartmentManager() {
        final Long id = 3L;
        final Employee employee = Employee.Builder.newInstance(id, "firstname", "lastname", "ldapUsername", "title", "grade").build();
        final List<Department> departments = new ArrayList<>();
        final Department department = Department.Builder.newInstance(id, "test3", 1).location("aa").build();
        departments.add(department);

        mockery.checking(new Expectations() {
            {
                oneOf(employeeRepository).findOne(id);
                will(returnValue(employee));

                oneOf(departmentRepository).findByManagerId(id);
                will(returnValue(departments));
            }
        });

        target.removeById(id);

        mockery.assertIsSatisfied();
    }

    @Test
    public void testRemove() {
        final Long id = 3L;
        final Employee employee = Employee.Builder.newInstance(id, "firstname", "lastname", "ldapUsername", "title", "grade").gender(Gender.Male).birthdate(1235324).build();
        final List<Department> departments = new ArrayList<>();

        mockery.checking(new Expectations() {
            {
                oneOf(employeeRepository).findOne(id);
                will(returnValue(employee));

                oneOf(departmentRepository).findByManagerId(id);
                will(returnValue(departments));

                oneOf(employeeRepository).delete(id);
            }
        });

        target.removeById(id);

        mockery.assertIsSatisfied();
    }
}