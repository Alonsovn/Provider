package com.provider.app.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.stream.Collectors.toMap;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.util.FileSystemUtils;

import com.provider.app.util.FileUtil;

@Service
public class FileStorageImpl implements IFileStorageService {

	private final Path root = Paths.get("uploads");
	private final FileUtil fileutil = new FileUtil();

	@Override
	public void init() {
		try {
			Files.createDirectories(root);
		} catch (IOException e) {
			throw new RuntimeException("Could not create folder for upload!");
		}

	}

	@Override
	public void saveFile(MultipartFile file) {

		try {
			Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
			readFile(file.getOriginalFilename());
		} catch (Exception e) {
			throw new RuntimeException("Could not create folder for upload!");
		}
	}

	@Override
	public void deleteAll() {
		//FileSystemUtils.deleteRecursively(root.toFile());
	}

	@Override
	public List<Map<String, String>> upload(MultipartFile file) throws Exception {

		//Path tempDir = root; // Files.createDirectories(root);

		Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
		File tempFile = root.resolve(file.getOriginalFilename()).toFile();
		//File f = Files.read
		
		//file.transferTo(tempFile);


		Workbook workbook = WorkbookFactory.create(tempFile);   // .create(tempFile);

		Sheet sheet = workbook.getSheetAt(0);

		Supplier<Stream<Row>> rowStreamSupplier = fileutil.getRowStreamSupplier(sheet);  
		
		Row headerRow = rowStreamSupplier.get().findFirst().get();
		
		List<String> headerCells = fileutil.getStream(headerRow)  //getStream(headerRow)
				.map(Cell::getNumericCellValue)
				.map(String::valueOf)
				.collect(Collectors.toList());
		
		int colCount = headerCells.size();
		
		return rowStreamSupplier.get()
				.skip(1)
				.map(row -> {
			
					List<String> cellList = fileutil.getStream(row)
							.map(Cell::getStringCellValue)
							.collect(Collectors.toList());	
					
					return fileutil.cellIteratorSupplier(colCount)
							 .get()
							 .collect(toMap(headerCells::get, cellList::get));
		})
		.collect(Collectors.toList());
	}
	
	
	
	
	@Override
	public void readFile(String fileName) {
		try {
			String fullPath = root + "\\" + fileName;
			String content = Files.readString(Paths.get(fullPath));
	
			
			System.out.println("File content: \n " + content);
		} catch (IOException e) {
			throw new RuntimeException("Could not read the file. ");
		}

	}
}
