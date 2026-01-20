/**
 * Copyright (c) 2018 Mohammed Irfanulla S <mohammed.irfanulla.s1@gmail.com>
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

package io.appform.jsonrules.expressions.preoperation.array;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONType;
import io.appform.jsonrules.ExpressionEvaluationContext;
import io.appform.jsonrules.expressions.preoperation.PreOperation;
import io.appform.jsonrules.expressions.preoperation.PreOperationType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JSONType(typeName = "size")
public class SizeOperation extends PreOperation<Number> {

    public SizeOperation() {
        super(PreOperationType.size);
    }

    @Override
    public Number compute(ExpressionEvaluationContext context) {
        final Object node = context.getNode();
        if (node instanceof JSONArray) {
            return ((JSONArray) node).size();
        }
        if (node instanceof JSONObject) {
            return ((JSONObject) node).size();
        }
        if (node instanceof java.util.Collection) {
            return ((java.util.Collection<?>) node).size();
        }
        if (node instanceof java.util.Map) {
            return ((java.util.Map<?, ?>) node).size();
        }
        if (node != null && node.getClass().isArray()) {
            return java.lang.reflect.Array.getLength(node);
        }
        throw new IllegalArgumentException("Size operation is not supported");
    }
}
