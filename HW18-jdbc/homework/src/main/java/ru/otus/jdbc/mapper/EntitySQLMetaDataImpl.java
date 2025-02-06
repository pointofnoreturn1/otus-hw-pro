package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "SELECT * FROM " + getTableName();
    }

    @Override
    public String getSelectByIdSql() {
        return "SELECT * FROM "
                + getTableName()
                + " WHERE "
                + entityClassMetaData.getIdField().getName() +
                " = ?";
    }

    @Override
    public String getInsertSql() {
        return "INSERT INTO "
                + getTableName() +
                " (" +
                entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.joining(",")) +
                ") VALUES (" +
                Stream.generate(() -> "?").limit(entityClassMetaData.getFieldsWithoutId().size()).collect(Collectors.joining(",")) +
                ")";
    }

    @Override
    public String getUpdateSql() {
        return "UPDATE " +
                getTableName() +
                " SET " +
                entityClassMetaData.getFieldsWithoutId().stream().map(it -> it.getName() + " = ?").collect(Collectors.joining(",")) +
                " WHERE " +
                entityClassMetaData.getIdField().getName() +
                " = ?";
    }

    private String getTableName() {
        return entityClassMetaData.getName().toLowerCase();
    }
}
