package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> cl;

    public EntityClassMetaDataImpl(Class<T> cl) {
        this.cl = cl;
    }

    @Override
    public String getName() {
        return cl.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return cl.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Default constructor not found");
        }
    }

    @Override
    public Field getIdField() {
        for (var field : cl.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }

        return null;
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(cl.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        List<Field> fieldsWithoutId = new ArrayList<>();
        for (var field : cl.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Id.class)) {
                fieldsWithoutId.add(field);
            }
        }

        return fieldsWithoutId;
    }
}
