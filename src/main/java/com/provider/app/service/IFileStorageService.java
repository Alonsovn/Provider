package com.provider.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {

	public void init();

	public void saveFile(MultipartFile file);

	public void deleteAll();
	
	public void readFile(String fileName);
	
	public List<Map<String, String>> upload(MultipartFile file) throws Exception;

}
