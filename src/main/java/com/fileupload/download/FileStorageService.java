package com.fileupload.download;

import java.io.IOException;

import java.io.ByteArrayOutputStream;

import org.springframework.core.io.ByteArrayResource;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

	@Autowired
	FileJPARepo fileRepo;

	@Value("${file.upload-dir}") // Read the configured directory from properties
	private String uploadDir = "C:/MaheMyFiles/Storage";

	String modifiedName;

	// ------ Uploading files -----------------

	// ------ Default Upload using POST -------------
	public FileEntity uploadDefault(MultipartFile file) throws IOException {
		return storeFile(file, "file");
	}

	@Transactional
	public FileEntity storeFile(MultipartFile file, String fileType) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		long fileSize = file.getSize();

		FileEntity fileEntity = new FileEntity();

		if ("aadhar".equals(fileType)) {
			// fileEntity.getId();
			fileEntity.setAadharCard(fileName);
			fileEntity.setAaSize(fileSize);
			fileEntity.setAaType(fileType);
		} else if ("pancard".equals(fileType)) {
			// fileEntity.getId();
			fileEntity.setPanCard(fileName);
			fileEntity.setPaSize(fileSize);
			fileEntity.setPaType(fileType);
		} else if ("salaryslip".equals(fileType)) {
			// fileEntity.getId();
			fileEntity.setSalarySlip(fileName);
			fileEntity.setSaSize(fileSize);
			fileEntity.setSaType(fileType);
		} else if ("file".equals(fileType)) {
			// fileEntity.getId();
			fileEntity.setName(fileName);
			fileEntity.setFileSize(fileSize);
			fileEntity.setData(file.getBytes());
			fileEntity.setType(file.getContentType());
		}

		// long fileSize = file.getSize();

		fileEntity = fileRepo.save(fileEntity);
//
//		// Store the file content in the local file system
		String fileId = fileEntity.getId();
