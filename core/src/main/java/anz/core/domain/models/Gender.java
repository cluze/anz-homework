package anz.core.domain.models;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

public enum Gender {
                    Male("male"),
                    Female("female");

    private static final Map<String, Gender> STRING_MAP = new HashMap<>();

    private static final Map<String, Gender> VALUE_MAP = new HashMap<>();

    static {
        for (final Gender gender : values()) {
            STRING_MAP.put(gender.toString(), gender);
            VALUE_MAP.put(gender.value.toLowerCase(), gender);
        }
    }

    private final String value;

    private Gender(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return name();
    }

    public String getValue() {
        return value;
    }

    public static Gender fromString(final String name) {
        Validate.notEmpty(name, "name cannot be empty");
        return STRING_MAP.get(name);
    }

    public static Gender fromValue(final String value) {
        if (null == value) {
            return null;
        }
        Validate.notEmpty(value, "value cannot be empty");
        return VALUE_MAP.get(value.toLowerCase());
    }
}