package io.appform.jsonrules.expressions.preoperation.date;

import com.alibaba.fastjson2.annotation.JSONType;
import io.appform.jsonrules.ExpressionEvaluationContext;
import io.appform.jsonrules.OptionKeys;
import io.appform.jsonrules.expressions.preoperation.PreOperation;
import io.appform.jsonrules.expressions.preoperation.PreOperationType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by gabber12 on 24/07/17.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JSONType(typeName = "current_epoch_diff")
public class DiffFromCurrentEpochOperation extends PreOperation<Number> {
    protected DiffFromCurrentEpochOperation() {
        super(PreOperationType.current_epoch_diff);
    }


    private Number compute(Object evaluatedNode, long currentEpoch) {
        if (evaluatedNode instanceof Long || evaluatedNode instanceof Integer) {
            return currentEpoch - ((Number) evaluatedNode).longValue();
        }
        throw new IllegalArgumentException("Evaluated node does not represent a valid epoch");
    }

    @Override
    public Number compute(ExpressionEvaluationContext context) {
        try {
            final long currentEpoch = (long) (context.getOptions()
                    .getOrDefault(OptionKeys.SYSTEM_TIME, System.currentTimeMillis()));
            final Object node = context.getNode();
            return compute(node, currentEpoch);
        } catch (Exception e) {
            throw new IllegalArgumentException("Operands does not represent a valid epoch", e);
        }
    }
}
