package com.hello.capston.batch.reader.options;

import com.hello.capston.batch.reader.expression.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;


@Slf4j
public abstract class QuerydslNoOffsetOptions<T> {

    protected final String fieldName;
    protected final Expression expression;

    public QuerydslNoOffsetOptions(@NonNull Path field, @NonNull Expression expression) {
        String[] qField = field.toString().split("\\.");
        this.fieldName = qField[qField.length - 1];
        this.expression = expression;
    }

    public String getFieldName() {
        return fieldName;
    }

    public abstract void initKeys(JPAQuery<T> query, int page);

    protected abstract void initFirstId(JPAQuery<T> query);
    protected abstract void initLastId(JPAQuery<T> query);

    public abstract JPAQuery<T> createQuery(JPAQuery<T> query, int page);

    public abstract void resetCurrentId(T item);

    protected Object getFieldValue(T item) {
        try {
            Field field = item.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(item);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("Not Found or Not Access Field = {} {}", fieldName, e);
            throw new IllegalArgumentException("Not Found or Not Access Field");
        }
    }

    public boolean isGroupByQuery(JPAQuery<T> query) {
        return isGroupByQuery(query.toString());
    }

    public boolean isGroupByQuery(String sql) {
        return sql.contains("group by");
    }
}
