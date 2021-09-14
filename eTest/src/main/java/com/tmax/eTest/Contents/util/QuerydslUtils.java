package com.tmax.eTest.Contents.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;

import org.springframework.stereotype.Component;

@Component(value = "QuerydslUtils")
public class QuerydslUtils {
  public OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
    Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);
    return new OrderSpecifier(order, fieldPath);
  }
}
