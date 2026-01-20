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

package io.appform.jsonrules.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import io.appform.jsonrules.Expression;
import io.appform.jsonrules.ExpressionEvaluationContext;
import lombok.EqualsAndHashCode;

import java.util.Collections;

/**
 * A basic rule
 */
@EqualsAndHashCode
public class Rule {
    private final Expression expression;

    public Rule(Expression expression) {
        this.expression = expression;
    }

    public static Rule create(final String json) throws Exception {
        Expression expression = JSON.parseObject(json, Expression.class,
                JSONReader.Feature.UseDoubleForDecimals);
        return new Rule(expression);
    }

    public boolean matches(Object node) {
        return expression.evaluate(
                ExpressionEvaluationContext.builder()
                        .node(node)
                        .options(Collections.emptyMap())
                        .build());
    }

    public boolean matches(JSONObject node) {
        return matches((Object) node);
    }

    public String representation() throws Exception {
        return JSON.toJSONString(expression, JSONWriter.Feature.IgnoreEmpty);
    }
}
