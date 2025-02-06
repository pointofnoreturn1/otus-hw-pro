package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {
    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor,
            EntitySQLMetaData entitySQLMetaData,
            EntityClassMetaData<T> entityClassMetaData
    ) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectByIdSql(),
                Collections.singletonList(id),
                rs -> handleResultSet(rs).getFirst()
        );
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectByIdSql(),
                Collections.emptyList(),
                this::handleResultSet
        ).orElse(Collections.emptyList());
    }

    @Override
    public long insert(Connection connection, T obj) {
        return dbExecutor.executeStatement(
                connection,
                entitySQLMetaData.getInsertSql(),
                getInsertValues(obj)
        );
    }

    @Override
    public void update(Connection connection, T obj) {
        dbExecutor.executeStatement(
                connection,
                entitySQLMetaData.getUpdateSql(),
                getUpdateValues(obj)
        );
    }

    private T instantiate() {
        try {
            return entityClassMetaData.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error while instantiating object", e);
        }
    }

    private List<T> handleResultSet(ResultSet rs) {
        List<T> createdObjects = new ArrayList<>();
        try {
            while (rs.next()) {
                var instance = instantiate();
                for (var field : entityClassMetaData.getAllFields()) {
                    var fieldName = field.getName();
                    boolean accessible = field.canAccess(instance);
                    var declaredField = instance.getClass().getDeclaredField(fieldName);
                    declaredField.setAccessible(true);
                    declaredField.set(instance, rs.getObject(fieldName));
                    declaredField.setAccessible(accessible);
                }
                createdObjects.add(instance);
            }
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error while handling data from DB", e);
        }

        return createdObjects;
    }

    private List<Object> getInsertValues(T obj) {
        return entityClassMetaData.getFieldsWithoutId().stream().map(it -> getFieldValue(obj, it)).toList();
    }

    private List<Object> getUpdateValues(T obj) {
        List<Object> updateValues = new ArrayList<>();
        for (var field : entityClassMetaData.getFieldsWithoutId()) {
            updateValues.add(getFieldValue(obj, field));
        }
        updateValues.add(getFieldValue(obj, entityClassMetaData.getIdField()));

        return updateValues;
    }

    private Object getFieldValue(T obj, Field field) {
        try {
            return obj.getClass().getMethod(getter(field.getName())).invoke(obj);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Error while getting field value from object", e);
        }
    }

    private String getter(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}
