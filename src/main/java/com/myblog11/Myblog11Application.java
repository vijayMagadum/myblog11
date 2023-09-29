package com.myblog11;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Myblog11Application {

	public static void main(String[] args) {
		SpringApplication.run(Myblog11Application.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){

		return new ModelMapper();
	}


}
