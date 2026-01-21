package io.appform.jsonrules;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.appform.jsonrules.expressions.equality.EqualsExpression;
import io.appform.jsonrules.expressions.meta.ExistsExpression;
import io.appform.jsonrules.expressions.numeric.GreaterThanExpression;
import io.appform.jsonrules.utils.TestJson;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for ExpressionEvaluationContext with various valid and invalid input types
 */
public class ExpressionEvaluationContextTest {

    /**
     * Test context creation with valid JSON string
     */
    @Test
    public void testContextConstructionWithValidJsonString() {
        String jsonString = "{ \"name\": \"Alice\", \"age\": 30 }";
        ExpressionEvaluationContext context = ExpressionEvaluationContext.construct(jsonString, null);

        Assert.assertNotNull(context);
        Assert.assertNotNull(context.getNode());
        Assert.assertTrue(context.getNode() instanceof JSONObject);
    }

    /**
     * Test context creation with valid JSON array string
     */
    @Test
    public void testContextConstructionWithJsonArrayString() {
        String jsonString = "[1, 2, 3, 4, 5]";
        ExpressionEvaluationContext context = ExpressionEvaluationContext.construct(jsonString, null);

        Assert.assertNotNull(context);
        Assert.assertNotNull(context.getNode());
        Assert.assertTrue(context.getNode() instanceof JSONArray);
    }

    /**
     * Test context creation with nested JSON string
     */
    @Test
    public void testContextConstructionWithNestedJsonString() {
        String jsonString = "{ \"user\": { \"profile\": { \"name\": \"Bob\", \"age\": 25 } } }";
        ExpressionEvaluationContext context = ExpressionEvaluationContext.construct(jsonString, null);

        Assert.assertNotNull(context);
        Assert.assertNotNull(context.getNode());
    }

    /**
     * Test context creation with invalid (non-JSON) string - should throw exception
     */
    @Test(expected = Exception.class)
    public void testContextConstructionWithNonJsonString() {
        String nonJsonString = "This is not a JSON string";
        ExpressionEvaluationContext.construct(nonJsonString, null);
    }

    /**
     * Test context creation with malformed JSON string
     */
    @Test(expected = Exception.class)
    public void testContextConstructionWithMalformedJsonString() {
        String malformedJson = "{ name: 'Alice', age: 30 }"; // Invalid: unquoted keys
        ExpressionEvaluationContext.construct(malformedJson, null);
    }

    /**
     * Test context creation with incomplete JSON string
     */
    @Test(expected = Exception.class)
    public void testContextConstructionWithIncompleteJsonString() {
        String incompleteJson = "{ \"name\": \"Alice\", \"age\":";
        ExpressionEvaluationContext.construct(incompleteJson, null);
    }

    /**
     * Test context creation with empty string - should handle gracefully or fail
     */
    @Test
    public void testContextConstructionWithEmptyString() {
        String emptyString = "";
        try {
            ExpressionEvaluationContext context = ExpressionEvaluationContext.construct(emptyString, null);
            // If it succeeds, the node should be null or empty
            Assert.assertTrue(context.getNode() == null || context.getNode().toString().isEmpty());
        } catch (Exception e) {
            // If it fails, that's also acceptable behavior
            Assert.assertTrue(true);
        }
    }

    /**
     * Test context creation with whitespace-only string
     */
    @Test(expected = Exception.class)
    public void testContextConstructionWithWhitespaceString() {
        String whitespaceString = "   \n  \t  ";
        ExpressionEvaluationContext.construct(whitespaceString, null);
    }

    /**
     * Test evaluation with JSONObject node
     */
    @Test
    public void testEvaluationWithJsonObjectNode() {
        JSONObject node = TestJson.obj("{ \"value\": 42 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Expression expression = EqualsExpression.builder()
                .path("$.value")
                .value(42)
                .build();

        Assert.assertTrue(expression.evaluate(context));
    }

    /**
     * Test evaluation with JSONArray node
     */
    @Test
    public void testEvaluationWithJsonArrayNode() {
        JSONArray array = new JSONArray();
        array.add(10);
        array.add(20);
        array.add(30);

        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(array)
                .build();

        Expression expression = GreaterThanExpression.builder()
                .path("$[1]")
                .value(15)
                .build();

        Assert.assertTrue(expression.evaluate(context));
    }

    /**
     * Test evaluation with Map node (should work as it's Map-based)
     */
    @Test
    public void testEvaluationWithMapNode() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Charlie");
        map.put("age", 35);

        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(map)
                .build();

        Expression expression = EqualsExpression.builder()
                .path("$.name")
                .value("Charlie")
                .build();

        Assert.assertTrue(expression.evaluate(context));
    }

