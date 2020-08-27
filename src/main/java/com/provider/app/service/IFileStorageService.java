package com.provider.app.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {
	
	public void init();

	public void saveFile(MultipartFile file);
	
	 public void deleteAll();

}
