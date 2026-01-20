package io.appform.jsonrules.test;

import com.alibaba.fastjson2.JSONObject;
import io.appform.jsonrules.ExpressionEvaluationContext;
import io.appform.jsonrules.expressions.string.StartsWithExpression;

/**
 * Quick test to verify the path missing detection works correctly
 */
public class QuickPathTest {
    public static void main(String[] args) {
        // Create a test JSONObject
        JSONObject node = JSONObject.parseObject("{ \"value\": 20, \"string\" : \"Hello\", \"kid\": null }");

        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        // Test 1: Missing path with defaultResult=true
        StartsWithExpression missingPathExpr = StartsWithExpression.builder()
                .path("$.somepath")
                .defaultResult(true)
                .build();

        boolean result1 = missingPathExpr.evaluate(context);
        System.out.println("Test 1 (missing path, defaultResult=true): " + result1 + " (expected: true)");

        // Test 2: Existing path with null value
        StartsWithExpression nullPathExpr = StartsWithExpression.builder()
                .path("$.kid")
                .defaultResult(true)
                .build();

        boolean result2 = nullPathExpr.evaluate(context);
        System.out.println("Test 2 (null value path): " + result2 + " (expected: false, because kid is null not text)");

        // Test 3: Existing path with text value but no comparison value
        StartsWithExpression existingPathExpr = StartsWithExpression.builder()
                .path("$.string")
                .defaultResult(false)
                .build();

        boolean result3 = existingPathExpr.evaluate(context);
        System.out.println("Test 3 (existing text path, no value to compare): " + result3 + " (expected: false)");

        // Test 4: Existing path with text value and matching starts-with
        StartsWithExpression matchingExpr = StartsWithExpression.builder()
                .path("$.string")
                .value("Hel")
                .defaultResult(false)
                .build();

        boolean result4 = matchingExpr.evaluate(context);
        System.out.println("Test 4 (existing path, starts with 'Hel'): " + result4 + " (expected: true)");

        System.out.println("\nAll tests completed!");
    }
}
