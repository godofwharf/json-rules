package io.appform.jsonrules.utils;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;

/**
 * Test-only JSON helpers.
 *
 * Used across unit tests to create fastjson2 objects from string literals.
 */

public final class TestJson {
    private TestJson() {
    }

    /**
     * Parse a JSON object string into a fastjson2 {@link JSONObject}.
     *
     * @throws IllegalArgumentException if the JSON is invalid or is not an object.
     */
    public static JSONObject obj(String json) {
        if (json == null) {
            return new JSONObject();
        }
        return JSONObject.parseObject(json, JSONObject.class, JSONReader.Feature.UseDoubleForDecimals);
    }
}
