package io.appform.jsonrules.benchmarks;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.appform.jsonrules.Expression;
import io.appform.jsonrules.ExpressionEvaluationContext;
import io.appform.jsonrules.config.JsonRulesConfiguration;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Threads(value = 5)
@Timeout(time = 5)
@Fork
@Warmup(iterations = 10, time = 5)
@Measurement(iterations = 3, time = 5)
@BenchmarkMode(value = Mode.Throughput)
public class ExpressionEvaluationBenchmark {
    @org.openjdk.jmh.annotations.State(value = Scope.Benchmark)
    public static class State {
        private Expression expression;
        private ExpressionEvaluationContext context;

        public State() {
            try {
                // Load expression using fastjson2
                String expressionJson = readResourceAsString("/expression.json");
                expression = JSON.parseObject(expressionJson, Expression.class);

                // Load collection data using fastjson2
                String collectionJson = readResourceAsString("/collection.json");
                JSONObject jsonNode = JSON.parseObject(collectionJson);

                context = ExpressionEvaluationContext.builder()
                        .node(jsonNode)
                        .options(new HashMap<>())
                        .build();
                JsonRulesConfiguration.configure(JsonRulesConfiguration.PerformanceSafetyPreference.SPEED);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private String readResourceAsString(String resourcePath) throws Exception {
            try (InputStream is = ExpressionEvaluationBenchmark.class.getResourceAsStream(resourcePath)) {
                if (is == null) {
                    throw new IllegalArgumentException("Resource not found: " + resourcePath);
                }
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
        }
    }

    @Benchmark
    public void evaluate(State state, Blackhole bh) {
        bh.consume(state.expression.evaluate(state.context));
    }
}
