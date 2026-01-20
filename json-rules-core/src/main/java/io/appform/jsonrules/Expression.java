/*
 * Copyright (c) 2016 Santanu Sinha <santanu.sinha@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.appform.jsonrules;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONType;
import io.appform.jsonrules.expressions.array.ContainsAllExpression;
import io.appform.jsonrules.expressions.array.ContainsAnyExpression;
import io.appform.jsonrules.expressions.array.InExpression;
import io.appform.jsonrules.expressions.array.NotInExpression;
import io.appform.jsonrules.expressions.composite.AndExpression;
import io.appform.jsonrules.expressions.composite.NotExpression;
import io.appform.jsonrules.expressions.composite.OrExpression;
import io.appform.jsonrules.expressions.debug.ExpressionDebugger;
import io.appform.jsonrules.expressions.debug.FailureDetail;
import io.appform.jsonrules.expressions.equality.EqualsExpression;
import io.appform.jsonrules.expressions.equality.NotEqualsExpression;
import io.appform.jsonrules.expressions.meta.ExistsExpression;
import io.appform.jsonrules.expressions.meta.NotExistsExpression;
import io.appform.jsonrules.expressions.numeric.*;
import io.appform.jsonrules.expressions.string.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.Map;

/**
 * A base expression
 */
@Data
@EqualsAndHashCode
@ToString
@JSONType(typeKey = "type", seeAlso = {
        EqualsExpression.class,
        NotEqualsExpression.class,
        GreaterThanExpression.class,
        GreaterThanEqualsExpression.class,
        LessThanExpression.class,
        LessThanEqualsExpression.class,
        BetweenExpression.class,
        AndExpression.class,
        OrExpression.class,
        NotExpression.class,
        ExistsExpression.class,
        NotExistsExpression.class,
        EmptyExpression.class,
        NotEmptyExpression.class,
        StartsWithExpression.class,
        EndsWithExpression.class,
        MatchesExpression.class,
        InExpression.class,
        NotInExpression.class,
        ContainsAnyExpression.class,
        ContainsAllExpression.class
})
public abstract class Expression {
    private final ExpressionType type;

    protected Expression(ExpressionType type) {
        this.type = type;
    }

    public boolean evaluate(JSONObject node) {
        return evaluate((Object) node, Collections.emptyMap());
    }

    public boolean evaluate(Object node) {
        return evaluate(node, Collections.emptyMap());
    }

    public boolean evaluate(Object node, Map<OptionKeys, Object> options) {
        if (null == node) {
            // Fail safe check, to replace null with empty node.
            node = new JSONObject();
        }
        return evaluate(ExpressionEvaluationContext.builder()
                .node(node)
                .options(options)
                .build());
    }

    public FailureDetail debug(JSONObject node) {
        return debug((Object) node);
    }

    public FailureDetail debug(Object node) {
        // ExpressionDebugger now operates on fastjson2 JSONObject payloads.
        final JSONObject jsonObject = node instanceof JSONObject ? (JSONObject) node : JSON.parseObject(JSON.toJSONString(node));
        return ExpressionDebugger.builder()
                .expression(this)
                .node(jsonObject)
                .build()
                .debug();
    }

    public abstract boolean evaluate(ExpressionEvaluationContext context);

    public abstract <T> T accept(ExpressionVisitor<T> visitor, JSONObject node);
}
