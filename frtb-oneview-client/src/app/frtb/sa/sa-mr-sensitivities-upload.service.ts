import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpHeaders, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as globals from 'src/app/globals';

@Injectable({
  providedIn: 'root'
})
export class SaMrSensitivitiesUploadService {

  constructor(
    private http: HttpClient,
  ) { }


  upload(file: File): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();

    formData.append('file', file);

    const req = new HttpRequest('POST', globals.samrFileUpload, formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  getFiles(): Observable<any> {
    return this.http.get(globals.samrFileUpload);
  }
}
