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

import com.provider.app.model.Provider;

import org.springframework.util.FileSystemUtils;

import org.apache.poi.ss.usermodel.WorkbookFactory;

@Service
public class FileStorageImpl implements IFileStorageService {

	private final Path root = Paths.get("uploads");

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
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}

	@Override
	public List<Provider> readExcelFile(MultipartFile file) {
		try {

			/*
			 * System.out.println("Reading excel file... path:" ); FileInputStream excelFile
			 * = new FileInputStream(new File(
			 * "C://Users//USER//Documents//Workspace//Eclipse//Provider//uploads//customers.xlsx"
			 * )); System.out.println("File got it"); Workbook workbook = new
			 * XSSFWorkbook(excelFile); System.out.println("workbook created");
			 */
			// Path tempDir = Files.createDirectories(root);
			// //Files.createTempDirectory("");
			File tempFile = root.resolve(file.getOriginalFilename()).toFile();
			Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));

			// file.transferTo(tempFile);

			Workbook workbook = WorkbookFactory.create(tempFile);

			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rows = sheet.iterator();

			List<Provider> lstCustomers = new ArrayList<Provider>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();

				Provider prov = new Provider();

				int cellIndex = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();

					if (cellIndex == 0) { // ID
						prov.setId((int) currentCell.getNumericCellValue());
						// System.out.println("0");

					} else if (cellIndex == 1) { // Weight
						prov.setWeight(currentCell.getStringCellValue());
						// System.out.println("1");

					} else if (cellIndex == 2) { // Volume
						prov.setVolume(currentCell.getStringCellValue());
						// System.out.println("2");

					} else if (cellIndex == 3) { // Value
						prov.setValue(currentCell.getStringCellValue());
						// System.out.println("3");

					} else if (cellIndex == 4) { // Units
						prov.setUnits((int) currentCell.getNumericCellValue());
						// System.out.println("4");

					} else if (cellIndex == 5) { // TransportType
						prov.setTrasnportType(currentCell.getStringCellValue());
						// System.out.println("5");

					} else if (cellIndex == 6) { // Warehouse
						prov.setWarehouse(currentCell.getStringCellValue());
						// System.out.println("6");

					} else if (cellIndex == 7) { // User
						prov.setUser((int) currentCell.getNumericCellValue());
						// System.out.println("7");

					} else if (cellIndex == 8) { // DeliveryDate
						prov.setDeliveryDate(currentCell.toString());
						// System.out.println("8");
					}

					cellIndex++;
				}

				lstCustomers.add(prov);
			}

			// Close WorkBook
			workbook.close();

			return lstCustomers;
		} catch (IOException e) {
			throw new RuntimeException("FAIL! -> message = " + e.getMessage());
		}
	}

	public Date StringToDate(String s) {

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
