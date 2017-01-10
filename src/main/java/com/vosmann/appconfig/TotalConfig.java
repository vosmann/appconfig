package com.vosmann.appconfig;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.google.common.io.Resources;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.copyOf;

public class TotalConfig {

    private Map<String, Object> allConfigs;

    public static TotalConfig fromDefaultLocation() {
        return from("src/test/resources/appconfig.json");
    }

    public static TotalConfig from(String location) {
        try {
            return from(new FileInputStream(location));
        } catch (IOException e) {
            throw new AppConfigException("Could not read file.", e);
        }
    }

    public static TotalConfig from(InputStream inputStream) {
        ObjectMapper objectMapper = new ObjectMapper();
        final JsonNode root;
        try {
            root = objectMapper.readTree(inputStream);
        } catch (IOException e) {
            throw new AppConfigException("Could not read file.", e);
        }
        return new TotalConfig(JsonFlattener.flatten(root));
    }

    private TotalConfig(Map<String, Object> allConfigs) {
        this.allConfigs = copyOf(allConfigs);
    }


    public <T> T get(String key) {
        return (T) allConfigs.get(key);
    }

    public static class JsonFlattener {

        public static Map<String, Object> flatten(JsonNode json) {
            Map<String, Object> map = new HashMap<>();
            String initialKeyPrefix = "";
            addKeys(json, initialKeyPrefix, map);
            return map;
        }

        private static void addKeys(JsonNode node, String currentPath, Map<String, Object> map) {
            if (node.isObject()) {
                ObjectNode object = (ObjectNode) node;
                object.fields()
                      .forEachRemaining(entry -> addKeys(entry.getValue(), nextPath(currentPath, entry.getKey()),  map));

            } else if (node.isArray()) {
                // todo support integer, floating point and string arrays.
                // ArrayNode arrayNode = (ArrayNode) node;
                // arrayNode.elements()
                //          .forEachRemaining();
                // addKeys(currentPath + "[" + i + "]", arrayNode.get(i), map);
                // }
            } else if (node.isValueNode()) {
                ValueNode valueNode = (ValueNode) node;
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

        private static String nextPath(String currentPath, String newPathElement) {
            if (currentPath.isEmpty()) {
                return newPathElement;
            }

            return currentPath + "." + newPathElement;
        }
    }

    @Override
    public String toString() {
        return "TotalConfig{allConfigs=" + allConfigs + '}';
    }
}
