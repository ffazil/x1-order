package com.tracebucket.x1.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//Explicitly configure @EntityScan to enable the JSR-310 JPA 2.1 converters
@EntityScan(basePackageClasses = { X1OrderApplication.class, Jsr310JpaConverters.class })
//Enable Jpa auditing
@EnableJpaAuditing
// Explicitly enable entity links as Boot fails to auto-configure them
@EnableEntityLinks
@EnableAsync
public class X1OrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(X1OrderApplication.class, args);
	}
}
