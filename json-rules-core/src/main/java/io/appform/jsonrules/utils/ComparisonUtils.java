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

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import io.appform.jsonrules.ExpressionEvaluationContext;
import io.appform.jsonrules.config.EvaluationConfiguration;

/**
 * Created by santanu on 15/9/16.
 */
public class ComparisonUtils {
    public static final Configuration SUPPRESS_EXCEPTION_CONFIG = EvaluationConfiguration.getInstance().getConfiguration()
            .addOptions(Option.SUPPRESS_EXCEPTIONS);

    private ComparisonUtils() {
    }

    public static int compare(Object evaluatedNode, Object value) {
        int comparisonResult = 0;
        if (isNumber(evaluatedNode)) {
            if (isNumber(value)) {
                return compare(evaluatedNode, asNumber(value));
            }
            return compare(evaluatedNode, (Number) value);
        } else if (isBoolean(evaluatedNode)) {
            if (isBoolean(value)) {
                return compare(evaluatedNode, asBoolean(value));
            }
            return compare(evaluatedNode, Boolean.parseBoolean(value.toString()));
        } else if (isString(evaluatedNode)) {
            if (isString(value)) {
                return compare(evaluatedNode, asString(value));
            }
            return compare(evaluatedNode, String.valueOf(value));
        } else if (evaluatedNode instanceof JSONObject) {
            throw new IllegalArgumentException("Object comparisons not supported");
        }
        return comparisonResult;
    }

    public static int compare(Object evaluatedNode, Number value) {
        int comparisonResult = 0;
        if (isNumber(evaluatedNode)) {
            Number nodeValue = asNumber(evaluatedNode);
            if (isIntegralNumber(evaluatedNode)) {
                comparisonResult = Long.compare(nodeValue.longValue(), value.longValue());
            } else {
                comparisonResult = Double.compare(nodeValue.doubleValue(), value.doubleValue());
            }
        }
        return comparisonResult;
    }

    public static int compare(Object evaluatedNode, Boolean value) {
        int comparisonResult = 0;
        if (isBoolean(evaluatedNode)) {
            final boolean bValue = Boolean.parseBoolean(value.toString());
            comparisonResult = Boolean.compare(asBoolean(evaluatedNode), bValue);
        }
        return comparisonResult;
    }

    public static int compare(Object evaluatedNode, String value) {
        int comparisonResult = -1;
        if (isString(evaluatedNode)) {
            comparisonResult = asString(evaluatedNode).compareTo(value);
        }
        return comparisonResult;
    }

    public static boolean compareForEquality(ExpressionEvaluationContext context,
                                             Object evaluatedNode,
                                             Object value) {
        final boolean nodeMissingOrNullCheck = isNodeMissingOrNull(evaluatedNode);

        // If value is a JSON path, extract it
        if (!(isJSONType(value))) {
            Object raw = JsonPathUtils.read(SUPPRESS_EXCEPTION_CONFIG, context.getNode(), String.valueOf(value));
            value = raw;
        }

        if (isNodeMissingOrNull(value)) {
            return nodeMissingOrNullCheck;
        } else if (isNumber(value)) {
            if (isIntegralNumber(value)) {
                return !nodeMissingOrNullCheck && compare(evaluatedNode, asNumber(value).longValue()) == 0;
            } else {
                return !nodeMissingOrNullCheck && compare(evaluatedNode, asNumber(value).doubleValue()) == 0;
            }
        } else if (isBoolean(value)) {
            return !nodeMissingOrNullCheck
                    && ComparisonUtils.compare(evaluatedNode, asBoolean(value)) == 0;
        } else if (isString(value)) {
            return !nodeMissingOrNullCheck && compare(evaluatedNode, asString(value)) == 0;
        } else {
            return !nodeMissingOrNullCheck && compare(evaluatedNode, value) == 0;
        }
    }

    public static boolean compareForNotEquals(ExpressionEvaluationContext context,
                                              Object evaluatedNode,
                                              Object value) {
        final boolean nodeMissingOrNullCheck = isNodeMissingOrNull(evaluatedNode);

        // If value is a JSON path, extract it
        if (!(isJSONType(value))) {
            Object raw = JsonPathUtils.read(SUPPRESS_EXCEPTION_CONFIG, context.getNode(), String.valueOf(value));
            value = raw;
        }

        if (isNodeMissingOrNull(value)) {
            return !nodeMissingOrNullCheck;
        } else if (isNumber(value)) {
            if (isIntegralNumber(value)) {
                return nodeMissingOrNullCheck || compare(evaluatedNode, asNumber(value).longValue()) != 0;
            } else {
                return nodeMissingOrNullCheck || compare(evaluatedNode, asNumber(value).doubleValue()) != 0;
            }
        } else if (isBoolean(value)) {
            return nodeMissingOrNullCheck || compare(evaluatedNode, asBoolean(value)) != 0;
        } else if (isString(value)) {
            return nodeMissingOrNullCheck || compare(evaluatedNode, asString(value)) != 0;
        } else {
            return nodeMissingOrNullCheck || compare(evaluatedNode, value) != 0;
        }
    }

    public static boolean isNodeMissingOrNull(Object node) {
        return null == node;
    }

    // Helper methods for type checking and conversion
    private static boolean isJSONType(Object value) {
        return value instanceof JSONObject || value instanceof JSONArray;
    }

    private static boolean isNumber(Object value) {
        return value instanceof Number;
    }

    private static boolean isIntegralNumber(Object value) {
        return value instanceof Integer || value instanceof Long ||
                value instanceof Short || value instanceof Byte;
    }

    private static boolean isBoolean(Object value) {
        return value instanceof Boolean;
    }

    private static boolean isString(Object value) {
        return value instanceof String;
    }

    private static Number asNumber(Object value) {
        return (Number) value;
    }

    private static Boolean asBoolean(Object value) {
        return (Boolean) value;
    }

    private static String asString(Object value) {
        return (String) value;
    }

    public static boolean getDefaultResult(Boolean defaultResult, boolean resultIfNull) {
        if (null == defaultResult)
            return resultIfNull;
        return defaultResult;
    }
}
