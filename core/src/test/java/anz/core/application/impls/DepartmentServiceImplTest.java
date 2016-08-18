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

import anz.core.application.shared.InvalidEntityException;
import anz.core.domain.models.Department;
import anz.core.repositories.DepartmentRepository;
import anz.core.repositories.EmployeeRepository;

public class DepartmentServiceImplTest {
    private Mockery mockery = new Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    private DepartmentRepository departmentRepository = mockery.mock(DepartmentRepository.class);

    private EmployeeRepository employeeRepository = mockery.mock(EmployeeRepository.class);

    private DepartmentServiceImpl target = new DepartmentServiceImpl(departmentRepository, employeeRepository);

    @Test(expected = EntityExistsException.class)
    public void testCreateWhenDepartmentNameExist() {
        final String name = "test";
        Department department = Department.Builder.newInstance(name, 1).location("aa").managerId(1L).parentDepartment(2L).build();
        final Department existDepartment = mockery.mock(Department.class);

        mockery.checking(new Expectations() {
            {
                oneOf(departmentRepository).findByNameIgnoreCase(name);
                will(returnValue(existDepartment));
            }
        });

        target.create(department);

        mockery.assertIsSatisfied();
    }

    @Test(expected = EntityNotFoundException.class)
    public void testCreateWhenInvalidManagerIdSet() {
        final String name = "test";
        final Long managerId = 1L;
        Department department = Department.Builder.newInstance(name, 1).location("aa").managerId(managerId).parentDepartment(2L).build();

        mockery.checking(new Expectations() {
            {
                oneOf(departmentRepository).findByNameIgnoreCase(name);
                will(returnValue(null));

                oneOf(employeeRepository).findOne(managerId);
                will(returnValue(null));
            }
        });

        target.create(department);

        mockery.assertIsSatisfied();
    }

    @Test(expected = EntityNotFoundException.class)
    public void testCreateWhenInvalidParentDepartmentIdSet() {
        final String name = "test";
        final Long parentId = 2L;
        Department department = Department.Builder.newInstance(name, 1).location("aa").parentDepartment(parentId).build();

        mockery.checking(new Expectations() {
            {
                oneOf(departmentRepository).findByNameIgnoreCase(name);
                will(returnValue(null));

                oneOf(departmentRepository).findOne(parentId);
                will(returnValue(null));
            }
        });

        target.create(department);

        mockery.assertIsSatisfied();
    }

    @Test
    public void testCreate() {
        final String name = "test";
        final Department department = Department.Builder.newInstance(name, 1).location("aa").build();

        mockery.checking(new Expectations() {
            {
                oneOf(departmentRepository).findByNameIgnoreCase(name);
                will(returnValue(null));

                oneOf(departmentRepository).save(department);
                will(returnValue(department));
            }
        });

        Department actual = target.create(department);

        mockery.assertIsSatisfied();
        Assert.assertEquals(department.getName(), actual.getName());
        Assert.assertEquals(department.getOpenPositions(), actual.getOpenPositions());
    }

    @Test
    public void testGetAll() {
        final List<Department> departments = new ArrayList<>();
        mockery.checking(new Expectations() {
            {
                oneOf(departmentRepository).findAll();
                will(returnValue(departments));
            }
        });

        List<Department> actual = target.getAll();

        mockery.assertIsSatisfied();
        Assert.assertEquals(departments.size(), actual.size());
    }

    @Test
    public void testGetById() {
        final Long id = 1L;
        final Department department = Department.Builder.newInstance(id, "name", 1).build();
        mockery.checking(new Expectations() {
            {
                oneOf(departmentRepository).findOne(id);
                will(returnValue(department));
            }
        });

        Department actual = target.getById(id);

        mockery.assertIsSatisfied();
        Assert.assertEquals(department.getId(), actual.getId());
    }

    @Test
    public void testGetParentByIdWhenDepartmentNotExist() {
        final Long id = 1L;
        mockery.checking(new Expectations() {
            {
                oneOf(departmentRepository).findOne(id);
                will(returnValue(null));
            }
        });

        Department actual = target.getParentById(id);

        mockery.assertIsSatisfied();
        Assert.assertNull(actual);
    }

    @Test
    public void testGetParentByIdWhenParentDepartmentNotExist() {
        final Long id = 1L;
        final Department department = Department.Builder.newInstance(id, "name", 1).build();
        mockery.checking(new Expectations() {
            {
                oneOf(departmentRepository).findOne(id);
                will(returnValue(department));
            }
        });

        Department actual = target.getParentById(id);

        mockery.assertIsSatisfied();
        Assert.assertNull(actual);
    }

    @Test
    public void testGetParentById() {
        final Long id = 1L;
        final Long parentId = 2L;
        final Department department = Department.Builder.newInstance(id, "name", 1).parentDepartment(parentId).build();
        final Department parent = mockery.mock(Department.class);

        mockery.checking(new Expectations() {
            {
                oneOf(departmentRepository).findOne(id);
                will(returnValue(department));

                oneOf(departmentRepository).findOne(parentId);
                will(returnValue(parent));
            }
        });

        Department actual = target.getParentById(id);

        mockery.assertIsSatisfied();
        Assert.assertNotNull(actual);
    }

