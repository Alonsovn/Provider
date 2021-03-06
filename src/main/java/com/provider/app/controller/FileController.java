package com.provider.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.provider.app.kafka.ProviderProducer;
import com.provider.app.message.ResponseMessage;
import com.provider.app.service.IFileStorageService;

@RequestMapping("/provider")
@RestController
public class FileController {

	@Autowired
	private ProviderProducer producer;
	@Autowired
	private IFileStorageService storageService;

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadExcelFile(@RequestParam("file") MultipartFile file) {

		String message = "";
		try {
			String jsonContent = storageService.uploadFile(file);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			producer.sendMessage(jsonContent);
			storageService.deleteFile(file.getOriginalFilename());
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not Uploaded the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

}
