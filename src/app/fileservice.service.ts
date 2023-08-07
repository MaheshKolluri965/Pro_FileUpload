import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileserviceService {
  private httpBaseUrl = 'http://localhost:9098/rest/api';

  constructor(private http: HttpClient) { }

  uploadDefault(document: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append("file", document)
    return this.http.post(this.httpBaseUrl + "/upload/default", formData);
  }


  uploadAadharCardById(id: number, aadharCard: File) {
    const formData: FormData = new FormData();
    formData.append('aadhar', aadharCard);

    return this.http.put(this.httpBaseUrl + "/upload/aadhar/" + id, formData);
  }

  // the double quotes are used to define parts of the string, 
  //and the backticks (template literals) are used to create a dynamic string with variable interpolation
  uploadPanCardById(id: number, panCard: File) {
    const formData: FormData = new FormData();
    formData.append('pancard', panCard);

    return this.http.put(this.httpBaseUrl + "/upload/pancard/" + id, formData);
  }

  
  uploadSalarySlipById(id: number, salarySlip: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('salaryslip', salarySlip);

    return this.http.put(`${this.httpBaseUrl}/upload/salaryslip/${id}`, formData);
  }

  downloadZipFile(id: any) {
    //const id = "MJ10";
    return this.http.get(`${this.httpBaseUrl}/download/${id}`, { responseType: 'blob' });
  }

  downloadAadharById(id: any, type: any){
    return this.http.get(`${this.httpBaseUrl}/download/${id}/${type}`, { responseType: 'blob' } );
  }

  //---------------------------------------------------------------


}
