package io.appform.jsonrules.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import io.appform.jsonrules.config.JsonRulesConfiguration;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonPathUtils {

    private static Configuration defaultConfigurationFor(Object node) {
        if (node instanceof java.util.Map || node instanceof java.util.List || node instanceof JSONObject || node instanceof JSONArray) {
            return JsonRulesConfiguration.getConfiguration();
        }
        throw new IllegalArgumentException("Unsupported node type for JsonPath parsing");
    }

    public static <T> T read(final Object node, final String path) {
        return JsonPath
                .using(defaultConfigurationFor(node))
                .parse(node)
                .read(path);
    }

    public static <T> T read(final Configuration configuration, final Object node, final String path) {
        return JsonPath
                .using(configuration)
                .parse(node)
                .read(path);
    }
}
