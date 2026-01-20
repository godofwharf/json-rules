/**
 * Copyright (c) 2017 Mohammed Irfanulla S <mohammed.irfanulla.s1@gmail.com>
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

package io.appform.jsonrules.expressions.array;

import com.alibaba.fastjson2.JSONArray;
import io.appform.jsonrules.ExpressionEvaluationContext;
import io.appform.jsonrules.ExpressionType;
import io.appform.jsonrules.expressions.JsonPathBasedExpression;
import io.appform.jsonrules.expressions.preoperation.PreOperation;
import io.appform.jsonrules.utils.ComparisonUtils;
import io.appform.jsonrules.utils.JsonPathUtils;
import io.appform.jsonrules.utils.JsonUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.ToString;

import java.util.Set;


/**
 * All collection operable expressions
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class CollectionJsonPathBasedExpression extends JsonPathBasedExpression {
    private Set<Object> values;
    private boolean extractValues;
    private String valuesPath;

    protected CollectionJsonPathBasedExpression(ExpressionType type) {
        super(type);
    }

    protected CollectionJsonPathBasedExpression(ExpressionType type,
                                                String path,
                                                @Singular Set<Object> values,
                                                boolean extractValues,
                                                String valuesPath,
                                                boolean defaultResult,
                                                PreOperation<?> preoperation) {
        super(type, path, defaultResult, preoperation);
        this.values = values;
        this.extractValues = extractValues;
        this.valuesPath = valuesPath;
    }

    public void setValues(final Set<Object> values) {
        this.values = values;
    }

    @Override
    protected final boolean evaluate(ExpressionEvaluationContext context, String path, Object evaluatedNode) {
        if (extractValues) {
            Object raw = JsonPathUtils.read(ComparisonUtils.SUPPRESS_EXCEPTION_CONFIG, context.getNode(), String.valueOf(valuesPath));
            if (raw == null || !(raw instanceof JSONArray)) {
                return false;
            }
            Set<Object> pathValues = JsonUtils.convertToSet((JSONArray) raw);
            return evaluate(evaluatedNode, pathValues);
        }

        if (null == values || values.isEmpty()) {
            return false;
        }
        return evaluate(evaluatedNode, values);
    }

    protected abstract boolean evaluate(Object evaluatedNode, Set<Object> values);

}
