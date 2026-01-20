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

package io.appform.jsonrules.expressions.preoperation;

import com.alibaba.fastjson2.annotation.JSONType;
import io.appform.jsonrules.ExpressionEvaluationContext;
import io.appform.jsonrules.expressions.preoperation.array.SizeOperation;
import io.appform.jsonrules.expressions.preoperation.date.DateTimeOperation;
import io.appform.jsonrules.expressions.preoperation.date.DiffFromCurrentEpochOperation;
import io.appform.jsonrules.expressions.preoperation.date.EpochOperation;
import io.appform.jsonrules.expressions.preoperation.numeric.*;
import io.appform.jsonrules.expressions.preoperation.string.LengthOperation;
import io.appform.jsonrules.expressions.preoperation.string.SubStringOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * A base operation
 */
@Data
@EqualsAndHashCode
@ToString
@JSONType(typeKey = "operation", seeAlso = {
        AddOperation.class,
        SubtractOperation.class,
        MultiplyOperation.class,
        DivideOperation.class,
        ModuloOperation.class,
        SizeOperation.class,
        LengthOperation.class,
        SubStringOperation.class,
        EpochOperation.class,
        DateTimeOperation.class,
        DiffFromCurrentEpochOperation.class
})
public abstract class PreOperation<T> {
	private final PreOperationType operation;
	
	protected PreOperation(PreOperationType operation) {
		this.operation = operation;
	}

	public abstract T compute(ExpressionEvaluationContext context);
}
