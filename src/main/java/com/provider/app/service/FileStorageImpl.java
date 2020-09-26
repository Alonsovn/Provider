package com.provider.app.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.provider.app.controller.FileController;
import com.provider.app.model.ExcelFile;

import org.springframework.util.FileSystemUtils;

import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FileStorageImpl implements IFileStorageService {

	private final Path root = Paths.get("uploads");
	private final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Override
	public void init() {
		try {
			Files.createDirectories(root);
		} catch (IOException e) {
			throw new RuntimeException("Could not create folder for upload!");
		}

	}

	@Override
	public void save(MultipartFile file) {

		try {
			Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			throw new RuntimeException("Could not create folder for upload!");
		}
	}

	@Override
	public Resource load(String filename) {
		try {
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
	}

	@Override
	public void deleteFile(String fileName) {
		try {
			File file = new File(root + "\\" + fileName);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String uploadFile(MultipartFile file) {

		String result;
		String fileType = "";
		String extension = file.getOriginalFilename().split("\\.")[1];

		if (extension.contains("xlsx") || extension.contains("xls")) {
			fileType = "excel";
		}

		switch (fileType) {
		case "excel":
			result = uploadExcelFile(file);
			logger.info(String.format("#### -> Converted excel file to json format -> %s", result));
			break;

		default:
			logger.info(String.format("#### -> Unknown file type -> %s", file.getOriginalFilename()));
			result = "";
			break;
		}

		return result;

	}

	@Override
	public String uploadExcelFile(MultipartFile file) {
		try {

			File tempFile = root.resolve(file.getOriginalFilename()).toFile();
			Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));

			Workbook workbook = WorkbookFactory.create(tempFile);

			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rows = sheet.iterator();

			List<ExcelFile> excelFileRecords = new ArrayList<ExcelFile>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();

				ExcelFile excelFile = new ExcelFile();

				int cellIndex = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();

					if (cellIndex == 0) { // ID
						excelFile.setId((long) currentCell.getNumericCellValue());

					} else if (cellIndex == 1) { // Weight
						excelFile.setWeight(currentCell.getStringCellValue());

					} else if (cellIndex == 2) { // Volume
						excelFile.setVolume(currentCell.getStringCellValue());

					} else if (cellIndex == 3) { // Value
						excelFile.setValue(currentCell.getStringCellValue());

					} else if (cellIndex == 4) { // Units
						excelFile.setUnits((int) currentCell.getNumericCellValue());

					} else if (cellIndex == 5) { // TransportType
						excelFile.setTransportType(currentCell.getStringCellValue());

					} else if (cellIndex == 6) { // Warehouse
						excelFile.setWarehouse(currentCell.getStringCellValue());

					} else if (cellIndex == 7) { // User
						excelFile.setUser((int) currentCell.getNumericCellValue());

					} else if (cellIndex == 8) { // DeliveryDate
						excelFile.setDeliveryDate(stringToDate(currentCell.toString()));
					}

					cellIndex++;
				}

				excelFileRecords.add(excelFile);
			}

			// Close WorkBook
			workbook.close();

			return excelFileRecords.toString();
		} catch (IOException e) {
			throw new RuntimeException("FAIL! -> message = " + e.getMessage());
		}
	}

	public Date stringToDate(String s) {

		Date result = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			result = dateFormat.parse(s);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
