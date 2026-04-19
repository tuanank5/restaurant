package org.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"org.restaurant", "org.restaurant.api", "org.restaurant.service", "org.restaurant.repository"})
@EntityScan(basePackages = {"entity"})
@EnableJpaRepositories(basePackages = {"org.restaurant.repository"})
public class RestaurantBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantBackendApplication.class, args);
	}

}
