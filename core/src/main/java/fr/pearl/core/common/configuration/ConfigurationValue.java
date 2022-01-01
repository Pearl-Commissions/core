package fr.pearl.core.common.configuration;

import java.lang.reflect.Field;
import java.util.Optional;

public class ConfigurationValue {

    private final Field field;
    private Object value;
    private final String[] comments;

    public ConfigurationValue(Field field, Object value, String... comments) {
        this.field = field;
        this.value = value;
        this.comments = comments;
    }

    public Optional<Field> getField() {
        return Optional.of(this.field);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String[] getComments() {
        return comments;
    }
}
