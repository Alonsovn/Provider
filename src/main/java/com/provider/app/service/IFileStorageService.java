package com.provider.app.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.provider.app.model.ExcelFile;

public interface IFileStorageService {

	public void init();

	public void save(MultipartFile file);

	public Resource load(String filename);

	public void deleteAll();

	public void deleteFile(String fileName);

	public List<ExcelFile> uploadExcelFile(MultipartFile file);

}
