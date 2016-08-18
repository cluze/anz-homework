package anz.core.domain.models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;

@Entity
@Table(name = "Employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;

    private String lastname;

    private String ldapUsername;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private long birthdate;

    private String title;

    private String grade;

    private Long departmentId;

    Employee() {
    }

    private Employee(final Builder builder) {
        id = builder.id;
        firstname = builder.firstname;
        lastname = builder.lastname;
        ldapUsername = builder.ldapUsername;
        gender = builder.gender;
        birthdate = builder.birthdate;
        title = builder.title;
        grade = builder.grade;
        departmentId = builder.departmentId;
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getLdapUsername() {
        return ldapUsername;
    }

    public Gender getGender() {
        return gender;
    }

    public long getBirthdate() {
        return birthdate;
    }

    public String getTitle() {
        return title;
    }

    public String getGrade() {
        return grade;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public static class Builder {
        private final Long id;

        private final String firstname;

        private final String lastname;

        private final String ldapUsername;

        private Gender gender;

        private long birthdate;

        private final String title;

        private final String grade;

        private Long departmentId;

        private Builder(final Long id, final String firstname, final String lastname, final String ldapUsername, final String title, final String grade) {
            this.id = id;
            this.firstname = firstname;
            this.lastname = lastname;
            this.ldapUsername = ldapUsername;
            this.title = title;
            this.grade = grade;
        }

        public static Builder newInstance(final String firstname, final String lastname, final String ldapUsername, final String title, final String grade) {
            Validate.notEmpty(firstname, "firstname cannot be empty");
            Validate.notEmpty(lastname, "lastname cannot be empty");
            Validate.notEmpty(ldapUsername, "ldapUsername cannot be empty");
            Validate.notEmpty(title, "title cannot be empty");
            Validate.notEmpty(grade, "grade cannot be empty");
            return new Builder(null, firstname, lastname, ldapUsername, title, grade);
        }

        public static Builder newInstance(final Long id, final String firstname, final String lastname, final String ldapUsername, final String title, final String grade) {
            Validate.notNull(id, "id cannot be null");
            Validate.notEmpty(firstname, "firstname cannot be empty");
            Validate.notEmpty(lastname, "lastname cannot be empty");
            Validate.notEmpty(ldapUsername, "ldapUsername cannot be empty");
            Validate.notEmpty(title, "title cannot be empty");
            Validate.notEmpty(grade, "grade cannot be empty");
            return new Builder(id, firstname, lastname, ldapUsername, title, grade);
        }

        public Builder gender(final Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder birthdate(final long birthdate) {
            this.birthdate = birthdate;
            return this;
        }

        public Builder departmentId(final Long departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }
}