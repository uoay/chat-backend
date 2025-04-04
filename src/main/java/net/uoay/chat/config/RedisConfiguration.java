package net.uoay.chat.config;

import net.uoay.chat.user.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {

    @Bean
    RedisTemplate<String, Profile> profileRedisTemplate(RedisConnectionFactory factory) {
        var template = new RedisTemplate<String, Profile>();
        template.setConnectionFactory(factory);
        return template;
    }

}
