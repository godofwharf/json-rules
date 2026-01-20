/**
 * Copyright (c) 2018 Mohammed Irfanulla S <mohammed.irfanulla.s1@gmail.com>
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

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONType;
import io.appform.jsonrules.ExpressionEvaluationContext;
import io.appform.jsonrules.ExpressionType;
import io.appform.jsonrules.ExpressionVisitor;
import io.appform.jsonrules.expressions.JsonPathBasedExpression;
import io.appform.jsonrules.expressions.preoperation.PreOperation;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JSONType(typeName = "between")
public class BetweenExpression extends JsonPathBasedExpression {
    private Number lowerBound;
    private Number upperBound;
    private boolean includeLowerBound;
    private boolean includeUpperBound;

    public BetweenExpression() {
        super(ExpressionType.between);
    }

    @Builder
    public BetweenExpression(String path,
                             Number lowerbound,
                             Number upperBound,
                             boolean includeLowerBound,
                             boolean includeUpperBound,
                             boolean defaultResult,
                             PreOperation<?> preoperation) {
        super(ExpressionType.between, path, defaultResult, preoperation);
        this.lowerBound = lowerbound;
        this.upperBound = upperBound;
        this.includeLowerBound = includeLowerBound;
        this.includeUpperBound = includeUpperBound;
    }

    @Override
    protected boolean evaluate(ExpressionEvaluationContext context, String path, Object evaluatedNode) {
        if (null == evaluatedNode || !(evaluatedNode instanceof Number)) {
            return false;
        }

        Number nodeValue = (Number) evaluatedNode;
        boolean finalResult = false;

        // Check if both are integral or floating point
        boolean nodeIsIntegral = (nodeValue instanceof Integer || nodeValue instanceof Long ||
                                   nodeValue instanceof Short || nodeValue instanceof Byte);

        if (nodeIsIntegral) {
            finalResult = includeLowerBound ? nodeValue.longValue() >= lowerBound.longValue()
                    : nodeValue.longValue() > lowerBound.longValue();
            finalResult &= includeUpperBound ? nodeValue.longValue() <= upperBound.longValue()
                    : nodeValue.longValue() < upperBound.longValue();
        } else {
            finalResult = includeLowerBound ? nodeValue.doubleValue() >= lowerBound.doubleValue()
                    : nodeValue.doubleValue() > lowerBound.doubleValue();
            finalResult &= includeUpperBound ? nodeValue.doubleValue() <= upperBound.doubleValue()
                    : nodeValue.doubleValue() < upperBound.doubleValue();
        }
        return finalResult;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor, JSONObject jsonNode) {
        return visitor.visit(this, jsonNode);
    }
}
