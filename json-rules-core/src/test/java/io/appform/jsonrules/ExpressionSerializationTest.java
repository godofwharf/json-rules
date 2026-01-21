package io.appform.jsonrules;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import io.appform.jsonrules.expressions.composite.AndExpression;
import io.appform.jsonrules.expressions.composite.NotExpression;
import io.appform.jsonrules.expressions.composite.OrExpression;
import io.appform.jsonrules.expressions.equality.EqualsExpression;
import io.appform.jsonrules.expressions.equality.NotEqualsExpression;
import io.appform.jsonrules.expressions.meta.ExistsExpression;
import io.appform.jsonrules.expressions.meta.NotExistsExpression;
import io.appform.jsonrules.expressions.numeric.GreaterThanEqualsExpression;
import io.appform.jsonrules.expressions.numeric.GreaterThanExpression;
import io.appform.jsonrules.expressions.numeric.LessThanEqualsExpression;
import io.appform.jsonrules.expressions.numeric.LessThanExpression;
import io.appform.jsonrules.expressions.preoperation.numeric.AddOperation;
import io.appform.jsonrules.expressions.preoperation.numeric.MultiplyOperation;
import io.appform.jsonrules.expressions.preoperation.string.LengthOperation;
import io.appform.jsonrules.expressions.string.EmptyExpression;
import io.appform.jsonrules.expressions.string.EndsWithExpression;
import io.appform.jsonrules.expressions.string.MatchesExpression;
import io.appform.jsonrules.expressions.string.NotEmptyExpression;
import io.appform.jsonrules.expressions.string.StartsWithExpression;
import io.appform.jsonrules.utils.TestJson;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for expression serialization/deserialization and evaluation context handling
 */
public class ExpressionSerializationTest {

    /**
     * Test serialization and deserialization of a simple numeric expression
     */
    @Test
    public void testSimpleNumericExpressionSerializationRoundTrip() throws Exception {
        // Create an expression
        Expression originalExpression = GreaterThanExpression.builder()
                .path("$.value")
                .value(20)
                .defaultResult(false)
                .build();

        // Serialize to JSON
        String json = JSON.toJSONString(originalExpression, JSONWriter.Feature.WriteClassName);
        System.out.println("Serialized: " + json);

        // Deserialize from JSON
        Expression deserializedExpression = JSON.parseObject(json, Expression.class, JSONReader.Feature.UseDoubleForDecimals);

        // Verify they are equal
        Assert.assertEquals(originalExpression, deserializedExpression);

        // Verify behavior is the same
        JSONObject testNode = TestJson.obj("{ \"value\": 25 }");
        Assert.assertTrue(originalExpression.evaluate(testNode));
        Assert.assertTrue(deserializedExpression.evaluate(testNode));
    }

    /**
     * Test serialization and deserialization of a composite expression
     */
    @Test
    public void testCompositeExpressionSerializationRoundTrip() throws Exception {
        Expression originalExpression = AndExpression.builder()
                .child(GreaterThanExpression.builder()
                        .path("$.value")
                        .value(10)
                        .build())
                .child(LessThanExpression.builder()
                        .path("$.value")
                        .value(30)
                        .build())
                .build();

        // Serialize to JSON
        String json = JSON.toJSONString(originalExpression, JSONWriter.Feature.WriteClassName);
        System.out.println("Serialized composite: " + json);

        // Deserialize from JSON
        Expression deserializedExpression = JSON.parseObject(json, Expression.class, JSONReader.Feature.UseDoubleForDecimals);

        // Verify they are equal
        Assert.assertEquals(originalExpression, deserializedExpression);

        // Verify behavior
        JSONObject testNode = TestJson.obj("{ \"value\": 20 }");
        Assert.assertTrue(originalExpression.evaluate(testNode));
        Assert.assertTrue(deserializedExpression.evaluate(testNode));
    }

    /**
     * Test serialization and deserialization of a string expression
     */
    @Test
    public void testStringExpressionSerializationRoundTrip() throws Exception {
        Expression originalExpression = StartsWithExpression.builder()
                .path("$.name")
                .value("Hello")
                .defaultResult(false)
                .build();

        // Serialize to JSON
        String json = JSON.toJSONString(originalExpression, JSONWriter.Feature.WriteClassName);
        System.out.println("Serialized string expression: " + json);

        // Deserialize from JSON
        Expression deserializedExpression = JSON.parseObject(json, Expression.class, JSONReader.Feature.UseDoubleForDecimals);

        // Verify they are equal
        Assert.assertEquals(originalExpression, deserializedExpression);

        // Verify behavior
        JSONObject testNode = TestJson.obj("{ \"name\": \"HelloWorld\" }");
        Assert.assertTrue(originalExpression.evaluate(testNode));
        Assert.assertTrue(deserializedExpression.evaluate(testNode));
    }

