package com.example.albarakaResidence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class AlbarakaResidenceApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return customizerBuilder(builder);
	}

	public static void main(String[] args) {
		customizerBuilder(new SpringApplicationBuilder()).run(args);
	}

	private static SpringApplicationBuilder customizerBuilder(SpringApplicationBuilder builder) {
		return builder.sources(AlbarakaResidenceApplication.class).bannerMode(Banner.Mode.OFF);
	}

}
