package net.uoay.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;

@SpringBootApplication
@EnableJpaAuditing
@EnableRedisIndexedHttpSession
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
