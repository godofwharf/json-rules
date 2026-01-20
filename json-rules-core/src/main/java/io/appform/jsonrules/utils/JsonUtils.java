package io.appform.jsonrules.utils;

import com.alibaba.fastjson2.JSONArray;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class JsonUtils {

    public static String convertToString(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        return obj.toString();
    }

    public static Set<Object> convertToSet(final JSONArray arrayNode) {
        return new HashSet<>(arrayNode);
    }

    public static boolean checkAllMatch(final JSONArray arrayNode,
                                        final Set<Object> values) {
        for (Object element : arrayNode) {
            if (!values.contains(element)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkAnyMatch(final JSONArray arrayNode,
                                        final Set<Object> values) {
        for (Object element : arrayNode) {
            if (values.contains(element)) {
                return true;
            }
        }
        return false;
    }
}
