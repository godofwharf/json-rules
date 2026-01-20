package io.appform.jsonrules.config;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import io.appform.jsonrules.jsonpath.providers.Fastjson2JsonProvider;
import io.appform.jsonrules.jsonpath.providers.Fastjson2MappingProvider;
import lombok.Getter;

import java.util.EnumSet;
import java.util.Set;

/**
 * JsonPath defaults based on fastjson2 {@code JSONObject}/{@code JSONArray}.
 */
public final class Fastjson2Configuration implements Configuration.Defaults {

    @Getter
    private static final Fastjson2Configuration instance = new Fastjson2Configuration();

    private final Fastjson2JsonProvider jsonProvider = new Fastjson2JsonProvider();
    private final Fastjson2MappingProvider mappingProvider = new Fastjson2MappingProvider();

    @Override
    public JsonProvider jsonProvider() {
        return jsonProvider;
    }

    @Override
    public MappingProvider mappingProvider() {
        return mappingProvider;
    }

    @Override
    public Set<Option> options() {
        return EnumSet.noneOf(Option.class);
    }

    public Configuration getConfiguration() {
        return Configuration.builder()
                .jsonProvider(jsonProvider)
                .mappingProvider(mappingProvider)
                .options(options())
                .build();
    }
}
