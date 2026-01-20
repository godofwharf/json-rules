/**
 * Copyright (c) 2017 Mohammed Irfanulla S <mohammed.irfanulla.s1@gmail.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.appform.jsonrules.expressions.preoperation.date;

import com.alibaba.fastjson2.annotation.JSONType;
import io.appform.jsonrules.expressions.preoperation.PreOperationType;
import io.appform.jsonrules.utils.PreOperationUtils;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JSONType(typeName = "epoch")
public class EpochOperation extends CalendarOperation {

    public EpochOperation() {
        super(PreOperationType.epoch);
    }

    @Builder
    public EpochOperation(String operand, String zoneOffSet) {
        super(PreOperationType.epoch, operand, zoneOffSet, null);
    }

    @Builder
    public EpochOperation(String operand, String zoneOffSet, String pattern) {
        super(PreOperationType.epoch, operand, zoneOffSet, pattern);
    }

    @Override
    protected Number compute(Object evaluatedNode, String operand, String zoneOffSet, String pattern) {
        /**
         * Pattern can't be used with epoch
         */
        long epochValue;
        if (evaluatedNode instanceof Number) {
            epochValue = ((Number) evaluatedNode).longValue();
        } else if (evaluatedNode instanceof String) {
            // Try to parse string as epoch
            try {
                epochValue = Long.parseLong((String) evaluatedNode);
            } catch (NumberFormatException e) {
                // If it's not a valid number, return 0 to match old Jackson behavior (asLong() on non-numeric text returns 0)
                epochValue = 0;
            }
        } else {
            // Non-numeric, non-string - return 0 to match old behavior
            epochValue = 0;
        }

        final OffsetDateTime dateTime = PreOperationUtils.getDateTime(epochValue, zoneOffSet);
        return PreOperationUtils.getFromDateTime(dateTime, operand);
    }
}