//		Path filePath = Paths.get(uploadDir, fileId);
//		Files.write(filePath, file.getBytes());
		
		// Create a directory using the ID as its name
		Path directoryPath = Paths.get(uploadDir, fileId);
		Files.createDirectories(directoryPath);

		// Store the file content in the created directory
		Path filePath = directoryPath.resolve(fileName);
		Files.write(filePath, file.getBytes());
		return fileEntity;


	}
	// ----------------------- end Default ----------------------
	// ----------------------- Upload by using PUT ----------------------

	public FileEntity uploadAadharByIdById(MultipartFile file, String id) throws IOException {
		return storeFileByIdCheck(file, "aadhar", id);
	}

	public FileEntity uploadPanCardById(MultipartFile file, String id) throws IOException {
		return storeFileByIdCheck(file, "pancard", id);
	}

	public FileEntity uploadSalarySlipById(MultipartFile file, String id) throws IOException {
		return storeFileByIdCheck(file, "salaryslip", id);
	}

	@Transactional
	public FileEntity storeFileByIdCheck(MultipartFile file, String fileDiffer, String id) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		long fileSize = file.getSize();

		// Check if a FileEntity with the given ID exists in the database
		Optional<FileEntity> optionalFileEntity = fileRepo.findById(id);

		String fileExtension = fileName.substring(fileName.lastIndexOf('.'));

		if (optionalFileEntity.isPresent()) {
			FileEntity fileEntity = optionalFileEntity.get();

			// Check for PDFS
			if (!"application/pdf".equals(file.getContentType())
					&& !"application/vnd.openxmlformats-officedocument.wordprocessingml.document"
							.equals(file.getContentType())) {
				throw new IllegalArgumentException(
						"Invalid file type:" + fileExtension + ". Only PDF/DOCX files are allowed.");
			}

			if ("aadhar".equals(fileDiffer)) {
				fileEntity.setAadharCard("aadhar" + fileExtension);
				fileEntity.setAaSize(fileSize);
				fileEntity.setAaData(file.getBytes());
				fileEntity.setAaType(file.getContentType());
			} else if ("pancard".equals(fileDiffer)) {
				fileEntity.setPanCard("pancard" + fileExtension);
				fileEntity.setPaSize(fileSize);
				fileEntity.setPaData(file.getBytes());
				fileEntity.setPaType(file.getContentType());
			} else if ("salaryslip".equals(fileDiffer)) {
				fileEntity.setSalarySlip("salaryslip" + fileExtension);
				fileEntity.setSaSize(fileSize);
				fileEntity.setSaData(file.getBytes());
				fileEntity.setSaType(file.getContentType());
			} else if ("file".equals(fileDiffer)) {
				fileEntity.setName(fileName);
				fileEntity.setFileSize(fileSize);
				fileEntity.setData(file.getBytes());
				fileEntity.setType(file.getContentType());
			}

			fileEntity = fileRepo.save(fileEntity);

			// Create a directory using the ID as its name
			Path directoryPath = Paths.get(uploadDir, id);
			Files.createDirectories(directoryPath);

			// Get the file extension from the original file name
			modifiedName = fileDiffer + fileExtension;

			// Store the file content in the created directory
			Path filePath = directoryPath.resolve(modifiedName);
			Files.write(filePath, file.getBytes());
			return fileEntity;

		} else {
			// If the ID is not present, handle the situation accordingly
			throw new IllegalArgumentException("FileEntity with ID " + id + " not found");
		}
	}

	// ----------------------- end of Upload ----------------------

	// ----------- Download Zip File----------------

	public ResponseEntity<Resource> downloadFilesById(String id) {
		List<FileEntity> fileEntities = fileRepo.findAllById(id);

		if (!fileEntities.isEmpty()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (ZipOutputStream zipOutputStream = new ZipOutputStream(baos)) {
				for (FileEntity fileEntity : fileEntities) {
					addFileToZip(zipOutputStream, Paths.get(uploadDir, id, fileEntity.getAadharCard()).toString(),
							fileEntity.getAadharCard());
					addFileToZip(zipOutputStream, Paths.get(uploadDir, id, fileEntity.getPanCard()).toString(),
							fileEntity.getPanCard());
					addFileToZip(zipOutputStream, Paths.get(uploadDir, id, fileEntity.getSalarySlip()).toString(),
							fileEntity.getSalarySlip());
				}

			} catch (IOException e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}

			ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"files.zip\"")
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
		} else {
			System.out.println("No Files: ");
			return ResponseEntity.notFound().build();
		}
	}

	private void addFileToZip(ZipOutputStream zipOutputStream, String filePath, String entryName) throws IOException {
		if (filePath != null) {
			Path file = Paths.get(filePath);
			if (Files.exists(file)) {
				System.out.println("Adding file to zip: " + filePath);
				ZipEntry zipEntry = new ZipEntry(entryName);
				zipOutputStream.putNextEntry(zipEntry);
				byte[] fileData = Files.readAllBytes(file);
				zipOutputStream.write(fileData, 0, fileData.length);
				zipOutputStream.closeEntry();
			} else {
				System.out.println("File does not exist: " + filePath);
			}
		}
	}

	// -------------------End of Zip File-----------------------------------

	// --------------------Single File Download within the ID ------------------

	public ResponseEntity<Resource> downloadFileByIdAndTypeSingleFile(String id, String fileType) {

		// Check if a FileEntity with the given ID exists in the database
		Optional<FileEntity> optionalFileEntity = fileRepo.findById(id);

		if (optionalFileEntity.isPresent()) {
			FileEntity fileEntity = optionalFileEntity.get();
			String fileName = null;
			String fileWhichType = null;

			if ("aadhar".equals(fileType)) {
				fileName = fileEntity.getAadharCard();
				fileWhichType = fileEntity.getAaType();
			} else if ("pancard".equals(fileType)) {
				fileName = fileEntity.getPanCard();
				fileWhichType = fileEntity.getPaType();
			} else if ("salaryslip".equals(fileType)) {
				fileName = fileEntity.getSalarySlip();
				fileWhichType = fileEntity.getSaType();
			} else {
				return ResponseEntity.notFound().build();
			}

			if (fileEntity != null) {
				Path filePath = Paths.get(uploadDir, id, fileName);

				if (fileWhichType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
					fileWhichType = "application/msword";
				}

				try {
					ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(filePath));

					return ResponseEntity.ok()
							.header(HttpHeaders.CONTENT_DISPOSITION,
									"attachment; filename=\"" + fileEntity.getName() + "\"")
							.contentType(MediaType.parseMediaType(fileWhichType)).body(resource);
				} catch (IOException e) {
					e.printStackTrace();
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				}
			}
		}

		return ResponseEntity.notFound().build();
	}

	// Second Method -- using list property -- complex
	public ResponseEntity<Resource> downloadFileByIdAndTypeSingleFile1(String id, String fileType) {
		List<FileEntity> fileEntities = fileRepo.findAllById(id);
		System.out.println(fileEntities.toString());
		// Check filtType and provides the object
		if (!fileEntities.isEmpty()) {
			FileEntity fileEntity = null;
			for (FileEntity entity : fileEntities) {
				if (fileType.equals("aadhar") && entity.getAadharCard() != null) {
					fileEntity = entity;
					break;
				} else if (fileType.equals("pancard") && entity.getPanCard() != null) {
					fileEntity = entity;
					break;
				} else if (fileType.equals("salaryslip") && entity.getSalarySlip() != null) {
					fileEntity = entity;
					break;
				}
			}

			String fileName = null;
			String fileWhichType = null;

			if ("aadhar".equals(fileType)) {
				fileName = fileEntity.getAadharCard();
				fileWhichType = fileEntity.getAaType();
			} else if ("pancard".equals(fileType)) {
				fileName = fileEntity.getPanCard();
				fileWhichType = fileEntity.getPaType();
			} else if ("salaryslip".equals(fileType)) {
				fileName = fileEntity.getSalarySlip();
				fileWhichType = fileEntity.getSaType();
			} else {
				return null;
			}

			if (fileEntity != null) {
				Path filePath = Paths.get(uploadDir, id, fileName);

				if (fileWhichType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
					fileWhichType = "application/msword";
				}

				try {
					ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(filePath));

					return ResponseEntity.ok()
							.header(HttpHeaders.CONTENT_DISPOSITION,
									"attachment; filename=\"" + fileEntity.getName() + "\"")
							.contentType(MediaType.parseMediaType(fileWhichType)).body(resource);
				} catch (IOException e) {
					e.printStackTrace();
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				}
			}
		}
		return ResponseEntity.notFound().build();
	}

	// ----------------- individual Zip File -----------------------

	public ResponseEntity<Resource> downloadFilesByIdAndTypeSingleFileZip(String id, String fileType) {
		List<FileEntity> fileEntities = fileRepo.findAllById(id);

		if (!fileEntities.isEmpty()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (ZipOutputStream zipOutputStream = new ZipOutputStream(baos)) {

				String filePath = null;
				String entryName = null;

				if ("aadhar".equals(fileType)) {
					filePath = Paths.get(uploadDir, id, "aadhar.pdf").toString();
					entryName = "aadhar.pdf";
				} else if ("pancard".equals(fileType)) {
					filePath = Paths.get(uploadDir, id, "pancard.pdf").toString();
					entryName = "pancard.pdf";
				} else if ("salaryslip".equals(fileType)) {
					filePath = Paths.get(uploadDir, id, "salaryslip.pdf").toString();
					entryName = "salaryslip.pdf";
				}

				// addFileToZip is used here for zip the file
				if (filePath != null && entryName != null) {
					addFileToZip(zipOutputStream, filePath, entryName);
				}

			} catch (IOException e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}

			ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"files.zip\"")
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// --------------------End of Single File Download within the ID -----------

}

