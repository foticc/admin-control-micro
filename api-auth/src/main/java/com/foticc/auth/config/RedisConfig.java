package com.foticc.auth.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.foticc.security.config.RedisTemplateConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.format.DateTimeFormatter;

/**
 * //TODO 缓存序列化方法修改
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * 使用泛型 <Object, Object> 会和stringRedisTemplate 冲突
     * 使用@Primary 以自定义的bean为主
     * @param connectionFactory
     * @return
     */
    @Primary
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.10#separation-of-general--json-specific-features
        ObjectMapper objectMapper = JsonMapper.builder()
                // 不序列化 transient字段
                .configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,false)
                .addModule(configureJavaTimeModule())
                .build();

        objectMapper.enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping.NON_FINAL,"@class");
//        ObjectMapper objectMapper = new ObjectMapper();
//        // 所有的字段
//        objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.ANY);
//        // 解决java.time类序列化
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER,true);


        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = jackson2JsonRedisSerializer();

        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);

        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        ObjectMapper objectMapper = JsonMapper.builder()
                // 不序列化 transient字段
                .configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
                // 忽略未知属性
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,false)
                .configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY,false)
                .addModule(configureJavaTimeModule())
                .build();

        objectMapper.enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping.NON_FINAL,"@class");
        return new Jackson2JsonRedisSerializer<>(objectMapper,Object.class);
    }

    public JavaTimeModule configureJavaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
        return javaTimeModule;
    }



    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return new RedisCacheManagerBuilderCustomizer() {
            @Override
            public void customize(RedisCacheManager.RedisCacheManagerBuilder builder) {
                builder.withCacheConfiguration("role_perms",
                        RedisCacheConfiguration.defaultCacheConfig()
                                // 定义为单冒号
                                .computePrefixWith(cacheName -> cacheName + ":")
                                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer()))
                );
            }
        };
    }


}
