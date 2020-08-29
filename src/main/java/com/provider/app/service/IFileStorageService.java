package com.provider.app.service;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.provider.app.model.Provider;

public interface IFileStorageService {

	public void init();

	public void save(MultipartFile file);

	public Resource load(String filename);

	public void deleteAll();

	public Stream<Path> loadAll();

	public List<Provider> readExcelFile(MultipartFile file);

}