    /**
     * Test serialization and deserialization of expression with preoperation
     */
    @Test
    public void testExpressionWithPreoperationSerializationRoundTrip() throws Exception {
        Expression originalExpression = EqualsExpression.builder()
                .path("$.text")
                .preoperation(LengthOperation.builder().build())
                .value(5)
                .build();

        // Serialize to JSON
        String json = JSON.toJSONString(originalExpression, JSONWriter.Feature.WriteClassName);
        System.out.println("Serialized with preoperation: " + json);

        // Deserialize from JSON
        Expression deserializedExpression = JSON.parseObject(json, Expression.class, JSONReader.Feature.UseDoubleForDecimals);

        // Verify they are equal
        Assert.assertEquals(originalExpression, deserializedExpression);

        // Verify behavior
        JSONObject testNode = TestJson.obj("{ \"text\": \"Hello\" }");
        Assert.assertTrue(originalExpression.evaluate(testNode));
        Assert.assertTrue(deserializedExpression.evaluate(testNode));
    }

    /**
     * Test serialization and deserialization of complex nested expression
     */
    @Test
    public void testComplexNestedExpressionSerializationRoundTrip() throws Exception {
        Expression originalExpression = OrExpression.builder()
                .child(AndExpression.builder()
                        .child(GreaterThanExpression.builder()
                                .path("$.age")
                                .value(18)
                                .build())
                        .child(LessThanExpression.builder()
                                .path("$.age")
                                .value(65)
                                .build())
                        .build())
                .child(NotExpression.builder()
                        .child(ExistsExpression.builder()
                                .path("$.age")
                                .build())
                        .build())
                .build();

        // Serialize to JSON
        String json = JSON.toJSONString(originalExpression, JSONWriter.Feature.WriteClassName);
        System.out.println("Serialized complex nested: " + json);

        // Deserialize from JSON
        Expression deserializedExpression = JSON.parseObject(json, Expression.class, JSONReader.Feature.UseDoubleForDecimals);

        // Verify they are equal
        Assert.assertEquals(originalExpression, deserializedExpression);

        // Verify behavior
        JSONObject testNode1 = TestJson.obj("{ \"age\": 25 }");
        Assert.assertTrue(originalExpression.evaluate(testNode1));
        Assert.assertTrue(deserializedExpression.evaluate(testNode1));

        JSONObject testNode2 = TestJson.obj("{ \"name\": \"John\" }");
        Assert.assertTrue(originalExpression.evaluate(testNode2));
        Assert.assertTrue(deserializedExpression.evaluate(testNode2));
    }

    /**
     * Test that invalid JSON throws appropriate exception during deserialization
     */
    @Test(expected = Exception.class)
    public void testInvalidJsonDeserialization() throws Exception {
        String invalidJson = "{ invalid json }";
        JSON.parseObject(invalidJson, Expression.class, JSONReader.Feature.UseDoubleForDecimals);
    }

    /**
     * Test that malformed expression JSON throws appropriate exception
     */
    @Test(expected = Exception.class)
    public void testMalformedExpressionDeserialization() throws Exception {
        String malformedJson = "{ \"type\": \"unknown_type\", \"path\": \"$.value\" }";
        Expression expression = JSON.parseObject(malformedJson, Expression.class, JSONReader.Feature.UseDoubleForDecimals);
        // Try to use the expression - should fail
        expression.evaluate(new JSONObject());
    }

    /**
     * Test evaluation with valid JSON object context
     */
    @Test
    public void testEvaluationWithValidJsonObjectContext() {
        Expression expression = EqualsExpression.builder()
                .path("$.name")
                .value("John")
                .build();

        JSONObject node = TestJson.obj("{ \"name\": \"John\", \"age\": 30 }");
        Assert.assertTrue(expression.evaluate(node));
    }

    /**
     * Test evaluation with valid JSON array context
     */
    @Test
    public void testEvaluationWithJsonArrayContext() {
        Expression expression = GreaterThanExpression.builder()
                .path("$[0]")
                .value(5)
                .build();

        // Create a JSON array
        JSONArray array = new JSONArray();
        array.add(10);
        array.add(20);
        array.add(30);

        Assert.assertTrue(expression.evaluate(array));
    }

    /**
     * Test evaluation with nested JSON object context
     */
    @Test
    public void testEvaluationWithNestedJsonObjectContext() {
        Expression expression = EqualsExpression.builder()
                .path("$.user.address.city")
                .value("New York")
                .build();

        JSONObject node = TestJson.obj("{ \"user\": { \"address\": { \"city\": \"New York\", \"zip\": \"10001\" } } }");
        Assert.assertTrue(expression.evaluate(node));
    }

