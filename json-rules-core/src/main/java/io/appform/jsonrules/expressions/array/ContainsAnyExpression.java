/**
 * Copyright (c) 2017 Mohammed Irfanulla S <mohammed.irfanulla.s1@gmail.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.appform.jsonrules.expressions.array;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONType;
import io.appform.jsonrules.ExpressionType;
import io.appform.jsonrules.ExpressionVisitor;
import io.appform.jsonrules.expressions.preoperation.PreOperation;
import io.appform.jsonrules.utils.JsonUtils;
import lombok.*;

import java.util.Set;

/**
 * Compares collections - partial match
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JSONType(typeName = "contains_any")
public class ContainsAnyExpression extends CollectionJsonPathBasedExpression {

    public ContainsAnyExpression() {
        super(ExpressionType.contains_any);
    }

    @Builder
    public ContainsAnyExpression(String path,
                                 @Singular Set<Object> values,
                                 boolean extractValues,
                                 String valuesPath,
                                 boolean defaultResult,
                                 PreOperation<?> preoperation) {
        // No pre-operations supported on this expression.
        super(ExpressionType.contains_any, path, values, extractValues, valuesPath, defaultResult, null);
    }

    @Override
    protected boolean evaluate(Object evaluatedNode, Set<Object> values) {
        if (null == values || values.isEmpty() || !(evaluatedNode instanceof JSONArray)) {
            return false;
        }
        return JsonUtils.checkAnyMatch((JSONArray) evaluatedNode, values);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor, JSONObject jsonNode) {
        return visitor.visit(this, jsonNode);
    }
}
