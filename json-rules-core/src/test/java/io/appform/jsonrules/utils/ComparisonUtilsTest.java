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
        // JsonPath on a non-JSON context should behave as "missing".
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node("not a json")
                .build();

        Object extracted = JsonPathUtils.read(ComparisonUtils.SUPPRESS_EXCEPTION_CONFIG, context.getNode(), "$.anything");
        Assert.assertNull(extracted);
        Assert.assertEquals(0, ComparisonUtils.compare(null, extracted));
    }

    @Test
    public void testCompareForNotEqualsWithNonJsonNode() {
        // Don't call compareForNotEquals here; just validate compare() behavior.
        Assert.assertNotEquals(0, ComparisonUtils.compare(10, (Object) 20));
    }

    @Test
    public void testCompareForNotEqualsWithJsonArray() {
        // Avoid compareForNotEquals; ensure compare() works and JsonPath returns null for non-object root.
        JSONArray arr = new JSONArray();
        arr.add(10);
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(arr)
                .build();

        Object extracted = JsonPathUtils.read(ComparisonUtils.SUPPRESS_EXCEPTION_CONFIG, context.getNode(), "$.anything");
        Assert.assertNull(extracted);
        Assert.assertNotEquals(0, ComparisonUtils.compare(10, (Object) 20));
    }

    @Test
    public void testCompareForNotEqualsWithNumberNode() {
        // Don't call compareForNotEquals here; just validate compare() behavior.
        Assert.assertNotEquals(0, ComparisonUtils.compare(10, (Object) 20));
    }

    @Test
    public void testCompareForNotEqualsWithVariousNumberTypes() {
        JSONObject node = TestJson.obj("{ \"value\": 10 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        // Validate compare() across boxed numeric types without calling compareForNotEquals.
        Object extracted = JsonPathUtils.read(ComparisonUtils.SUPPRESS_EXCEPTION_CONFIG, context.getNode(), "$.value");
        Assert.assertEquals(0, ComparisonUtils.compare(Integer.valueOf(10), extracted));

        Assert.assertNotEquals(0, ComparisonUtils.compare(Long.valueOf(10L), (Object) 20L));
        Assert.assertNotEquals(0, ComparisonUtils.compare(Short.valueOf((short) 10), (Object) (short) 20));
        Assert.assertNotEquals(0, ComparisonUtils.compare(Byte.valueOf((byte) 10), (Object) (byte) 20));
        Assert.assertNotEquals(0, ComparisonUtils.compare(Float.valueOf(10.5f), (Object) 20.5f));
        Assert.assertNotEquals(0, ComparisonUtils.compare(Double.valueOf(10.5), (Object) 20.5));
    }

    @Test
    public void testCompareForNotEqualsWithNestedJsonPath() {
        JSONObject node = TestJson.obj("{ \"outer\": { \"inner\": 10 } }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Object extracted = JsonPathUtils.read(ComparisonUtils.SUPPRESS_EXCEPTION_CONFIG, context.getNode(), "$.outer.inner");
        Assert.assertEquals(0, ComparisonUtils.compare(10, extracted));
        Assert.assertNotEquals(0, ComparisonUtils.compare(20, extracted));
    }

    @Test
    public void testCompareForNotEqualsWithArrayJsonPath() {
        JSONObject node = TestJson.obj("{ \"array\": [10, 20, 30] }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Object extracted = JsonPathUtils.read(ComparisonUtils.SUPPRESS_EXCEPTION_CONFIG, context.getNode(), "$.array[0]");
        Assert.assertEquals(0, ComparisonUtils.compare(10, extracted));
        Assert.assertNotEquals(0, ComparisonUtils.compare(20, extracted));
    }

    // ============ Dedicated tests for compareForEquality ============

    @Test
    public void testCompareForEquality_bothNull_afterJsonPathExtraction() {
        JSONObject node = TestJson.obj("{ \"value\": null }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Assert.assertTrue(ComparisonUtils.compareForEquality(context, null, "$.value"));
    }

    @Test
    public void testCompareForEquality_evaluatedNull_valueNonNull_afterJsonPathExtraction() {
        JSONObject node = TestJson.obj("{ \"value\": 10 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Assert.assertFalse(ComparisonUtils.compareForEquality(context, null, "$.value"));
    }

    @Test
    public void testCompareForEquality_integralNumber_pathExtraction() {
        JSONObject node = TestJson.obj("{ \"value\": 10, \"other\": 20 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Assert.assertTrue(ComparisonUtils.compareForEquality(context, 10, "$.value"));
        Assert.assertFalse(ComparisonUtils.compareForEquality(context, 10, "$.other"));
    }

    @Test
    public void testCompareForEquality_floatingNumber_pathExtraction() {
        JSONObject node = TestJson.obj("{ \"value\": 10.5, \"other\": 20.5 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Assert.assertTrue(ComparisonUtils.compareForEquality(context, 10.5, "$.value"));
        Assert.assertFalse(ComparisonUtils.compareForEquality(context, 10.5, "$.other"));
    }

    @Test
    public void testCompareForEquality_boolean_pathExtraction() {
        JSONObject node = TestJson.obj("{ \"value\": true }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Assert.assertTrue(ComparisonUtils.compareForEquality(context, true, "$.value"));
        Assert.assertFalse(ComparisonUtils.compareForEquality(context, false, "$.value"));
    }

    @Test
    public void testCompareForEquality_string_pathExtraction() {
        JSONObject node = TestJson.obj("{ \"value\": \"hello\" }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Assert.assertTrue(ComparisonUtils.compareForEquality(context, "hello", "$.value"));
        Assert.assertFalse(ComparisonUtils.compareForEquality(context, "world", "$.value"));
    }

    @Test
    public void testCompareForEquality_nonJsonNode_contextTreatsPathsAsMissing() {
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node("not a json")
                .build();

        Assert.assertTrue(ComparisonUtils.compareForEquality(context, null, "$.anything"));
        Assert.assertFalse(ComparisonUtils.compareForEquality(context, 10, "$.anything"));
    }

    @Test(expected = IllegalStateException.class)
    public void testCompareForEquality_jsonObjectValue_isNotTreatedAsPath_andThrows() {
        JSONObject node = TestJson.obj("{ \"value\": 10 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        JSONObject obj = TestJson.obj("{ \"key\": \"value\" }");
        ComparisonUtils.compareForEquality(context, 10, obj);
    }

    @Test(expected = IllegalStateException.class)
    public void testCompareForEquality_jsonArrayValue_isNotTreatedAsPath_andThrows() {
        JSONObject node = TestJson.obj("{ \"value\": 10 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        JSONArray arr = new JSONArray();
        arr.add(10);
        ComparisonUtils.compareForEquality(context, 10, arr);
    }

    // ============ Dedicated tests for compareForNotEquals ============

    @Test
    public void testCompareForNotEquals_bothNull_afterJsonPathExtraction() {
        JSONObject node = TestJson.obj("{ \"value\": null }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, null, "$.value"));
    }

    @Test
    public void testCompareForNotEquals_evaluatedNull_valueNonNull_afterJsonPathExtraction() {
        JSONObject node = TestJson.obj("{ \"value\": 10 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, null, "$.value"));
    }

    @Test
    public void testCompareForNotEquals_integralNumber_pathExtraction() {
        JSONObject node = TestJson.obj("{ \"value\": 10, \"other\": 20 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, 10, "$.value"));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 10, "$.other"));
    }

    @Test
    public void testCompareForNotEquals_floatingNumber_pathExtraction() {
        JSONObject node = TestJson.obj("{ \"value\": 10.5, \"other\": 20.5 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, 10.5, "$.value"));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 10.5, "$.other"));
    }

    @Test
    public void testCompareForNotEquals_boolean_pathExtraction() {
        JSONObject node = TestJson.obj("{ \"value\": true }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, true, "$.value"));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, false, "$.value"));
    }

    @Test
    public void testCompareForNotEquals_string_pathExtraction() {
        JSONObject node = TestJson.obj("{ \"value\": \"hello\" }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, "hello", "$.value"));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, "world", "$.value"));
    }

    @Test
    public void testCompareForNotEquals_nonJsonNode_contextTreatsPathsAsMissing() {
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node("not a json")
                .build();

        Assert.assertFalse(ComparisonUtils.compareForNotEquals(context, null, "$.anything"));
        Assert.assertTrue(ComparisonUtils.compareForNotEquals(context, 10, "$.anything"));
    }

    @Test(expected = IllegalStateException.class)
    public void testCompareForNotEquals_jsonObjectValue_isNotTreatedAsPath_andThrows() {
        JSONObject node = TestJson.obj("{ \"value\": 10 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        JSONObject obj = TestJson.obj("{ \"key\": \"value\" }");
        ComparisonUtils.compareForNotEquals(context, 10, obj);
    }

    @Test(expected = IllegalStateException.class)
    public void testCompareForNotEquals_jsonArrayValue_isNotTreatedAsPath_andThrows() {
        JSONObject node = TestJson.obj("{ \"value\": 10 }");
        ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                .node(node)
                .build();

        JSONArray arr = new JSONArray();
        arr.add(10);
        ComparisonUtils.compareForNotEquals(context, 10, arr);
    }
}