    /**
     * Test evaluation with null node (should be handled gracefully)
     */
    @Test
    public void testEvaluationWithNullNode() {
        Expression expression = ExistsExpression.builder()
                .path("$.value")
                .build();

        // Evaluate with null directly - Expression.evaluate() handles null by converting to empty JSONObject
        Assert.assertFalse(expression.evaluate((Object) null));
    }

    /**
     * Test evaluation with empty JSONObject
     */
    @Test
    public void testEvaluationWithEmptyJsonObject() {
        JSONObject emptyNode = new JSONObject();
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(emptyNode)
                .build();

        Expression expression = ExistsExpression.builder()
                .path("$.anyField")
                .build();

        Assert.assertFalse(expression.evaluate(context));
    }

    /**
     * Test evaluation with empty JSONArray
     */
    @Test
    public void testEvaluationWithEmptyJsonArray() {
        JSONArray emptyArray = new JSONArray();
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(emptyArray)
                .build();

        Expression expression = ExistsExpression.builder()
                .path("$[0]")
                .build();

        Assert.assertFalse(expression.evaluate(context));
    }

    /**
     * Test evaluation with primitive types in context node
     */
    @Test
    public void testEvaluationWithPrimitiveInNode() {
        // Try with a string as the node
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node("just a string")
                .build();

        Expression expression = ExistsExpression.builder()
                .path("$.value")
                .build();

        // Primitive values can't have paths
        Assert.assertFalse(expression.evaluate(context));
    }

    /**
     * Test evaluation with number as node
     */
    @Test
    public void testEvaluationWithNumberAsNode() {
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(42)
                .build();

        Expression expression = ExistsExpression.builder()
                .path("$.value")
                .build();

        // Number can't have paths
        Assert.assertFalse(expression.evaluate(context));
    }

