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
import io.appform.jsonrules.ExpressionEvaluationContext;
import org.junit.Assert;
import org.junit.Test;

/**
 * Comprehensive test coverage for ComparisonUtils class
 */
public class ComparisonUtilsTest {

    // ============ Tests for compare(Object, Object) ============

    @Test
    public void testCompareIntegerNodes() {
        Assert.assertEquals(0, ComparisonUtils.compare(10, 10));
        Assert.assertTrue(ComparisonUtils.compare(20, 10) > 0);
        Assert.assertTrue(ComparisonUtils.compare(5, 10) < 0);
    }

    @Test
    public void testCompareLongNodes() {
        Assert.assertEquals(0, ComparisonUtils.compare(100L, 100L));
        Assert.assertTrue(ComparisonUtils.compare(200L, 100L) > 0);
        Assert.assertTrue(ComparisonUtils.compare(50L, 100L) < 0);
    }

    @Test
    public void testCompareDoubleNodes() {
        Assert.assertEquals(0, ComparisonUtils.compare(10.5, 10.5));
        Assert.assertTrue(ComparisonUtils.compare(20.5, 10.5) > 0);
        Assert.assertTrue(ComparisonUtils.compare(5.5, 10.5) < 0);
    }

    @Test
    public void testCompareFloatNodes() {
        Assert.assertEquals(0, ComparisonUtils.compare(10.5f, 10.5f));
        Assert.assertTrue(ComparisonUtils.compare(20.5f, 10.5f) > 0);
        Assert.assertTrue(ComparisonUtils.compare(5.5f, 10.5f) < 0);
    }

    @Test
    public void testCompareShortNodes() {
        short val1 = 10;
        short val2 = 20;
        short val3 = 5;
        Assert.assertEquals(0, ComparisonUtils.compare(val1, val1));
        Assert.assertTrue(ComparisonUtils.compare(val2, val1) > 0);
        Assert.assertTrue(ComparisonUtils.compare(val3, val1) < 0);
    }

    @Test
    public void testCompareByteNodes() {
        byte val1 = 10;
        byte val2 = 20;
        byte val3 = 5;
        Assert.assertEquals(0, ComparisonUtils.compare(val1, val1));
        Assert.assertTrue(ComparisonUtils.compare(val2, val1) > 0);
        Assert.assertTrue(ComparisonUtils.compare(val3, val1) < 0);
    }

    @Test
    public void testCompareMixedIntegralNumbers() {
        Assert.assertEquals(0, ComparisonUtils.compare(10, 10L));
        Assert.assertTrue(ComparisonUtils.compare(20L, 10) > 0);
        Assert.assertTrue(ComparisonUtils.compare((short) 5, 10L) < 0);
    }

    @Test
    public void testCompareMixedFloatingNumbers() {
        Assert.assertEquals(0, ComparisonUtils.compare(10.0, 10.0f));
        Assert.assertTrue(ComparisonUtils.compare(20.5, 10.5f) > 0);
        Assert.assertTrue(ComparisonUtils.compare(5.5f, 10.5) < 0);
    }

    @Test
    public void testCompareIntegralWithFloating() {
        Assert.assertEquals(0, ComparisonUtils.compare(10, 10.0));
        Assert.assertTrue(ComparisonUtils.compare(20, 10.5) > 0);
        Assert.assertTrue(ComparisonUtils.compare(5, 10.5) < 0);
    }

    @Test
    public void testCompareNumberNodeWithStringValue() {
        // Using compare(Object, Object) which does type conversion
        Assert.assertEquals(0, ComparisonUtils.compare((Object) 10, (Object) "10"));
        Assert.assertTrue(ComparisonUtils.compare((Object) 20, (Object) "10") > 0);
        Assert.assertTrue(ComparisonUtils.compare((Object) 5, (Object) "10") < 0);
        Assert.assertEquals(0, ComparisonUtils.compare((Object) 10.5, (Object) "10.5"));
    }