/*
 * 3. Create Service for File Storage The File Storage Service will use
 * FileDBRepository to provide following methods:
 * 
 * store(file): receives MultipartFile object, transform to FileDB object and
 * save it to Database getFile(id): returns a FileDB object by provided Id
 * getAllFiles(): returns all stored files as list of code>FileDB objects
 * 
 */

/*
 * 
 * 
 * 
 * public FileEntity uploadAadhar(MultipartFile file) throws IOException {
 * return storeFile(file, "aadhar"); } public FileEntity
 * uploadPanCard(MultipartFile file) throws IOException { return storeFile(file,
 * "pancard"); }
 * 
 * public FileEntity uploadSalarySlip(MultipartFile file) throws IOException {
 * return storeFile(file, "salaryslip"); }
 * 
 * 
 * 
 * 
 * 
 * @Transactional public FileEntity store(MultipartFile file) throws IOException
 * { String fileName = StringUtils.cleanPath(file.getOriginalFilename()); long
 * fileSize = file.getSize();
 * 
 * 
 * FileEntity fileEntity = new FileEntity(); fileEntity.setName(fileName);
 * fileEntity.setFileSize(fileSize); fileEntity.setType(file.getContentType());
 * 
 * fileEntity = fileRepo.save(fileEntity);
 * 
 * // Store the file content in the local file system String fileId =
 * fileEntity.getId(); Path filePath = Paths.get(uploadDir, fileId);
 * Files.write(filePath, file.getBytes());
 * 
 * return fileEntity;
 * 
 * }
 * 
 * 	// --------------------Fetch all the files(Not working as expected)

	public Stream<FileEntity> getAllFiles() {
		return fileRepo.findAll().stream();
	}

	// --------------------End of Fetch all the files ------------------

	// ---download-----
	public byte[] getFileDataFromStorage(String id) {
		Path filePath = Paths.get(uploadDir, id);
		try {
			return Files.readAllBytes(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


 * 
 */