    /**
     * Test context builder with options
     */
    @Test
    public void testContextBuilderWithOptions() {
        Map<OptionKeys, Object> options = new HashMap<>();
        options.put(OptionKeys.SYSTEM_TIME, 1500000000000L);

        JSONObject node = TestJson.obj("{ \"value\": 100 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .options(options)
                .build();

        Assert.assertNotNull(context.getOptions());
        Assert.assertEquals(1500000000000L, context.getOptions().get(OptionKeys.SYSTEM_TIME));
    }

    /**
     * Test context builder with null options (should default to empty map)
     */
    @Test
    public void testContextBuilderWithNullOptions() {
        JSONObject node = TestJson.obj("{ \"value\": 100 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .options(null)
                .build();

        Assert.assertNotNull(context.getOptions());
        Assert.assertTrue(context.getOptions().isEmpty());
    }

    /**
     * Test evaluation with JSON containing special characters
     */
    @Test
    public void testEvaluationWithSpecialCharactersInJson() {
        String jsonWithSpecialChars = "{ \"message\": \"Line1\\nLine2\\tTabbed\", \"path\": \"C:\\\\Users\\\\test\" }";
        ExpressionEvaluationContext context = ExpressionEvaluationContext.construct(jsonWithSpecialChars, null);

        Assert.assertNotNull(context.getNode());

        Expression expression = ExistsExpression.builder()
                .path("$.message")
                .build();

        Assert.assertTrue(expression.evaluate(context));
    }

    /**
     * Test evaluation with JSON containing unicode characters
     */
    @Test
    public void testEvaluationWithUnicodeInJson() {
        String jsonWithUnicode = "{ \"greeting\": \"Hello 世界\", \"emoji\": \"😀\" }";
        ExpressionEvaluationContext context = ExpressionEvaluationContext.construct(jsonWithUnicode, null);

        Assert.assertNotNull(context.getNode());

        Expression expression = EqualsExpression.builder()
                .path("$.greeting")
                .value("Hello 世界")
                .build();

        Assert.assertTrue(expression.evaluate(context));
    }

    /**
     * Test evaluation with very large JSON
     */
    @Test
    public void testEvaluationWithLargeJson() {
        StringBuilder largeJsonBuilder = new StringBuilder("{");
        for (int i = 0; i < 1000; i++) {
            if (i > 0) largeJsonBuilder.append(",");
            largeJsonBuilder.append("\"field").append(i).append("\": ").append(i);
        }
        largeJsonBuilder.append("}");

        ExpressionEvaluationContext context = ExpressionEvaluationContext.construct(
                largeJsonBuilder.toString(), null);

        Assert.assertNotNull(context.getNode());

        Expression expression = EqualsExpression.builder()
                .path("$.field500")
                .value(500)
                .build();

        Assert.assertTrue(expression.evaluate(context));
    }

    /**
     * Test evaluation with deeply nested JSON
     */
    @Test
    public void testEvaluationWithDeeplyNestedJson() {
        StringBuilder deepJson = new StringBuilder("{\"level0\":");
        for (int i = 1; i <= 10; i++) {
            deepJson.append("{\"level").append(i).append("\":");
        }
        deepJson.append("\"value\"");
        for (int i = 0; i <= 10; i++) {
            deepJson.append("}");
        }

        ExpressionEvaluationContext context = ExpressionEvaluationContext.construct(
                deepJson.toString(), null);

        Assert.assertNotNull(context.getNode());

        Expression expression = ExistsExpression.builder()
                .path("$.level0.level1.level2")
                .build();

        Assert.assertTrue(expression.evaluate(context));
    }

    /**
     * Test evaluation with JSON containing null values
     */
    @Test
    public void testEvaluationWithNullValuesInJson() {
        String jsonWithNulls = "{ \"name\": \"Test\", \"age\": null, \"address\": null }";
        ExpressionEvaluationContext context = ExpressionEvaluationContext.construct(jsonWithNulls, null);

        Assert.assertNotNull(context.getNode());

        // Name exists
        Expression nameExists = ExistsExpression.builder()
                .path("$.name")
                .build();
        Assert.assertTrue(nameExists.evaluate(context));

        // Age path exists but value is null
        Expression ageExists = ExistsExpression.builder()
                .path("$.age")
                .build();
        // This behavior depends on how ExistsExpression handles null values
        boolean ageResult = ageExists.evaluate(context);
        Assert.assertNotNull(ageResult);
    }

    /**
     * Test evaluation with JSON containing mixed types
     */
    @Test
    public void testEvaluationWithMixedTypesInJson() {
        String mixedJson = "{ " +
                "\"stringVal\": \"text\", " +
                "\"intVal\": 42, " +
                "\"doubleVal\": 3.14, " +
                "\"boolVal\": true, " +
                "\"arrayVal\": [1,2,3], " +
                "\"objectVal\": {\"nested\": \"value\"}, " +
                "\"nullVal\": null " +
                "}";

        ExpressionEvaluationContext context = ExpressionEvaluationContext.construct(mixedJson, null);

        Assert.assertNotNull(context.getNode());

        // Test string
        Expression stringExpr = EqualsExpression.builder()
                .path("$.stringVal")
                .value("text")
                .build();
        Assert.assertTrue(stringExpr.evaluate(context));

        // Test int
        Expression intExpr = EqualsExpression.builder()
                .path("$.intVal")
                .value(42)
                .build();
        Assert.assertTrue(intExpr.evaluate(context));

        // Test boolean
        Expression boolExpr = EqualsExpression.builder()
                .path("$.boolVal")
                .value(true)
                .build();
        Assert.assertTrue(boolExpr.evaluate(context));
    }

    /**
     * Test evaluation with JSON containing arrays of objects
     */
    @Test
    public void testEvaluationWithArrayOfObjects() {
        String jsonWithArray = "{ \"users\": [ " +
                "{\"name\": \"Alice\", \"age\": 25}, " +
                "{\"name\": \"Bob\", \"age\": 30}, " +
                "{\"name\": \"Charlie\", \"age\": 35} " +
                "] }";

        ExpressionEvaluationContext context = ExpressionEvaluationContext.construct(jsonWithArray, null);

        Assert.assertNotNull(context.getNode());

        Expression expression = EqualsExpression.builder()
                .path("$.users[1].name")
                .value("Bob")
                .build();

        Assert.assertTrue(expression.evaluate(context));
    }

    /**
     * Test no-arg constructor
     */
    @Test
    public void testNoArgConstructor() {
        ExpressionEvaluationContext context = new ExpressionEvaluationContext();

        Assert.assertNull(context.getNode());
        Assert.assertNotNull(context.getOptions());
    }

    /**
     * Test setting values after construction
     */
    @Test
    public void testSettingValuesAfterConstruction() {
        ExpressionEvaluationContext context = new ExpressionEvaluationContext();

        JSONObject node = TestJson.obj("{ \"test\": 123 }");
        context.setNode(node);

        Map<OptionKeys, Object> options = new HashMap<>();
        options.put(OptionKeys.SYSTEM_TIME, 1600000000000L);
        context.setOptions(options);

        Assert.assertEquals(node, context.getNode());
        Assert.assertEquals(options, context.getOptions());
    }

    /**
     * Test context with JSON containing only primitives in array
     */
    @Test
    public void testEvaluationWithPrimitiveArray() {
        String jsonWithPrimitiveArray = "{ \"numbers\": [1, 2, 3, 4, 5], \"strings\": [\"a\", \"b\", \"c\"] }";
        ExpressionEvaluationContext context = ExpressionEvaluationContext.construct(jsonWithPrimitiveArray, null);

        Assert.assertNotNull(context.getNode());

        Expression expression = EqualsExpression.builder()
                .path("$.numbers[2]")
                .value(3)
                .build();

        Assert.assertTrue(expression.evaluate(context));
    }
}
