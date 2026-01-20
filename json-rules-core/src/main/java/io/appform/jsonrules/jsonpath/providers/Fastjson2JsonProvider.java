package io.appform.jsonrules.jsonpath.providers;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.spi.json.AbstractJsonProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * JsonPath {@link com.jayway.jsonpath.spi.json.JsonProvider} implementation backed by fastjson2.
 *
 * <p>JsonPath expects this provider to operate on Java {@link Map}/{@link List} structures.
 * fastjson2's {@link JSONObject}/{@link JSONArray} implement these contracts, so we can use them
 * directly without conversion.</p>
 */
public class Fastjson2JsonProvider extends AbstractJsonProvider {

    @Override
    public Object parse(String json) throws InvalidJsonException {
        try {
            if (json == null) {
                return null;
            }
            return JSON.parse(json, JSONReader.Feature.UseDoubleForDecimals);
        } catch (Exception e) {
            throw new InvalidJsonException("Unable to parse JSON", e);
        }
    }

    @Override
    public Object parse(byte[] json) throws InvalidJsonException {
        try {
            if (json == null) {
                return null;
            }
            return JSON.parse(json, JSONReader.Feature.UseDoubleForDecimals);
        } catch (Exception e) {
            throw new InvalidJsonException("Unable to parse JSON", e);
        }
    }

    @Override
    public Object parse(InputStream jsonStream, String charset) throws InvalidJsonException {
        if (jsonStream == null) {
            return null;
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[8 * 1024];
            int read;
            while ((read = jsonStream.read(buf)) >= 0) {
                out.write(buf, 0, read);
            }
            byte[] bytes = out.toByteArray();
            // Prefer fastjson2 byte[] parsing; charset only matters when caller handed non-UTF8 bytes.
            if (charset != null && !charset.isEmpty() && !StandardCharsets.UTF_8.name().equalsIgnoreCase(charset)) {
                Charset cs = Charset.forName(charset);
                return parse(new String(bytes, cs));
            }
            return JSON.parse(bytes, JSONReader.Feature.UseDoubleForDecimals);
        } catch (IOException e) {
            throw new InvalidJsonException("Unable to read JSON stream", e);
        } catch (Exception e) {
            throw new InvalidJsonException("Unable to parse JSON", e);
        }
    }

    // JsonPath versions differ on whether JsonProvider declares parse(Reader);
    // keep this as a helper on the concrete provider.
    public Object parse(Reader reader) throws InvalidJsonException {
        if (reader == null) {
            return null;
        }
        try {
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[8 * 1024];
            int read;
            while ((read = reader.read(buf)) >= 0) {
                sb.append(buf, 0, read);
            }
            return parse(sb.toString());
        } catch (IOException e) {
            throw new InvalidJsonException("Unable to read JSON reader", e);
        }
    }

    @Override
    public String toJson(Object obj) {
        // Keep fastjson2 serialization stable.
        return JSON.toJSONString(obj);
    }

    @Override
    public Object createArray() {
        return new JSONArray();
    }

    @Override
    public Object createMap() {
        return new JSONObject();
    }

    @Override
    public boolean isArray(Object obj) {
        return obj instanceof Collection || (obj != null && obj.getClass().isArray());
    }

    @Override
    public boolean isMap(Object obj) {
        return obj instanceof Map;
    }

    @Override
    public Object unwrap(Object obj) {
        if (obj == null) {
            return null;
        }
        // fastjson2 basic types (String/Number/Boolean/Map/List) are already "unwrapped".
        return obj;
    }

    @Override
    public int length(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).size();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).size();
        }
        if (obj.getClass().isArray()) {
            return java.lang.reflect.Array.getLength(obj);
        }
        return super.length(obj);
    }

    @Override
    public Iterable<?> toIterable(Object obj) {
        if (obj == null) {
            return Collections.emptyList();
        }
        if (obj instanceof Iterable) {
            return (Iterable<?>) obj;
        }
        if (obj.getClass().isArray()) {
            int len = java.lang.reflect.Array.getLength(obj);
            List<Object> list = new ArrayList<>(len);
            for (int i = 0; i < len; i++) {
                list.add(java.lang.reflect.Array.get(obj, i));
            }
            return list;
        }
        return super.toIterable(obj);
    }

    @Override
    public Object getArrayIndex(Object obj, int idx) {
        if (obj instanceof List) {
            return ((List<?>) obj).get(idx);
        }
        if (obj != null && obj.getClass().isArray()) {
            return java.lang.reflect.Array.get(obj, idx);
        }
        return super.getArrayIndex(obj, idx);
    }


    @Override
    public void setArrayIndex(Object array, int index, Object newValue) {
        if (array instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) array;
            if (index == list.size()) {
                list.add(newValue);
            } else {
                list.set(index, newValue);
            }
            return;
        }
        super.setArrayIndex(array, index, newValue);
    }

    @Override
    public Object getMapValue(Object obj, String key) {
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).get(key);
        }
        return super.getMapValue(obj, key);
    }

    @Override
    public void setProperty(Object obj, Object key, Object value) {
        if (obj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) obj;
            map.put(String.valueOf(key), value);
            return;
        }
        super.setProperty(obj, key, value);
    }

    @Override
    public void removeProperty(Object obj, Object key) {
        if (obj instanceof Map) {
            ((Map<?, ?>) obj).remove(String.valueOf(key));
            return;
        }
        super.removeProperty(obj, key);
    }

    @Override
    public Collection<String> getPropertyKeys(Object obj) {
        if (obj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, ?> map = (Map<String, ?>) obj;
            return map.keySet();
        }
        return super.getPropertyKeys(obj);
    }
}
