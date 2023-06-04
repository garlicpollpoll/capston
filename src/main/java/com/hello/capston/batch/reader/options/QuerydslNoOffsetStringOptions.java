package com.hello.capston.batch.reader.options;

import com.hello.capston.batch.reader.expression.Expression;
import com.hello.capston.batch.reader.expression.WhereExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.NonNull;

public class QuerydslNoOffsetStringOptions<T> extends QuerydslNoOffsetOptions<T> {

    private String currentId;
    private String lastId;

    private final StringPath field;

    public QuerydslNoOffsetStringOptions(@NonNull StringPath field, @NonNull Expression expression) {
        super(field, expression);
        this.field = field;
    }

    public String getCurrentId() {
        return currentId;
    }

    public String getLastId() {
        return lastId;
    }

    @Override
    public void initKeys(JPAQuery<T> query, int page) {
        if (page == 0) {
            initFirstId(query);
            initLastId(query);
        }
    }

    @Override
    protected void initFirstId(JPAQuery<T> query) {
        JPAQuery<T> clone = query.clone();
        boolean isGroupByQuery = isGroupByQuery(clone);

        if (isGroupByQuery) {
            currentId = clone
                    .select(field)
                    .orderBy(expression.isAsc() ? field.asc() : field.desc())
                    .fetchFirst();
        }
        else {
            currentId = clone
                    .select(expression.isAsc() ? field.min() : field.max())
                    .fetchFirst();
        }
    }

    @Override
    protected void initLastId(JPAQuery<T> query) {
        JPAQuery<T> clone = query.clone();
        boolean isGroupByQuery = isGroupByQuery(clone);

        if (isGroupByQuery) {
            lastId = clone
                    .select(field)
                    .orderBy(expression.isAsc() ? field.desc() : field.asc())
                    .fetchFirst();
        }
        else {
            lastId = clone
                    .select(expression.isAsc() ? field.max() : field.min())
                    .fetchFirst();
        }
    }

    @Override
    public JPAQuery<T> createQuery(JPAQuery<T> query, int page) {
        if (currentId == null) {
            return query;
        }

        return query
                .where(whereExpression(page))
                .orderBy(orderExpression());
    }

    private OrderSpecifier<String> orderExpression() {
        return expression.order(field);
    }

    private BooleanExpression whereExpression(int page) {
        return expression.where(field, page, currentId)
                .and(expression.isAsc() ? field.loe(lastId) : field.goe(lastId));
    }

    @Override
    public void resetCurrentId(T item) {

    }


}
