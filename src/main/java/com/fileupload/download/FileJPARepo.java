package com.fileupload.download;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileJPARepo extends JpaRepository<FileEntity, String> {
	
	List<FileEntity> findAllById(String id);

}
/*
 * Now we can use FileDBRepository with JpaRepository‘s methods such as: save(FileDB), findById(id), findAll().
 *  */
