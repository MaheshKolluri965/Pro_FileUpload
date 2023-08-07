package com.fileupload.download;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="files")
public class FileEntity {
	
	  @Id
	  @GeneratedValue(generator = "id-generator")
	  @GenericGenerator(name = "id-generator", strategy = "com.fileupload.download.IDGenerator")
	  @Column(name="ID")
	  private String id;
	  
	  @Column(name="aadhar")
	  private String aadharCard;
	  
	  @Lob
	  @Column(name = "aadhar_data", columnDefinition = "LONGBLOB")
	  private byte[] aaData;
	  
	  @Column(name = "aadhar_filesize")
	  private long aaSize;
	  
	  @Column(name = "aadhar_filetype")
	   private String aaType;
	  
	  @Column(name="pancard")
	  private String panCard;

	  @Lob
	  @Column(name = "pancard_data", columnDefinition = "LONGBLOB")
	  private byte[] paData;
	  
	  @Column(name = "pancard_filesize")
	  private long paSize;
	  
	  @Column(name = "pancard_filetype")
	  private String paType;
	  
	  @Column(name="salaryslip")
	  private String salarySlip;
	  
	  @Lob
	  @Column(name = "salaryslip_data", columnDefinition = "LONGBLOB")
	  private byte[] saData;
	  
	  @Column(name = "salaryslip_filesize")
	  private long saSize;
	  
	  @Column(name = "salaryslip_filetype")
	   private String saType;
	  
	  
	  @Column(name="filename")
	  private String name;
	  
	  @Lob
	  @Basic(fetch = FetchType.LAZY) // Use FetchType.LAZY for better performance
	  @Column(name = "file_data", columnDefinition = "LONGBLOB")
	  private byte[] data;
	  
	  @Column(name="filesize")
	  private long fileSize;
	  
	  @Column(name="filetype")
	  private String type;
	  
	  

	  
	  
	  
}

/* 
 * 
	  //@GeneratedValue(generator = "uuid") -- complex ID
	  //@GenericGenerator(name = "uuid", strategy = "uuid2")
	  //private String id;
 * In the code above, data is annotated by @Lob annotation. LOB is datatype for storing large object data. There’re two kinds of LOB: BLOB and CLOB:

BLOB is for storing binary data
CLOB is for storing text data
*/
