package com.provider.app;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.provider.app.service.IFileStorageService;

@SpringBootApplication
public class ProviderApplication implements CommandLineRunner {

	@Resource
	IFileStorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(ProviderApplication.class, args);
	}

	
	@Override
	public void run(String... arg) throws Exception {
		storageService.deleteAll();
		storageService.init();
	}
	
	

}