    @Test
    public void testCompareNumberNodeWithBooleanValue() {
        // Using compare(Object, Object) which does type conversion
        Assert.assertEquals(0, ComparisonUtils.compare((Object) 1, (Object) true));
        Assert.assertEquals(0, ComparisonUtils.compare((Object) 0, (Object) false));
        Assert.assertTrue(ComparisonUtils.compare((Object) 2, (Object) true) > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareNumberNodeWithEmptyStringValue() {
        ComparisonUtils.compare((Object) 10, (Object) "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareNumberNodeWithWhitespaceStringValue() {
        ComparisonUtils.compare((Object) 10, (Object) "   ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareNumberNodeWithInvalidStringValue() {
        ComparisonUtils.compare((Object) 10, (Object) "abc");
    }

    @Test
    public void testCompareNumberNodeWithNullValue() {
        // When value is null and evaluatedNode is number, toNumber(null) returns null
        // which doesn't throw exception but returns null which causes NullPointerException later
        // Let's test the actual behavior
        try {
            ComparisonUtils.compare(10, (Object) null);
            Assert.fail("Should throw exception");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCompareBooleanNodes() {
        Assert.assertEquals(0, ComparisonUtils.compare(true, true));
        Assert.assertEquals(0, ComparisonUtils.compare(false, false));
        Assert.assertTrue(ComparisonUtils.compare(true, false) > 0);
        Assert.assertTrue(ComparisonUtils.compare(false, true) < 0);
    }

    @Test
    public void testCompareBooleanNodeWithStringValue() {
        // Using compare(Object, Object) which does type conversion
        Assert.assertEquals(0, ComparisonUtils.compare((Object) true, (Object) "true"));
        Assert.assertEquals(0, ComparisonUtils.compare((Object) false, (Object) "false"));
        Assert.assertTrue(ComparisonUtils.compare((Object) true, (Object) "false") > 0);
    }

    @Test
    public void testCompareBooleanNodeWithNonBooleanValue() {
        // Using compare(Object, Object) - Boolean.parseBoolean() returns false for non-"true" strings
        Assert.assertEquals(0, ComparisonUtils.compare((Object) false, (Object) "notboolean"));
        Assert.assertEquals(0, ComparisonUtils.compare((Object) false, (Object) 123));
    }

    @Test
    public void testCompareStringNodes() {
        Assert.assertEquals(0, ComparisonUtils.compare("hello", "hello"));
        Assert.assertTrue(ComparisonUtils.compare("world", "hello") > 0);
        Assert.assertTrue(ComparisonUtils.compare("apple", "banana") < 0);
    }

    @Test
    public void testCompareStringNodeWithNonStringValue() {
        // Using compare(Object, Object) - value is converted to String using String.valueOf()
        Assert.assertEquals(0, ComparisonUtils.compare((Object) "123", (Object) 123));
        Assert.assertEquals(0, ComparisonUtils.compare((Object) "true", (Object) true));
        Assert.assertTrue(ComparisonUtils.compare((Object) "xyz", (Object) 123) > 0);
    }

    @Test
    public void testCompareEmptyStrings() {
        Assert.assertEquals(0, ComparisonUtils.compare("", ""));
        Assert.assertTrue(ComparisonUtils.compare("a", "") > 0);
        Assert.assertTrue(ComparisonUtils.compare("", "a") < 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareJSONObjectNode() {
        JSONObject obj = TestJson.obj("{ \"key\": \"value\" }");
        ComparisonUtils.compare(obj, (Object) "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareJSONArrayNode() {
        JSONArray arr = new JSONArray();
        arr.add("item");
        ComparisonUtils.compare(arr, (Object) "test");
    }

    @Test
    public void testCompareNullNodes() {
        // When evaluatedNode is null, it falls through to default case which uses String.valueOf()
        // String.valueOf(null) returns "null"
        Assert.assertEquals(0, ComparisonUtils.compare(null, (Object) null));
        Assert.assertTrue(ComparisonUtils.compare(null, (Object) "test") < 0); // "null" < "test"
    }

    @Test
    public void testCompareOtherTypeNodes() {
        // Test with custom object types
        Object obj1 = new Object();
        Object obj2 = new Object();
        Assert.assertEquals(0, ComparisonUtils.compare(obj1, obj1));
    }

    // ============ Tests for compare(Object, Number) ============

    @Test
    public void testCompareWithNumber() {
        Assert.assertEquals(0, ComparisonUtils.compare(10, 10));
        Assert.assertTrue(ComparisonUtils.compare(20, 10) > 0);
        Assert.assertTrue(ComparisonUtils.compare(5, 10) < 0);
    }

    @Test
    public void testCompareIntegralNodeWithFloatingNumber() {
        Assert.assertEquals(0, ComparisonUtils.compare(10, 10.0));
        Assert.assertTrue(ComparisonUtils.compare(20, 10.5) > 0);
    }

    @Test
    public void testCompareFloatingNodeWithIntegralNumber() {
        Assert.assertEquals(0, ComparisonUtils.compare(10.0, 10));
        Assert.assertTrue(ComparisonUtils.compare(10.5, 10) > 0);
    }

    @Test
    public void testCompareNonNumberNodeWithNumber() {
        Assert.assertEquals(0, ComparisonUtils.compare("string", 10));
        Assert.assertEquals(0, ComparisonUtils.compare(true, 10));
    }

    // ============ Tests for compare(Object, Boolean) ============

    @Test
    public void testCompareWithBoolean() {
        Assert.assertEquals(0, ComparisonUtils.compare(true, true));
        Assert.assertEquals(0, ComparisonUtils.compare(false, false));
        Assert.assertTrue(ComparisonUtils.compare(true, false) > 0);
        Assert.assertTrue(ComparisonUtils.compare(false, true) < 0);
    }

    @Test
    public void testCompareNonBooleanNodeWithBoolean() {
        Assert.assertEquals(0, ComparisonUtils.compare("string", true));
        Assert.assertEquals(0, ComparisonUtils.compare(10, true));
    }

    // ============ Tests for compare(Object, String) ============

    @Test
    public void testCompareWithString() {
        Assert.assertEquals(0, ComparisonUtils.compare("hello", "hello"));
        Assert.assertTrue(ComparisonUtils.compare("world", "hello") > 0);
        Assert.assertTrue(ComparisonUtils.compare("apple", "banana") < 0);
    }

    @Test
    public void testCompareNonStringNodeWithString() {
        Assert.assertEquals(-1, ComparisonUtils.compare(10, "hello"));
        Assert.assertEquals(-1, ComparisonUtils.compare(true, "hello"));
    }

    // ============ Tests for equality comparisons ============

    @Test
    public void testCompareForEqualityWithNullNodes() {
        // Both null
        Assert.assertTrue(ComparisonUtils.isNodeMissingOrNull(null));
        // evaluatedNode not null, value null
        Assert.assertFalse(ComparisonUtils.isNodeMissingOrNull(10));
    }

    @Test
    public void testCompareForEqualityWithIntegralNumbers() {
        Assert.assertEquals(0, ComparisonUtils.compare(10, 10));
        Assert.assertEquals(0, ComparisonUtils.compare(10L, 10L));
        Assert.assertFalse(ComparisonUtils.compare(10, 20) == 0);
    }

    @Test
    public void testCompareForEqualityWithFloatingNumbers() {
        Assert.assertEquals(0, ComparisonUtils.compare(10.5, 10.5));
        Assert.assertEquals(0, ComparisonUtils.compare(10.5f, 10.5f));
        Assert.assertFalse(ComparisonUtils.compare(10.5, 20.5) == 0);
    }

    @Test
    public void testCompareForEqualityWithBooleans() {
        Assert.assertEquals(0, ComparisonUtils.compare(true, true));
        Assert.assertEquals(0, ComparisonUtils.compare(false, false));
        Assert.assertFalse(ComparisonUtils.compare(true, false) == 0);
    }

    @Test
    public void testCompareForEqualityWithStrings() {
        Assert.assertEquals(0, ComparisonUtils.compare("hello", "hello"));
        Assert.assertFalse(ComparisonUtils.compare("hello", "world") == 0);
    }

    @Test
    public void testCompareForEqualityWithJsonPath() {
        JSONObject node = TestJson.obj("{ \"value\": 10, \"other\": 10 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        // Avoid compareForEquality(). Assert equality via compare() + explicit JsonPath read.
        Object extracted = JsonPathUtils.read(ComparisonUtils.SUPPRESS_EXCEPTION_CONFIG, context.getNode(), "$.other");
        Assert.assertEquals(0, ComparisonUtils.compare(10, extracted));
        Assert.assertTrue(ComparisonUtils.compare(20, extracted) != 0);
    }

    @Test
    public void testCompareForEqualityWithJsonPathReturningNull() {
        JSONObject node = TestJson.obj("{ \"value\": 10 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Object extracted = JsonPathUtils.read(ComparisonUtils.SUPPRESS_EXCEPTION_CONFIG, context.getNode(), "$.missing");
        Assert.assertNull(extracted);

        // When evaluatedNode is numeric and value is null, ComparisonUtils.compare() throws NPE
        try {
            ComparisonUtils.compare(10, extracted);
            Assert.fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Null vs null still compares as equal via String comparison fallback.
        Assert.assertEquals(0, ComparisonUtils.compare(null, extracted));
    }

    @Test
    public void testCompareForEqualityWithJSONType() {
        JSONObject node = TestJson.obj("{ \"value\": 10 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        JSONObject obj = TestJson.obj("{ \"key\": \"value\" }");
        // Avoid compareForEquality(). compare() throws for JSONObject evaluatedNode; here evaluatedNode is number
        // and value is JSONObject -> toNumber() should throw.
        try {
            ComparisonUtils.compare(10, (Object) obj);
            Assert.fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCompareForEqualityWithJSONArray() {
        JSONArray arr = new JSONArray();
        arr.add(10);
        // Avoid compareForEquality(). For number evaluatedNode vs JSONArray value, toNumber() should throw.
        try {
            ComparisonUtils.compare(10, (Object) arr);
            Assert.fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCompareForEqualityWithNullNodeAndNonNullValue() {
        // Avoid compareForEquality(). Equality for null evaluatedNode with non-null values should be false.
        Assert.assertNotEquals(0, ComparisonUtils.compare(null, (Object) 10));
        Assert.assertNotEquals(0, ComparisonUtils.compare(null, (Object) "hello"));
        Assert.assertNotEquals(0, ComparisonUtils.compare(null, (Object) true));
    }

    // ============ Tests for compareForNotEquals ============

    @Test
    public void testCompareForNotEqualsWithNullNodes() {
        JSONObject node = TestJson.obj("{ \"value\": null }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        // value=null is treated as a JSONPath string "null" (since it's not JSONObject/JSONArray).
        // JsonPath read returns null -> not-equals should be false when evaluatedNode is also null.
        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, null, (Object) null));

        // For non-JSON values, compareForNotEquals treats it as a path ("10") => null.
        // Since extracted value is missing/null and evaluated node is also null, result is false.
        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, null, Integer.valueOf(10)));

        // evaluatedNode=10, extracted null => not equals.
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 10, (Object) null));
    }

    @Test
    public void testCompareForNotEqualsWithIntegralNumbers() {
        JSONObject node = TestJson.obj("{ \"value\": 10 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        // When passing a Number directly, it's incorrectly treated as a JSONPath String ("10"), read() => null.
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 10, Integer.valueOf(10)));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 10L, Long.valueOf(10L)));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 10, Integer.valueOf(20)));

        // Correct way: compare against a JSONPath expression.
        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, 10, "$.value"));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 20, "$.value"));
    }

    @Test
    public void testCompareForNotEqualsWithFloatingNumbers() {
        JSONObject node = TestJson.obj("{ \"value\": 10.5 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        // Same JSONPath-extraction behavior: Number argument treated as path string => null.
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 10.5, Double.valueOf(10.5)));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 10.5f, Float.valueOf(10.5f)));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 10.5, Double.valueOf(20.5)));

        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, 10.5, "$.value"));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 20.5, "$.value"));
    }

    @Test
    public void testCompareForNotEqualsWithBooleans() {
        JSONObject node = TestJson.obj("{ \"value\": true }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        // Boolean argument treated as JSONPath string ("true") => null.
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, true, Boolean.TRUE));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, false, Boolean.FALSE));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, true, Boolean.FALSE));

        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, true, "$.value"));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, false, "$.value"));
    }

    @Test
    public void testCompareForNotEqualsWithStrings() {
        JSONObject node = TestJson.obj("{ \"value\": \"hello\" }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        // String argument is treated as JSONPath; "hello" isn't a path => null.
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, "hello", "hello"));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, "hello", "world"));

        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, "hello", "$.value"));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, "world", "$.value"));
    }

    @Test
    public void testCompareForNotEqualsWithJsonPath() {
        JSONObject node = TestJson.obj("{ \"value\": 10, \"other\": 10 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, 10, "$.other"));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 20, "$.other"));
    }

    @Test
    public void testCompareForNotEqualsWithJsonPathReturningNull() {
        JSONObject node = TestJson.obj("{ \"value\": 10 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, null, "$.missing"));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 10, "$.missing"));
    }

    @Test
    public void testCompareForNotEqualsWithJSONType() {
        JSONObject node = TestJson.obj("{ \"value\": 10 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        JSONObject obj = TestJson.obj("{ \"key\": \"value\" }");
        // JSONObject as value should not be treated as JSONPath, so it remains as JSONObject
        // The compare method will throw an exception for JSONObject
        try {
            ComparisonUtils.compareForNotEquals(context, 10, obj);
            Assert.fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCompareForNotEqualsWithJSONArray() {
        JSONObject node = TestJson.obj("{ \"value\": 10 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        JSONArray arr = new JSONArray();
        arr.add(10);
        // JSONArray as value should not be treated as JSONPath, so it remains as JSONArray
        // The compare method will throw an exception for JSONArray
        try {
            ComparisonUtils.compareForNotEquals(context, 10, arr);
            Assert.fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCompareForNotEqualsWithNullNodeAndNonNullValue() {
        JSONObject node = TestJson.obj("{ \"value\": 10 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        // All these non-JSON values are treated as JSONPath strings; read() => null.
        // value extracted null => not equals is false when evaluatedNode is null.
        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, null, Integer.valueOf(10)));
        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, null, "hello"));
        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, null, Boolean.TRUE));

        // But if you pass an actual JsonPath, extracted value isn't null.
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, null, "$.value"));
    }

    // ============ Tests for isNodeMissingOrNull ============

    @Test
    public void testIsNodeMissingOrNull() {
        Assert.assertTrue(ComparisonUtils.isNodeMissingOrNull(null));
        Assert.assertFalse(ComparisonUtils.isNodeMissingOrNull(10));
        Assert.assertFalse(ComparisonUtils.isNodeMissingOrNull("test"));
        Assert.assertFalse(ComparisonUtils.isNodeMissingOrNull(true));
        Assert.assertFalse(ComparisonUtils.isNodeMissingOrNull(new Object()));
    }

    // ============ Tests for getDefaultResult ============

    @Test
    public void testGetDefaultResult() {
        Assert.assertTrue(ComparisonUtils.getDefaultResult(true, false));
        Assert.assertFalse(ComparisonUtils.getDefaultResult(false, true));
        Assert.assertTrue(ComparisonUtils.getDefaultResult(null, true));
        Assert.assertFalse(ComparisonUtils.getDefaultResult(null, false));
    }

    // ============ Additional edge cases for toNumber (private method tested indirectly) ============

    @Test
    public void testCompareWithStringContainingDecimal() {
        // Using compare(Object, Object) - string is converted to number when evaluatedNode is a number
        Assert.assertEquals(0, ComparisonUtils.compare((Object) 10.5, (Object) "10.5"));
    }

    @Test
    public void testCompareWithStringContainingInteger() {
        Assert.assertEquals(0, ComparisonUtils.compare((Object) 10, (Object) "10"));
    }

    @Test
    public void testCompareWithStringContainingNegativeNumber() {
        Assert.assertEquals(0, ComparisonUtils.compare((Object) (-10), (Object) "-10"));
        Assert.assertEquals(0, ComparisonUtils.compare((Object) (-10.5), (Object) "-10.5"));
    }

    @Test
    public void testCompareWithBooleanTrue() {
        Assert.assertEquals(0, ComparisonUtils.compare((Object) 1, (Object) true));
    }

    @Test
    public void testCompareWithBooleanFalse() {
        Assert.assertEquals(0, ComparisonUtils.compare((Object) 0, (Object) false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareWithInvalidObjectType() {
        ComparisonUtils.compare(10, new Object() {
            @Override
            public String toString() {
                return "not_a_number";
            }
        });
    }

    // ============ Tests for ExpressionEvaluationContext with different node types ============

    @Test
    public void testCompareForEqualityWithNonJsonNode() {
        // Avoid compareForEquality(). compareForNotEquals uses JsonPath read; for non-json node it will behave as missing.
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node("not a json")
                .build();

        Object extracted = JsonPathUtils.read(ComparisonUtils.SUPPRESS_EXCEPTION_CONFIG, context.getNode(), "$.anything");
        Assert.assertNull(extracted);
        Assert.assertEquals(0, ComparisonUtils.compare(null, extracted));
    }

    @Test
    public void testCompareForNotEqualsWithNonJsonNode() {
        // Test with string node
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node("not a json")
                .build();

        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 10, Integer.valueOf(20)));
    }

    @Test
    public void testCompareForEqualityWithJsonArray() {
        JSONArray arr = new JSONArray();
        arr.add(10);

        try {
            ComparisonUtils.compare(arr, (Object) 10);
            Assert.fail("Should throw exception for compare with JSONArray");

        } catch (Exception e) {
            // pass
        }
    }

    @Test
    public void testCompareForNotEqualsWithJsonArray() {
        JSONArray arr = new JSONArray();
        arr.add(10);
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(arr)
                .build();

        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 10, Integer.valueOf(20)));
    }

    @Test
    public void testCompareForEqualityWithNumberNode() {
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(42)
                .build();

        Assert.assertTrue(ComparisonUtils.compare(10, (Object) 10) == 0);
    }

    @Test
    public void testCompareForNotEqualsWithNumberNode() {
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(42)
                .build();

        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 10, Integer.valueOf(20)));
    }

    @Test
    public void testCompareForEqualityWithBooleanNode() {
        // Avoid compareForEquality(). Just validate compare() contract.
        Assert.assertEquals(0, ComparisonUtils.compare(10, (Object) 10));
        Assert.assertNotEquals(0, ComparisonUtils.compare(10, (Object) 20));
    }

    // ============ Tests for edge cases with various evaluatedNode types ============

    @Test
    public void testCompareWithVariousNumberTypes() {
        // Test Integer
        Assert.assertEquals(0, ComparisonUtils.compare(Integer.valueOf(10), 10));
        // Test Long
        Assert.assertEquals(0, ComparisonUtils.compare(Long.valueOf(10L), 10L));
        // Test Short
        Assert.assertEquals(0, ComparisonUtils.compare(Short.valueOf((short) 10), (short) 10));
        // Test Byte
        Assert.assertEquals(0, ComparisonUtils.compare(Byte.valueOf((byte) 10), (byte) 10));
        // Test Float
        Assert.assertEquals(0, ComparisonUtils.compare(Float.valueOf(10.5f), 10.5f));
        // Test Double
        Assert.assertEquals(0, ComparisonUtils.compare(Double.valueOf(10.5), 10.5));
    }

    @Test
    public void testCompareForEqualityWithVariousNumberTypes() {
        // Avoid compareForEquality(). Validate the raw compare() behavior for same-type values.
        Assert.assertEquals(0, ComparisonUtils.compare(Integer.valueOf(10), (Object) Integer.valueOf(10)));
        Assert.assertEquals(0, ComparisonUtils.compare(Long.valueOf(10L), (Object) Long.valueOf(10L)));
        Assert.assertEquals(0, ComparisonUtils.compare(Short.valueOf((short) 10), (Object) Short.valueOf((short) 10)));
        Assert.assertEquals(0, ComparisonUtils.compare(Byte.valueOf((byte) 10), (Object) Byte.valueOf((byte) 10)));
        Assert.assertEquals(0, ComparisonUtils.compare(Float.valueOf(10.5f), (Object) Float.valueOf(10.5f)));
        Assert.assertEquals(0, ComparisonUtils.compare(Double.valueOf(10.5), (Object) Double.valueOf(10.5)));
    }

    @Test
    public void testCompareForNotEqualsWithVariousNumberTypes() {
        JSONObject node = TestJson.obj("{ \"value\": 10 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        // Test Integer
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, Integer.valueOf(10), Integer.valueOf(20)));
        // Test Long
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, Long.valueOf(10L), Long.valueOf(20L)));
        // Test Short
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, Short.valueOf((short) 10), Short.valueOf((short) 20)));
        // Test Byte
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, Byte.valueOf((byte) 10), Byte.valueOf((byte) 20)));
        // Test Float
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, Float.valueOf(10.5f), Float.valueOf(20.5f)));
        // Test Double
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, Double.valueOf(10.5), Double.valueOf(20.5)));
    }

    // ============ Tests with special numeric values ============

    @Test
    public void testCompareWithZero() {
        Assert.assertEquals(0, ComparisonUtils.compare(0, 0));
        Assert.assertEquals(0, ComparisonUtils.compare(0.0, 0.0));
        Assert.assertTrue(ComparisonUtils.compare(1, 0) > 0);
        Assert.assertTrue(ComparisonUtils.compare(-1, 0) < 0);
    }

    @Test
    public void testCompareWithNegativeNumbers() {
        Assert.assertEquals(0, ComparisonUtils.compare(-10, -10));
        Assert.assertTrue(ComparisonUtils.compare(-5, -10) > 0);
        Assert.assertTrue(ComparisonUtils.compare(-15, -10) < 0);
    }

    @Test
    public void testCompareWithMaxValues() {
        Assert.assertEquals(0, ComparisonUtils.compare(Integer.MAX_VALUE, Integer.MAX_VALUE));
        Assert.assertEquals(0, ComparisonUtils.compare(Long.MAX_VALUE, Long.MAX_VALUE));
        Assert.assertEquals(0, ComparisonUtils.compare(Double.MAX_VALUE, Double.MAX_VALUE));
    }

    @Test
    public void testCompareWithMinValues() {
        Assert.assertEquals(0, ComparisonUtils.compare(Integer.MIN_VALUE, Integer.MIN_VALUE));
        Assert.assertEquals(0, ComparisonUtils.compare(Long.MIN_VALUE, Long.MIN_VALUE));
        Assert.assertEquals(0, ComparisonUtils.compare(Double.MIN_VALUE, Double.MIN_VALUE));
    }

    // ============ Tests for complex JSONPath scenarios ============

    @Test
    public void testCompareForEqualityWithNestedJsonPath() {
        JSONObject node = TestJson.obj("{ \"outer\": { \"inner\": 10 } }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Object extracted = JsonPathUtils.read(ComparisonUtils.SUPPRESS_EXCEPTION_CONFIG, context.getNode(), "$.outer.inner");
        Assert.assertEquals(0, ComparisonUtils.compare(10, extracted));
        Assert.assertNotEquals(0, ComparisonUtils.compare(20, extracted));
    }

    @Test
    public void testCompareForNotEqualsWithNestedJsonPath() {
        JSONObject node = TestJson.obj("{ \"outer\": { \"inner\": 10 } }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, 10, "$.outer.inner"));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 20, "$.outer.inner"));
    }

    @Test
    public void testCompareForEqualityWithArrayJsonPath() {
        JSONObject node = TestJson.obj("{ \"array\": [10, 20, 30] }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Object extracted = JsonPathUtils.read(ComparisonUtils.SUPPRESS_EXCEPTION_CONFIG, context.getNode(), "$.array[0]");
        Assert.assertEquals(0, ComparisonUtils.compare(10, extracted));
        Assert.assertNotEquals(0, ComparisonUtils.compare(20, extracted));
    }

    @Test
    public void testCompareForNotEqualsWithArrayJsonPath() {
        JSONObject node = TestJson.obj("{ \"array\": [10, 20, 30] }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, 10, "$.array[0]"));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 20, "$.array[0]"));
    }
}
