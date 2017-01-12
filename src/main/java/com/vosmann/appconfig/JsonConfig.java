package com.vosmann.appconfig;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JsonConfig {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Map<String, Object> config;

    private JsonConfig(final Map<String, Object> config) {
        this.config = config;
    }

    public static JsonConfig fromDefaultLocation() {
        return from("src/test/resources/appconfig.json");
    }

    public static JsonConfig from(final String location) {
        try {
            return from(new FileInputStream(location));
        } catch (final IOException e) {
            throw new AppConfigException("Could not read file.", e);
        }
    }

    public static JsonConfig from(final InputStream inputStream) {
        final JsonNode root;
        try {
            root = OBJECT_MAPPER.readTree(inputStream);
        } catch (final IOException e) {
            throw new AppConfigException("Could not read file.", e);
        }
        final Map<String, Object> flattenedJson = flatten(root);
        return new JsonConfig(flattenedJson);
    }

    public static Map<String, Object> flatten(final JsonNode json) {
        final Map<String, Object> map = new HashMap<>();
        final String initialKeyPrefix = "";
        addKeys(json, initialKeyPrefix, map);
        return map;
    }

    private static void addKeys(final JsonNode node, final String currentPath, final Map<String, Object> map) {
        if (node.isObject()) {
            final ObjectNode object = (ObjectNode) node;
            object.fields()
                  .forEachRemaining(entry -> addKeys(entry.getValue(), nextPath(currentPath, entry.getKey()), map));

        } else if (node.isArray()) {
            throw new IllegalArgumentException("Array values not supported.");
        } else if (node.isValueNode()) {
            final ValueNode valueNode = (ValueNode) node;
            if (valueNode.isInt()) {
                map.put(currentPath, valueNode.asInt());
            } else if (valueNode.isLong()) {
                map.put(currentPath, valueNode.asLong());
            } else if (valueNode.isFloat()) {
                map.put(currentPath, valueNode.floatValue());
            } else if (valueNode.isDouble()) {
                map.put(currentPath, valueNode.asDouble());
            } else if (valueNode.isBoolean()) {
                map.put(currentPath, valueNode.asDouble());
            } else {
                map.put(currentPath, valueNode.asText());
            }
        }
    }

    private static String nextPath(final String currentPath, final String newPathElement) {
        if (currentPath.isEmpty()) {
            return newPathElement;
        }

        return currentPath + "." + newPathElement;
    }

    public Set<String> getKeys() {
        return config.keySet();
    }

    public boolean contains(final String key) {
        return config.containsKey(key);
    }

    public Object get(final String key) {
        return config.get(key);
    }

    @Override
    public String toString() {
        return "JsonConfig{config=" + config + '}';
    }

}

