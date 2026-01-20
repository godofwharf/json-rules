package io.appform.jsonrules;

import com.alibaba.fastjson2.JSONObject;
import io.appform.jsonrules.expressions.composite.NotExpression;
import io.appform.jsonrules.expressions.composite.OrExpression;
import io.appform.jsonrules.expressions.numeric.GreaterThanExpression;
import io.appform.jsonrules.expressions.numeric.LessThanExpression;
import io.appform.jsonrules.utils.Rule;
import io.appform.jsonrules.utils.TestJson;
import io.appform.jsonrules.utils.TestUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test to check rule functionality
 */
public class RuleTest {
    @Test
    public void testRule() throws Exception {
        final String ruleRepr = TestUtils.read("/simple.rule");
        Rule rule = Rule.create(ruleRepr);
        JSONObject node = TestJson.obj("{ \"value\": 20, \"string\" : \"Hello\" }");
        Assert.assertTrue(rule.matches((Object) node));
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            if (!rule.matches((Object) node)) {
                System.err.println("Mismatch");
            }
        }
        System.out.println("Time taken: " + (System.currentTimeMillis() - currentTime));
    }

    @Test
    public void testDefaultResultRule() throws Exception {
        final String ruleRepr = TestUtils.read("/simple_rule_with_default.rule");
        Rule rule = Rule.create(ruleRepr);
        JSONObject nodeWithMissingOperandPath = TestJson.obj("{ \"value\": 20, \"name\" : \"Hello\" }");
        Assert.assertTrue(rule.matches((Object) nodeWithMissingOperandPath));

        final String ruleRepr2 = TestUtils.read("/simple.rule");
        Rule rule2 = Rule.create(ruleRepr2);
        Assert.assertFalse(rule2.matches((Object) nodeWithMissingOperandPath));

        final String ruleRepr3 = TestUtils.read("/complex.rule");
        Rule rule3 = Rule.create(ruleRepr3);
        Assert.assertTrue(rule3.matches((Object) nodeWithMissingOperandPath));
    }

    @Test
    public void testDefaultResultForNegativeRule() throws Exception {
        final String ruleRepr = TestUtils.read("/notExists.rule");
        Rule rule = Rule.create(ruleRepr);
        JSONObject nodeWithMissingOperandPath = TestJson.obj("{ \"value\": 20, \"name\" : \"Hello\" }");
        Assert.assertTrue(rule.matches((Object) nodeWithMissingOperandPath));

    }

    @Test
    @Ignore
    public void testPerf() throws Exception {
        final String ruleRepr = TestUtils.read("/complex.rule");
        Rule rule = Rule.create(ruleRepr);
        JSONObject node = TestJson.obj("{ \"value\": 20, \"string\" : \"Hello\" }");
        for (int j = 0; j < 10; j++) {
            long currentTime = System.currentTimeMillis();
            for (long i = 0; i < 10_000_000; i++) {
                if (!rule.matches((Object) node)) {
                    System.err.println("Mismatch");
                }
            }
            System.out.println("Time taken: " + (System.currentTimeMillis() - currentTime));
        }
    }

    @Test
    public void testRepresentation() throws Exception {
        Rule rule = new Rule(NotExpression.builder()
                .child(
                        OrExpression.builder()
                                .child(LessThanExpression.builder()
                                        .path("$.value")
                                        .value(11)
                                        .build())
                                .child(GreaterThanExpression.builder()
                                        .path("$.value")
                                        .value(30)
                                        .build())
                                .build())
                .build());

        final String ruleRep = rule.representation();
        System.out.println(ruleRep);

        // Deserialize and compare objects instead of string comparison
        Rule deserializedRule = Rule.create(ruleRep);
        Assert.assertEquals(rule, deserializedRule);
    }
}
