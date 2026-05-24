package com.grupo9.todo_list_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.grupo9")
public class TodoListApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoListApiApplication.class, args);
	}

}
