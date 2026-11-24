package com.javanauta.usuariogradlegroovy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class UsuariogradlegroovyApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuariogradlegroovyApplication.class, args);
	}

}
