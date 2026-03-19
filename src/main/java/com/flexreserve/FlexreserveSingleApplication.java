package com.flexreserve;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.flexreserve.**.mapper")
public class FlexreserveSingleApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlexreserveSingleApplication.class, args);
	}

}
