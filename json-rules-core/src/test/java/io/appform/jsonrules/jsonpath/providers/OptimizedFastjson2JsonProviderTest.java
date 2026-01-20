package io.appform.jsonrules.jsonpath.providers;

import com.google.common.collect.ImmutableSet;
import com.jayway.jsonpath.Option;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class OptimizedFastjson2JsonProviderTest {

    private OptimizedFastjson2JsonProvider provider;

    public OptimizedFastjson2JsonProviderTest() {
        provider = new OptimizedFastjson2JsonProvider(ImmutableSet.of(Option.AS_PATH_LIST));
    }

    @Test
    public void testConstructorWithOptionAsPathList() {
        provider = new OptimizedFastjson2JsonProvider(ImmutableSet.of(Option.AS_PATH_LIST));
        assertTrue(provider.isShouldReturnPathAsList());
    }

    @Test
    public void testConstructorWithoutOptionAsPathList() {
        provider = new OptimizedFastjson2JsonProvider(Collections.emptySet());
        assertFalse(provider.isShouldReturnPathAsList());
    }

    @Test
    public void testSetArrayIndexWithNonArrayThrowsException() {
        try {
            provider.setArrayIndex("notAnArray", 0, "value");
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    @Test
    public void testSetArrayIndexWithValidArrayAndIndex() {
        List<Object> list = new ArrayList<>();
        list.add("existing");

        provider.setArrayIndex(list, 0, "value");

        assertEquals("value", list.get(0));
    }

    @Test
    public void testSetArrayIndexWithValidArrayAndIndexEqualToSize() {
        List<Object> list = new ArrayList<>();
        list.add("existing");

        provider.setArrayIndex(list, 1, "value");

        assertEquals(2, list.size());
        assertEquals("value", list.get(1));
    }

    @Test
    public void testCreateJsonElementWithNullValue() {
        Object result = provider.createJsonElement(null);
        assertNull(result);
    }

    @Test
    public void testCreateJsonElementWithMapOrList() {
        Object map = Collections.singletonMap("k", "v");
        assertSame(map, provider.createJsonElement(map));

        Object list = Collections.singletonList("v");
        assertSame(list, provider.createJsonElement(list));
    }

    @Test
    public void testCreateJsonElementWithNonJsonNode() {
        provider = new OptimizedFastjson2JsonProvider(Collections.emptySet());
        Object value = "testValue";
        assertNull(provider.createJsonElement(value));

        provider = new OptimizedFastjson2JsonProvider(ImmutableSet.of(Option.AS_PATH_LIST));
        Object result = provider.createJsonElement(value);
        assertNotNull(result);
        assertEquals("testValue", result);
    }
}