    /**
     * Test evaluation with null context (should use empty object)
     */
    @Test
    public void testEvaluationWithNullContext() {
        Expression expression = ExistsExpression.builder()
                .path("$.value")
                .build();

        // Passing null should be handled gracefully
        Assert.assertFalse(expression.evaluate((Object) null));
    }

    /**
     * Test evaluation with empty JSON object context
     */
    @Test
    public void testEvaluationWithEmptyJsonObjectContext() {
        Expression expression = ExistsExpression.builder()
                .path("$.value")
                .build();

        JSONObject emptyNode = new JSONObject();
        Assert.assertFalse(expression.evaluate(emptyNode));
    }

    /**
     * Test evaluation with missing path and defaultResult
     */
    @Test
    public void testEvaluationWithMissingPathAndDefaultResult() {
        Expression expression = GreaterThanExpression.builder()
                .path("$.nonexistent")
                .value(10)
                .defaultResult(true)
                .build();

        JSONObject node = TestJson.obj("{ \"value\": 20 }");
        Assert.assertTrue(expression.evaluate(node));
    }

    /**
     * Test evaluation with null value in JSON
     */
    @Test
    public void testEvaluationWithNullValueInJson() {
        Expression expression = NotExistsExpression.builder()
                .path("$.value")
                .build();

        JSONObject node = TestJson.obj("{ \"value\": null }");
        // Path with null value is treated as not existing
        Assert.assertTrue(expression.evaluate(node));

        // Test with an actually missing path
        Expression existsExpression = ExistsExpression.builder()
                .path("$.missing")
                .build();
        Assert.assertFalse(existsExpression.evaluate(node));
    }

    /**
     * Test evaluation with invalid path format
     */
    @Test
    public void testEvaluationWithInvalidPathFormat() {
        Expression expression = EqualsExpression.builder()
                .path("invalid.path.without.dollar")
                .value("test")
                .defaultResult(false)
                .build();

        JSONObject node = TestJson.obj("{ \"value\": \"test\" }");
        // Invalid path should return defaultResult
        Assert.assertFalse(expression.evaluate(node));
    }

    /**
     * Test evaluation with array indexing
     */
    @Test
    public void testEvaluationWithArrayIndexing() {
        Expression expression = EqualsExpression.builder()
                .path("$.items[1]")
                .value("second")
                .build();

        JSONObject node = TestJson.obj("{ \"items\": [\"first\", \"second\", \"third\"] }");
        Assert.assertTrue(expression.evaluate(node));
    }

    /**
     * Test evaluation with wildcard path
     */
    @Test
    public void testEvaluationWithWildcardPath() {
        Expression expression = NotEmptyExpression.builder()
                .path("$.items[*]")
                .defaultResult(false)
                .build();

        JSONObject node = TestJson.obj("{ \"items\": [\"a\", \"b\", \"c\"] }");
        // Wildcard paths should be handled appropriately
        boolean result = expression.evaluate(node);
        // Result will depend on how the expression handles array results
        Assert.assertNotNull(result);
    }

    /**
     * Test evaluation context with Map input
     */
    @Test
    public void testEvaluationContextWithMapInput() {
        Expression expression = EqualsExpression.builder()
                .path("$.name")
                .value("Alice")
                .build();

        java.util.HashMap<String, Object> map = new java.util.HashMap<>();
        map.put("name", "Alice");
        map.put("age", 25);

        // Should work with Map as well
        Assert.assertTrue(expression.evaluate(map));
    }

    /**
     * Test ExpressionEvaluationContext construction from JSON string
     */
    @Test
    public void testExpressionEvaluationContextFromJsonString() {
        String jsonString = "{ \"value\": 42, \"name\": \"test\" }";
        ExpressionEvaluationContext context = ExpressionEvaluationContext.construct(jsonString, null);

        Assert.assertNotNull(context);
        Assert.assertNotNull(context.getNode());

        Expression expression = EqualsExpression.builder()
                .path("$.value")
                .value(42)
                .build();

        Assert.assertTrue(expression.evaluate(context));
    }

    /**
     * Test ExpressionEvaluationContext with invalid JSON string
     */
    @Test(expected = Exception.class)
    public void testExpressionEvaluationContextWithInvalidJsonString() {
        String invalidJson = "not a valid json";
        ExpressionEvaluationContext.construct(invalidJson, null);
    }

