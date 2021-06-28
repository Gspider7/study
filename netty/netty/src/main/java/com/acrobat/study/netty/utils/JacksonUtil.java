package com.acrobat.study.netty.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * json序列化工具类
 * @date 2019-08-12 15:50
 */
public class JacksonUtil {

    // 对象解析器，包含解析配置
    private static ObjectMapper objectMapper = null;

    // 初始化 && 配置对象解析器
    public static ObjectMapper objectMapper() {
        if (objectMapper != null) return objectMapper;
        synchronized (JacksonUtil.class) {
            if (objectMapper == null) {
                objectMapper = new ObjectMapper();

                objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);       // 允许未知元素
                objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);                   // 允许单引号

//                // null值序列化
//                objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
//                    @Override
//                    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
//                            throws IOException {
//                        jsonGenerator.writeString("");
//                    }
//                });
            }
        }
        return objectMapper;
    }

    // ------------------------------------------------------------------------

    /**
     * 指定Class，解析单个Object
     */
    public static <T> T readValue(String content, Class<T> clazz) throws IOException {
        return objectMapper().readValue(content, clazz);
    }

    /**
     * 指定Class，解析Object列表
     */
    public static <T> List<T> readListValues(String content, Class<T> clazz) throws IOException {
        JavaType javaType = objectMapper().getTypeFactory().constructCollectionType(List.class, clazz);
        return objectMapper().readValue(content, javaType);
    }

    /**
     * 序列化单个Object
     */
    public static String writeValueAsString(Object obj) throws JsonProcessingException {
        return objectMapper().writeValueAsString(obj);
    }

    /**
     * 读取整个json树结构
     */
    public static JsonNode readTree(String json) throws IOException {
        return objectMapper().readTree(json);
    }
}
