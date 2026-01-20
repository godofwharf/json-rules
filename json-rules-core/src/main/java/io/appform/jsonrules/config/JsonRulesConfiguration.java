package io.appform.jsonrules.config;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.cache.CacheProvider;
import io.appform.jsonrules.jsonpath.caches.UnboundedCache;
import io.appform.jsonrules.jsonpath.providers.OptimizedFastjson2JsonProvider;
import lombok.Getter;
import lombok.val;

public class JsonRulesConfiguration {
    @Getter
    private static Configuration configuration;

    static {
        val fastjson2Configuration = Fastjson2Configuration.getInstance();
        configuration = new Configuration.ConfigurationBuilder()
                .jsonProvider(fastjson2Configuration.jsonProvider())
                .mappingProvider(fastjson2Configuration.mappingProvider())
                .options(fastjson2Configuration.options())
                .build();
    }

    public static void configure(final PerformanceSafetyPreference performanceSafetyPreference) {
        if (performanceSafetyPreference == PerformanceSafetyPreference.SPEED) {
            CacheProvider.setCache(new UnboundedCache());
            configuration.jsonProvider(new OptimizedFastjson2JsonProvider(
                    Fastjson2Configuration.getInstance().options()));
        }
        // if performanceSafetyPreference is set to SAFETY, we don't override the cache implementation provided by jsonpath library
    }

    public enum PerformanceSafetyPreference {
        SPEED,
        SAFETY
    }
}
