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

package io.appform.jsonrules.expressions;

import com.jayway.jsonpath.PathNotFoundException;
import io.appform.jsonrules.Expression;
import io.appform.jsonrules.ExpressionEvaluationContext;
import io.appform.jsonrules.ExpressionType;
import io.appform.jsonrules.config.EvaluationConfiguration;
import io.appform.jsonrules.expressions.preoperation.PreOperation;
import io.appform.jsonrules.utils.JsonPathUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.val;


/**
 * All expressions that evaluate a json path uses this.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class JsonPathBasedExpression extends Expression {
    private String path;
    private PreOperation<?> preoperation;
    private boolean defaultResult;

    protected JsonPathBasedExpression(ExpressionType type) {
        super(type);
    }

    protected JsonPathBasedExpression(ExpressionType type, String path, boolean defaultResult,
            PreOperation<?> preoperation) {
        this(type);
        this.path = path;
        this.preoperation = preoperation;
        this.defaultResult = defaultResult;
    }

    @Override
    public final boolean evaluate(ExpressionEvaluationContext context) {
        Object nodeAtPath;
        try {
            Object rawValue = JsonPathUtils.read(EvaluationConfiguration.getInstance().getConfiguration(), context.getNode(), path);
            if (rawValue != null) {
                nodeAtPath = rawValue;
            } else {
                // rawValue is null - could be either explicit null value OR missing path.
                // For simple paths like $.key, check if the key actually exists
                if (isSimplePathMissing(context.getNode(), path)) {
                    // Path doesn't exist - use default result
                    return defaultResult;
                }
                // Path exists but value is null - treat as null
                nodeAtPath = null;
            }
        } catch (PathNotFoundException exception) {
            // Using default result when the 'path' doesn't exist
            return defaultResult;
        }

        Object evaluatedNode = applyPreoperation(context, nodeAtPath);
        return evaluate(context, path, evaluatedNode);
    }

    /**
     * For a simple definite path like $.key or nested paths like $.a.b.c, check if it exists.
     * Returns true if the path is definitely missing, false if it exists or we can't determine.
     */
    private boolean isSimplePathMissing(Object node, String path) {
        // Handle paths starting with $.
        if (path == null || !path.startsWith("$.") || path.length() <= 2) {
            return false;
        }

        String pathRemainder = path.substring(2); // Remove $.

        // Only handle simple property paths (no arrays, wildcards, filters, etc.)
        if (pathRemainder.contains("[") || pathRemainder.contains("*") ||
            pathRemainder.contains("?") || pathRemainder.contains("@")) {
            return false;
        }

        // Split by dots to handle nested paths like a.b.c
        String[] parts = pathRemainder.split("\\.");
        Object current = node;

        for (String part : parts) {
            if (!(current instanceof java.util.Map)) {
                // Can't traverse further if current is not a Map
                return false;
            }

            java.util.Map<?, ?> map = (java.util.Map<?, ?>) current;
            if (!map.containsKey(part)) {
                // This key doesn't exist - path is missing
                return true;
            }

            // Move to next level
            current = map.get(part);
        }

        // All parts of the path exist
        return false;
    }

    private Object applyPreoperation(ExpressionEvaluationContext globalContext, Object nodeAtPath) {
        if (null == preoperation) {
            return nodeAtPath;
        }

        val newContext = ExpressionEvaluationContext.builder()
                .node(nodeAtPath)
                .options(globalContext.getOptions())
                .build();

        return preoperation.compute(newContext);
    }

    protected abstract boolean evaluate(ExpressionEvaluationContext context, final String path, Object evaluatedNode);
}
