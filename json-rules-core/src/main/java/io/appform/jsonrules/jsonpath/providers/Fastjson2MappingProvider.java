package io.appform.jsonrules.jsonpath.providers;

import com.alibaba.fastjson2.JSON;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

/**
 * JsonPath {@link MappingProvider} implementation backed by fastjson2.
 */
public class Fastjson2MappingProvider implements MappingProvider {

    @Override
    public <T> T map(Object source, Class<T> targetType, Configuration configuration) {
        if (source == null) {
            return null;
        }
        if (targetType.isInstance(source)) {
            return targetType.cast(source);
        }
        return JSON.parseObject(JSON.toJSONString(source), targetType);
    }

    @Override
    public <T> T map(Object source, TypeRef<T> targetType, Configuration configuration) {
        if (source == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        T value = (T) JSON.parseObject(JSON.toJSONString(source), targetType.getType());
        return value;
    }
}
