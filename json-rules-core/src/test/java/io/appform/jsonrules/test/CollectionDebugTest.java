package io.appform.jsonrules.test;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Sets;
import io.appform.jsonrules.ExpressionEvaluationContext;
import io.appform.jsonrules.expressions.array.ContainsAnyExpression;
import io.appform.jsonrules.utils.TestJson;

public class CollectionDebugTest {
    public static void main(String[] args) {
        try {
            JSONObject node = TestJson.obj("{ \"felines\": [\"leopard\",\"lion\",\"tiger\",\"jaguar\"]}");

            System.out.println("Node: " + node);
            System.out.println("Felines: " + node.get("felines"));
            System.out.println("Felines class: " + node.get("felines").getClass());

            ExpressionEvaluationContext context = ExpressionEvaluationContext.builder()
                    .node(node)
                    .build();

            ContainsAnyExpression expr = ContainsAnyExpression.builder()
                    .path("$.felines")
                    .values(Sets.newHashSet("leopard","lion","panther"))
                    .defaultResult(false)
                    .build();

            System.out.println("\nExpression values: " + expr.getValues());
            System.out.println("Values class: " + expr.getValues().iterator().next().getClass());

            boolean result = expr.evaluate(context);
            System.out.println("\nResult: " + result + " (expected: true)");

            if (!result) {
                System.out.println("FAIL! Expected true but got false");
            } else {
                System.out.println("SUCCESS!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
