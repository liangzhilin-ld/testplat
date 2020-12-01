package com.techstar.testplat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@MapperScan(value = "com.autotest.data.mapper")
@ComponentScan(basePackages = {"com.techstar.testplat", "com.autotest.data"})
@SpringBootApplication
public class Application 
{
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
