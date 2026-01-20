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
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Context passed to expression evaluator
 */
@Data
@NoArgsConstructor
public class ExpressionEvaluationContext {
    private Object node;
    private Map<OptionKeys, Object> options = new HashMap<>();

    @Builder
    public ExpressionEvaluationContext(final Object node,
                                       final Map<OptionKeys, Object> options) {
        this.node = node;
        this.options = options == null ? new HashMap<>() : options;
    }

    public static ExpressionEvaluationContext construct(final String json,
                                                        final Map<OptionKeys, Object> options) {
        Object node = JSON.parse(json);
        return new ExpressionEvaluationContext(node, options);
    }
}
