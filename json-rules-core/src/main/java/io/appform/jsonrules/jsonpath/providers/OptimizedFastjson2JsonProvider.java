package io.appform.jsonrules.jsonpath.providers;

import com.google.common.annotations.VisibleForTesting;
import com.jayway.jsonpath.Option;
import lombok.Getter;

import java.util.List;
import java.util.Set;

/**
 * A performance-oriented JsonProvider that avoids converting arbitrary objects
 * into JSON values unless JsonPath has been configured with {@link Option#AS_PATH_LIST}.
 */
public class OptimizedFastjson2JsonProvider extends Fastjson2JsonProvider {

    @Getter
    private final boolean shouldReturnPathAsList;

    public OptimizedFastjson2JsonProvider(Set<Option> options) {
        this.shouldReturnPathAsList = options.contains(Option.AS_PATH_LIST);
    }

    @Override
    public void setArrayIndex(Object array, int index, Object newValue) {
        if (!(array instanceof List)) {
            throw new UnsupportedOperationException();
        }
        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) array;
        Object element = createJsonElement(newValue);
        if (index == list.size()) {
            list.add(element);
        } else {
            list.set(index, element);
        }
    }

    @VisibleForTesting
    Object createJsonElement(Object o) {
        if (o == null) {
            return null;
        }
        // If already a JSON-serializable structure, keep it.
        if (o instanceof java.util.Map || o instanceof java.util.List) {
            return o;
        }
        // [PATCHED]
        // Convert the object to a JSON value only if Option.AS_PATH_LIST is set.
        // We delegate conversion to JsonPath's default provider via toJson/parse.
        return shouldReturnPathAsList ? parse(toJson(o)) : null;
    }
}
