package io.appform.jsonrules;

import com.alibaba.fastjson2.JSONObject;

public interface ExpressionVisitor<T> {
    T visit(io.appform.jsonrules.expressions.composite.AndExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.composite.OrExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.composite.NotExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.meta.ExistsExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.meta.NotExistsExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.numeric.GreaterThanExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.numeric.GreaterThanEqualsExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.numeric.LessThanExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.numeric.LessThanEqualsExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.numeric.BetweenExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.equality.EqualsExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.equality.NotEqualsExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.string.EmptyExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.string.NotEmptyExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.string.StartsWithExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.string.EndsWithExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.string.MatchesExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.array.InExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.array.NotInExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.array.ContainsAnyExpression expression, JSONObject node);

    T visit(io.appform.jsonrules.expressions.array.ContainsAllExpression expression, JSONObject node);
}
