import { Component } from '@angular/core';
import { FileserviceService } from '../fileservice.service';

@Component({
  selector: 'app-document',
  templateUrl: './document.component.html',
  styleUrls: ['./document.component.css']
})
export class DocumentComponent {

  defaultFile: File | any;
  aadharFile: File | any;
  panCardFile: File | any;
  salarySlipFile: File | any;
  filesList: any[] = [];
  selectedFile: any;
  
  id:any = "MJ1";
  aadhar ="aadhar";
  panCard = "pancard";
  salarySlip = "salaryslip";

  pdfCheck = 'application/pdf';
  docsCheck = 'application/vnd.openxmlformats-officedocument.wordprocessingml.document';

  document: Document = new Document();

  constructor(private fileUploadService: FileserviceService) { }

  ngOnInit(): void {
  }

  onDefaultFileSelected(event: any) {
    this.defaultFile = event.target.files[0];
  }

  onAadharFileSelected(event: any) {
    this.aadharFile = event.target.files[0];
  }

  onPanCardFileSelected(event: any) {
    this.panCardFile = event.target.files[0];
  }

  onSalarySlipFileSelected(event: any) {
    this.salarySlipFile = event.target.files[0];
  }

  uploadDefault() {  
    if(this.defaultFile && this.defaultFile.type === 'application/pdf'){
      this.fileUploadService.uploadDefault(this.defaultFile).subscribe({
        next: (data: any) => {
          alert(data.message);
          console.log(data);
        },
        error: (error: any) => {
          console.error('Error:', error);
        }
      });
    }else {
      alert('Please select a PDF file.');
    }
  }

  uploadAadharById(id: number) {
    if(this.aadharFile && this.aadharFile.type === this.pdfCheck  || this.aadharFile.type === this.docsCheck){
      this.fileUploadService.uploadAadharCardById(id, this.aadharFile).subscribe({
        next: (data: any) => {
          alert(data.message);
          console.log(data);
        },
        error: (error: any) => {
          console.error('Error:', error);
        }
      });
    }else{
      alert("Please select pdf file")
    }
  }
  
  uploadPanCardById(id: number) {
    if(this.panCardFile && this.panCardFile.type === this.pdfCheck  || this.panCardFile.type === this.docsCheck){
      this.fileUploadService.uploadPanCardById(id, this.panCardFile).subscribe({
        next: (response: any) => {
          alert(response.message);
          console.log(response);
        },
        error: (error: any) => console.error('Error:', error)
      });
    }else{
      alert("Please select pdf file")
    } 
  }
 
  uploadSalarySlipById(id: number) {
    if(this.salarySlipFile && this.salarySlipFile.type === this.pdfCheck  || this.salarySlipFile.type === this.docsCheck){
      this.fileUploadService.uploadSalarySlipById(id, this.salarySlipFile).subscribe({
        next: (response: any) => {
          alert(response.message);
          console.log(response);
        },
        error: (error: any) => console.error('Error:', error)
      });
    }else{
      alert("Please select pdf/Docx file")
    }   
  }
  
  downloadFileById(id: any, differ: any){
    this.fileUploadService.downloadAadharById(id, differ).subscribe((response: Blob) => {
      const url = window.URL.createObjectURL(response);
      const a = document.createElement('a');
      a.href = url;
      console.log(response);
      if(response.type === 'application/msword'){  
        a.download = `${differ}_${id}.docx`; // Set the filename with the id, or adjust as needed
      }else if(response.type === 'application/pdf'){
        a.download = `${differ}_${id}.pdf`;
      }
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }

  
  downloadZipFileById(id: any) {
    this.fileUploadService.downloadZipFile(id).subscribe((response: Blob) => {
      const url = window.URL.createObjectURL(response);
      const a = document.createElement('a');
      a.href = url;
      a.download = `File_${id}.zip`; // Set the filename with the id, or adjust as needed
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }




}
