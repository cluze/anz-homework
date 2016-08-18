package anz.core.domain.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;

@Entity
@Table(name = "Department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String location;

    private Long managerId;

    private int openPositions;

    private Long parentDepartmentId;

    Department() {
    }

    private Department(final Builder builder) {
        id = builder.id;
        name = builder.name;
        location = builder.location;
        managerId = builder.managerId;
        openPositions = builder.openPositions;
        parentDepartmentId = builder.parentDepartmentId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Long getManagerId() {
        return managerId;
    }

    public int getOpenPositions() {
        return openPositions;
    }

    public Long getParentDepartmentId() {
        return parentDepartmentId;
    }

    public void update(final int openPositions, final String location, final Long managerId, final Long parentDepartmentId) {
        this.openPositions = openPositions;
        this.location = location;
        this.managerId = managerId;
        this.parentDepartmentId = parentDepartmentId;
    }

    public static class Builder {
        private final Long id;

        private final String name;

        private String location;

        private Long managerId;

        private final int openPositions;

        private Long parentDepartmentId;

        private Builder(final Long id, final String name, final int openPositions) {
            this.id = id;
            this.name = name;
            this.openPositions = openPositions;
        }

        public static Builder newInstance(final String name, final int openPositions) {
            Validate.notEmpty(name, "name cannot be empty");
            return new Builder(null, name, openPositions);
        }

        public static Builder newInstance(final Long id, final String name, final int openPositions) {
            Validate.notNull(id, "id cannot be null");
            Validate.notEmpty(name, "name cannot be empty");
            return new Builder(id, name, openPositions);
        }

        public Builder location(final String location) {
            this.location = location;
            return this;
        }

        public Builder managerId(final Long managerId) {
            this.managerId = managerId;
            return this;
        }

        public Builder parentDepartment(final Long parentDepartmentId) {
            this.parentDepartmentId = parentDepartmentId;
            return this;
        }

        public Department build() {
            return new Department(this);
        }
    }
}