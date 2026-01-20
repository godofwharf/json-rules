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

package io.appform.jsonrules.expressions.numeric;

import io.appform.jsonrules.ExpressionEvaluationContext;
import io.appform.jsonrules.ExpressionType;
import io.appform.jsonrules.expressions.JsonPathBasedExpression;
import io.appform.jsonrules.expressions.preoperation.PreOperation;
import io.appform.jsonrules.utils.ComparisonUtils;
import io.appform.jsonrules.utils.JsonPathUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


/**
 * All numeric binary expressions
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class NumericJsonPathBasedExpression extends JsonPathBasedExpression {
    private Object value;
    private boolean extractValueFromPath;

    protected NumericJsonPathBasedExpression(ExpressionType type) {
        super(type);
    }

    protected NumericJsonPathBasedExpression(ExpressionType type, String path, Object value,
                                             boolean extractValueFromPath, boolean defaultResult, PreOperation<?> preoperation) {
        super(type, path, defaultResult, preoperation);
        this.value = value;
        this.extractValueFromPath = extractValueFromPath;
    }

    @Override
    protected final boolean evaluate(ExpressionEvaluationContext context, String path, Object evaluatedNode) {
        if (null == evaluatedNode || !(evaluatedNode instanceof Number)) {
            return false;
        }

        Number numericalValue;
        if (extractValueFromPath) {
            Object raw = JsonPathUtils.read(ComparisonUtils.SUPPRESS_EXCEPTION_CONFIG, context.getNode(), String.valueOf(value));
            if (raw == null || !(raw instanceof Number)) {
                return false;
            }
            numericalValue = (Number) raw;
        } else {
            numericalValue = (Number) value;
        }

        Number nodeValue = (Number) evaluatedNode;
        int comparisonResult = 0;

        // Check if both are integral or floating point
        boolean nodeIsIntegral = (nodeValue instanceof Integer || nodeValue instanceof Long ||
                nodeValue instanceof Short || nodeValue instanceof Byte);
        boolean valueIsIntegral = (numericalValue instanceof Integer || numericalValue instanceof Long ||
                numericalValue instanceof Short || numericalValue instanceof Byte);

        if (nodeIsIntegral && valueIsIntegral) {
            comparisonResult = Long.compare(nodeValue.longValue(), numericalValue.longValue());
        } else {
            comparisonResult = Double.compare(nodeValue.doubleValue(), numericalValue.doubleValue());
        }

        return evaluate(context, comparisonResult);
    }

    protected abstract boolean evaluate(ExpressionEvaluationContext context, int comparisonResult);
}
