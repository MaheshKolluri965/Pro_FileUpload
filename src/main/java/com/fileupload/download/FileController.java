package com.fileupload.download;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("rest/api")
@CrossOrigin("http://localhost:4200")
public class FileController {

	@Autowired
	private FileStorageService storageService;

	// -- 1. uploading individual files by using post --> to create a row in
	// db(remaning methods are not required
	@PostMapping("/upload/default")
	public ResponseEntity<ResponseMessage> uploadFileByTypeDefault(@RequestParam("file") MultipartFile file) {

		String message = "";
		try {
			// storageService.store(file);
			FileEntity savedFile = storageService.uploadDefault(file);

			message = "Uploaded the file successfully: " + file.getOriginalFilename() + " (ID: " + savedFile.getId()
					+ ")";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + " - " + e;
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}
	// -------------------End of Default-----------------

	// 2. uploading individual files by using put --> for writing data in db
	@PutMapping("/upload/aadhar/{id}")
	public ResponseEntity<ResponseMessage> uploadFileByAadharById(@RequestParam("aadhar") MultipartFile file,
			@PathVariable String id) {

		String message = "";
		try {
			FileEntity savedFile = storageService.uploadAadharByIdById(file, id);

			message = "Uploaded the file successfully: " + file.getOriginalFilename() + " (ID: " + savedFile.getId()
					+ ")";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + " - " + e;
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

	@PutMapping("/upload/pancard/{id}")
	public ResponseEntity<ResponseMessage> uploadFilePanCardByID(@RequestParam("pancard") MultipartFile file,
			@PathVariable String id) {

		String message = "";
		try {
			FileEntity savedFile = storageService.uploadPanCardById(file, id);

			message = "Uploaded the file successfully: " + file.getOriginalFilename() + " (ID: " + savedFile.getId()
					+ ")";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + " - " + e;
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

	@PutMapping("/upload/salaryslip/{id}")
	public ResponseEntity<ResponseMessage> uploadFileSalarySlipById(@RequestParam("salaryslip") MultipartFile file,
			@PathVariable String id) {

		String message = "";
		try {
			FileEntity savedFile = storageService.uploadSalarySlipById(file, id);

			message = "Uploaded the file successfully: " + file.getOriginalFilename() + " (ID: " + savedFile.getId()
					+ ")";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + " - " + e;
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

	// ---------------- End of Upload -------------------------

	// 3. For making the zip file with all the files within the ID
	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> downloadFilesByUserId(@PathVariable String id) {
		return storageService.downloadFilesById(id);
	}
	// ---------------End of Zip File--------------------------

	// 4. Individual file download in a ID
	@GetMapping("/download/{id}/{fileType}")
	public ResponseEntity<Resource> downloadFileByIdAndTypes(@PathVariable String id, @PathVariable String fileType) {
		// System.out.println("in");
		ResponseEntity<Resource> response = storageService.downloadFileByIdAndTypeSingleFile(id, fileType);
		return response;
	}
	// ---------------End of single file download in the ID---------------

}

/*
 * @PostMapping("/upload") public ResponseEntity<ResponseMessage>
 * uploadFile(@RequestParam("file") MultipartFile file) {
 * 
 * String message = ""; try { //storageService.store(file); FileEntity savedFile
 * = storageService.store(file);
 * 
 * message = "Uploaded the file successfully: " + file.getOriginalFilename() +
 * " (ID: " + savedFile.getId() + ")"; return
 * ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message)); }
 * catch (Exception e) { message = "Could not upload the file: " +
 * file.getOriginalFilename() + " - " + e; return
 * ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new
 * ResponseMessage(message)); } }
 * 
 * @PostMapping("/upload/aadhar") public ResponseEntity<ResponseMessage>
 * uploadFileByAadhar(@RequestParam("aadhar") MultipartFile file) {
 * 
 * String message = ""; try { //storageService.store(file); FileEntity savedFile
 * = storageService.uploadAadhar(file);
 * 
 * message = "Uploaded the file successfully: " + file.getOriginalFilename() +
 * " (ID: " + savedFile.getId() + ")"; return
 * ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message)); }
 * catch (Exception e) { message = "Could not upload the file: " +
 * file.getOriginalFilename() + " - " + e; return
 * ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new
 * ResponseMessage(message)); } }
 * 
 * @PostMapping("/upload/pancard") public ResponseEntity<ResponseMessage>
 * uploadFilePanCard(@RequestParam("pancard") MultipartFile file,
 * 
 * @PathVariable FileEntity id) {
 * 
 * String message = ""; try { //storageService.store(file); FileEntity savedFile
 * = storageService.uploadPanCard(file);
 * 
 * message = "Uploaded the file successfully: " + file.getOriginalFilename() +
 * " (ID: " + savedFile.getId() + ")"; return
 * ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message)); }
 * catch (Exception e) { message = "Could not upload the file: " +
 * file.getOriginalFilename() + " - " + e; return
 * ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new
 * ResponseMessage(message)); } }
 * 
 * @PostMapping("/upload/salaryslip") public ResponseEntity<ResponseMessage>
 * uploadFileSalarySlip(@RequestParam("salaryslip") MultipartFile file) {
 * 
 * String message = ""; try { //storageService.store(file); FileEntity savedFile
 * = storageService.uploadSalarySlip(file);
 * 
 * message = "Uploaded the file successfully: " + file.getOriginalFilename() +
 * " (ID: " + savedFile.getId() + ")"; return
 * ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message)); }
 * catch (Exception e) { message = "Could not upload the file: " +
 * file.getOriginalFilename() + " - " + e; return
 * ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new
 * ResponseMessage(message)); } }
 * 
 * // X. For list of all files --> not Working as Expected
 * 
 * @GetMapping("/files") public ResponseEntity<List<ResponseFile>>
 * getListFiles() { List<ResponseFile> files =
 * storageService.getAllFiles().map(dbFile -> { String fileDownloadUri =
 * ServletUriComponentsBuilder .fromCurrentContextPath() .path("/files/")
 * .path(dbFile.getId()) .toUriString(); // Add a null check before accessing
 * the length of data long dataLength = (dbFile.getFileSize() != 0) ?
 * dbFile.getFileSize() : 0; return new ResponseFile( dbFile.getName(),
 * fileDownloadUri, dbFile.getType(), dataLength);
 * }).collect(Collectors.toList());
 * 
 * return ResponseEntity.status(HttpStatus.OK).body(files); }
 * 
 * 
 * 
 */

/*
 * 5. – @CrossOrigin is for configuring allowed origins. – @Controller
 * annotation is used to define a controller. – @GetMapping and @PostMapping
 * annotation is for mapping HTTP GET & POST requests onto specific handler
 * methods:
 * 
 * POST /upload: uploadFile() GET /files: getListFiles() GET /files/[id]:
 * getFile() – We use @Autowired to inject implementation of FileStorageService
 * bean to local variable.
 * 
 * 6.Configure Spring Datasource, JPA, Hibernate spring.datasource.username &
 * spring.datasource.password properties are the same as your database
 * installation. Spring Boot uses Hibernate for JPA implementation, we configure
 * MySQL5InnoDBDialect for MySQL or PostgreSQLDialect for PostgreSQL
 * spring.jpa.hibernate.ddl-auto is used for database initialization. We set the
 * value to update value so that a table will be created in the database
 * automatically corresponding to defined data model. Any change to the model
 * will also trigger an update to the table. For production, this property
 * should be validate.
 * 
 * 7. Configure Multipart File for Servlet –
 * spring.servlet.multipart.max-file-size: max file size for each request. –
 * spring.servlet.multipart.max-request-size: max request size for a
 * multipart/form-data
 * 
 */