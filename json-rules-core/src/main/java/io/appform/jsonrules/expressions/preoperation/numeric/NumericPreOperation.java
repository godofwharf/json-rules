/**
 * Copyright (c) 2017 Mohammed Irfanulla S <mohammed.irfanulla.s1@gmail.com>
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

package io.appform.jsonrules.expressions.preoperation.numeric;

import io.appform.jsonrules.ExpressionEvaluationContext;
import io.appform.jsonrules.expressions.preoperation.PreOperation;
import io.appform.jsonrules.expressions.preoperation.PreOperationType;
import io.appform.jsonrules.utils.PreOperationUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * All numeric pre-operations
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class NumericPreOperation extends PreOperation<Number> {
	private Number operand;
	
	public NumericPreOperation(PreOperationType type) {
		super(type);
	}
	
	public NumericPreOperation(PreOperationType type, Number operand) {
		this(type);
		this.operand = operand;
	}

	public Number compute(Object evaluatedNode) {
		if (evaluatedNode instanceof Number) {
			Number nodeValue = (Number) evaluatedNode;
			boolean nodeIsIntegral = (nodeValue instanceof Integer || nodeValue instanceof Long ||
			                          nodeValue instanceof Short || nodeValue instanceof Byte);

			if (nodeIsIntegral) {
				return compute(nodeValue.longValue(), operand.longValue());
			} else {
				return compute(nodeValue.doubleValue(), operand.doubleValue());
			}
        } else if (PreOperationUtils.isNumericRepresentation(evaluatedNode)) {
            // For extending pre-operation to numbers represented as text.
            return compute(Double.parseDouble(evaluatedNode.toString()), operand.doubleValue());
        } else {
            throw new IllegalArgumentException("Non numeric operations are not supported");
        }
	}

	@Override
	public Number compute(ExpressionEvaluationContext context) {
		Object node = context.getNode();
		return compute(node);
	}

	public abstract long compute(long leftOperand, long rightOperand);
	
	public abstract double compute(double leftOperand, double rightOperand);

}