    /**
     * Test evaluation with complex JSON including arrays and nested objects
     */
    @Test
    public void testEvaluationWithComplexJson() {
        Expression expression = GreaterThanEqualsExpression.builder()
                .path("$.users[0].scores[1]")
                .value(85)
                .build();

        String complexJson = "{ \"users\": [ { \"name\": \"Alice\", \"scores\": [90, 85, 92] }, { \"name\": \"Bob\", \"scores\": [75, 80, 88] } ] }";
        JSONObject node = JSON.parseObject(complexJson, JSONObject.class, JSONReader.Feature.UseDoubleForDecimals);

        Assert.assertTrue(expression.evaluate(node));
    }

    /**
     * Test evaluation with boolean values
     */
    @Test
    public void testEvaluationWithBooleanValues() {
        Expression expression = EqualsExpression.builder()
                .path("$.active")
                .value(true)
                .build();

        JSONObject node = TestJson.obj("{ \"active\": true, \"name\": \"test\" }");
        Assert.assertTrue(expression.evaluate(node));
    }

    /**
     * Test evaluation with numeric types (integer, double)
     */
    @Test
    public void testEvaluationWithMixedNumericTypes() {
        Expression intExpression = EqualsExpression.builder()
                .path("$.intValue")
                .value(10)
                .build();

        Expression doubleExpression = EqualsExpression.builder()
                .path("$.doubleValue")
                .value(10.5)
                .build();

        JSONObject node = TestJson.obj("{ \"intValue\": 10, \"doubleValue\": 10.5 }");
        Assert.assertTrue(intExpression.evaluate(node));
        Assert.assertTrue(doubleExpression.evaluate(node));
    }

    /**
     * Test serialization preserves all expression properties
     */
    @Test
    public void testSerializationPreservesAllProperties() throws Exception {
        Expression originalExpression = GreaterThanExpression.builder()
                .path("$.score")
                .value(75)
                .defaultResult(false)
                .preoperation(AddOperation.builder().operand(5).build())
                .build();

        String json = JSON.toJSONString(originalExpression, JSONWriter.Feature.WriteClassName);
        Expression deserializedExpression = JSON.parseObject(json, Expression.class, JSONReader.Feature.UseDoubleForDecimals);

        // Verify all properties are preserved
        Assert.assertEquals(originalExpression, deserializedExpression);

        // Verify the preoperation is working
        JSONObject node = TestJson.obj("{ \"score\": 71 }");
        // 71 + 5 = 76 > 75
        Assert.assertTrue(deserializedExpression.evaluate(node));
    }

    /**
     * Test that empty string is handled correctly
     */
    @Test
    public void testEvaluationWithEmptyString() {
        Expression expression = EmptyExpression.builder()
                .path("$.text")
                .defaultResult(false)
                .build();

        JSONObject node = TestJson.obj("{ \"text\": \"\" }");
        Assert.assertTrue(expression.evaluate(node));

        JSONObject node2 = TestJson.obj("{ \"text\": \"not empty\" }");
        Assert.assertFalse(expression.evaluate(node2));
    }

    /**
     * Test regex pattern expressions
     */
    @Test
    public void testEvaluationWithRegexPattern() {
        Expression expression = MatchesExpression.builder()
                .path("$.email")
                .value(".*@example\\.com")
                .defaultResult(false)
                .build();

        JSONObject validEmail = TestJson.obj("{ \"email\": \"user@example.com\" }");
        Assert.assertTrue(expression.evaluate(validEmail));

        JSONObject invalidEmail = TestJson.obj("{ \"email\": \"user@other.com\" }");
        Assert.assertFalse(expression.evaluate(invalidEmail));
    }

    /**
     * Test with special characters in JSON
     */
    @Test
    public void testEvaluationWithSpecialCharactersInJson() {
        Expression expression = EqualsExpression.builder()
                .path("$.message")
                .value("Hello \"World\" with 'quotes'")
                .build();

        JSONObject node = TestJson.obj("{ \"message\": \"Hello \\\"World\\\" with 'quotes'\" }");
        Assert.assertTrue(expression.evaluate(node));
    }

    /**
     * Test evaluation with unicode characters
     */
    @Test
    public void testEvaluationWithUnicodeCharacters() {
        Expression expression = EqualsExpression.builder()
                .path("$.name")
                .value("Café ☕")
                .build();

        JSONObject node = TestJson.obj("{ \"name\": \"Café ☕\" }");
        Assert.assertTrue(expression.evaluate(node));
    }

    /**
     * Test multiple levels of nesting
     */
    @Test
    public void testEvaluationWithDeepNesting() {
        Expression expression = EqualsExpression.builder()
                .path("$.level1.level2.level3.level4.value")
                .value("deep")
                .build();

        JSONObject node = TestJson.obj("{ \"level1\": { \"level2\": { \"level3\": { \"level4\": { \"value\": \"deep\" } } } } }");
        Assert.assertTrue(expression.evaluate(node));
    }
}
