// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import java.util.TimeZone;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.fasterxml.jackson.databind.JavaType;
import java.util.List;
import java.util.HashMap;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil
{
    private static final ObjectMapper JSON_MAPPER;
    
    private JsonUtil() {
    }
    
    public static String toJsonString(final Object object) {
        try {
            return JsonUtil.JSON_MAPPER.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Json serialization exception.", e);
        }
    }
    
    public static <T, V> Map<T, V> stringToMap(final String json, final Class<T> classT, final Class<V> classV) {
        try {
            return JsonUtil.JSON_MAPPER.readValue(json, new TypeReference<Map<T, V>>() {});
        }
        catch (Exception e) {
            throw new RuntimeException("Json\u53cd\u5e8f\u5217\u5316\u5f02\u5e38", e);
        }
    }
    
    public static <T> T parseObject(final String json, final Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return JsonUtil.JSON_MAPPER.readValue(json, clazz);
        }
        catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException("Json deserialization exception.", e);
        }
    }
    
    public static <V> Map<String, V> parseMap(final String jsonStr) {
        try {
            return JsonUtil.JSON_MAPPER.readValue(jsonStr, new TypeReference<Map<String, V>>() {});
        }
        catch (Exception e) {
            throw new RuntimeException("Json deserialization exception.", e);
        }
    }
    
    public static <T> T parseObject(final String jsonStr, final TypeReference<T> type) {
        try {
            return JsonUtil.JSON_MAPPER.readValue(jsonStr, type);
        }
        catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException("Json deserialization exception.", e);
        }
    }
    
    public static Map<String, String> parseObjectMap(final String json) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return JsonUtil.JSON_MAPPER.readValue(json, new TypeReference<HashMap<String, String>>() {});
        }
        catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException("Json deserialization exception.", e);
        }
    }
    
    public static <T> List<T> parseObjectList(final String json, final Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            final JavaType javaType = JsonUtil.JSON_MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
            return JsonUtil.JSON_MAPPER.readValue(json, javaType);
        }
        catch (IOException e) {
            throw new RuntimeException("Json deserialization exception.", e);
        }
    }
    
    public static List<Map<String, Object>> parseObjectMapList(final String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return (List<Map<String, Object>>)Lists.newArrayList();
        }
        try {
            return parseObject(jsonStr, (TypeReference<List<Map<String, Object>>>)new TypeReference<List<Map<String, Object>>>() {});
        }
        catch (Exception e) {
            throw new RuntimeException("Json deserialization exception.", e);
        }
    }
    
    public static JsonNode readTree(final String json) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return JsonUtil.JSON_MAPPER.readTree(json);
        }
        catch (IOException e) {
            throw new RuntimeException("Json parse exception.", e);
        }
    }
    
    public static boolean isJsonNode(final String json) {
        return json == null || readTree(json) != null;
    }
    
    public static String findNodeByField(final JsonNode jsonNode, final String field) {
        final JsonNode node = jsonNode.findValue(field);
        if (node == null) {
            return null;
        }
        return node.toString();
    }
    
    static {
        (JSON_MAPPER = new ObjectMapper()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonUtil.JSON_MAPPER.setTimeZone(TimeZone.getDefault());
        JsonUtil.JSON_MAPPER.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        JsonUtil.JSON_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JsonUtil.JSON_MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }
}