    @Test
    public void testGetSubDepartmentsById() {
        final long id = 1L;
        final List<Department> departments = new ArrayList<>();
        mockery.checking(new Expectations() {
            {
                oneOf(departmentRepository).findByParentDepartmentId(id);
                will(returnValue(departments));
            }
        });

        List<Department> actual = target.getSubDepartmentsById(id);

        mockery.assertIsSatisfied();
        Assert.assertEquals(departments.size(), actual.size());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateWhenInvalidManagerIdSet() {
        final Long managerId = 1L;
        Department department = Department.Builder.newInstance("test", 1).location("aa").managerId(managerId).parentDepartment(2L).build();

        mockery.checking(new Expectations() {
            {
                oneOf(employeeRepository).findOne(managerId);
                will(returnValue(null));
            }
        });

        target.update(department);

        mockery.assertIsSatisfied();
    }

    @Test(expected = InvalidEntityException.class)
    public void testUpdateWhenParentIdIsSelfId() {
        final Long id = 1L;
        final Long parentId = 1L;
        Department department = Department.Builder.newInstance(id, "test", 1).location("aa").parentDepartment(parentId).build();

        target.update(department);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateWhenParentNotExist() {
        final Long id = 1L;
        final Long parentId = 2L;
        Department department = Department.Builder.newInstance(id, "test", 1).location("aa").parentDepartment(parentId).build();

        mockery.checking(new Expectations() {
            {
                oneOf(departmentRepository).findOne(parentId);
                will(returnValue(null));
            }
        });

        target.update(department);

        mockery.assertIsSatisfied();
    }

    @Test(expected = InvalidEntityException.class)
    public void testUpdateWhenParentIsChild() {
        final Long parentId = 3L;
        Department department1 = Department.Builder.newInstance(1L, "test", 1).location("aa").parentDepartment(parentId).build();
        final Department department2 = Department.Builder.newInstance(2L, "test2", 1).location("aa").parentDepartment(1L).build();
        final Department department3 = Department.Builder.newInstance(3L, "test3", 1).location("aa").parentDepartment(2L).build();

        mockery.checking(new Expectations() {
            {
                oneOf(departmentRepository).findOne(parentId);
                will(returnValue(department3));

                oneOf(departmentRepository).findOne(parentId);
                will(returnValue(department3));

                oneOf(departmentRepository).findOne(department3.getParentDepartmentId());
                will(returnValue(department2));
            }
        });

        target.update(department1);

        mockery.assertIsSatisfied();
    }

    @Test
    public void testUpdate() {
        final Long parentId = 3L;
        final Department department1 = Department.Builder.newInstance(1L, "test", 1).location("aa").parentDepartment(parentId).build();
        final Department department2 = Department.Builder.newInstance(parentId, "test3", 1).location("aa").build();

        mockery.checking(new Expectations() {
            {
                oneOf(departmentRepository).findOne(parentId);
                will(returnValue(department2));

                oneOf(departmentRepository).findOne(parentId);
                will(returnValue(department2));

                oneOf(departmentRepository).save(department1);
                will(returnValue(department1));
            }
        });

        Department actual = target.update(department1);

        mockery.assertIsSatisfied();

        Assert.assertEquals(department2.getId(), actual.getParentDepartmentId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testRemoveByIdWhenDepartmentNotExist() {
        final Long id = 3L;

        mockery.checking(new Expectations() {
            {
                oneOf(departmentRepository).findOne(id);
                will(returnValue(null));
            }
        });

        target.removeById(id);

        mockery.assertIsSatisfied();
    }

    @Test(expected = EntityExistsException.class)
    public void testRemoveByIdWhenDepartmentContainsSubDepartments() {
        final Long id = 3L;
        final Department department = Department.Builder.newInstance(id, "test3", 1).location("aa").build();
        final List<Department> children = new ArrayList<>();
        final Department child = Department.Builder.newInstance(2L, "test3", 1).location("aa").build();
        children.add(child);

        mockery.checking(new Expectations() {
            {
                oneOf(departmentRepository).findOne(id);
                will(returnValue(department));

                oneOf(departmentRepository).findByParentDepartmentId(id);
                will(returnValue(children));
            }
        });

        target.removeById(id);

        mockery.assertIsSatisfied();
    }

    @Test
    public void testRemoveById() {
        final Long id = 3L;
        final Department department = Department.Builder.newInstance(id, "test3", 1).location("aa").build();
        final List<Department> children = new ArrayList<>();

        mockery.checking(new Expectations() {
            {
                oneOf(departmentRepository).findOne(id);
                will(returnValue(department));

                oneOf(departmentRepository).findByParentDepartmentId(id);
                will(returnValue(children));

                oneOf(departmentRepository).delete(id);
            }
        });

        target.removeById(id);

        mockery.assertIsSatisfied();
    }
}