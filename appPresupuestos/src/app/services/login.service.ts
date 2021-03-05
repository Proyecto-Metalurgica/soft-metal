import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';



@Injectable({
  providedIn: 'root'
})
export class LoginService {
  ConversionBase64: string;

  constructor(private http: HttpClient) { }

  public URLservidor: String;
  //Si no encuentra URL en la cookie usara la siguiente URL
  public URLSecundaria: String = 'http://192.168.1.100:8080';
  // public URLSecundaria: String =  'http://localhost:8080';

  realizaLogin(usuario:String, contrasena:String){

    if(window.localStorage.URLservidor){
      this.URLservidor = window.localStorage.URLservidor;
    }else{
      
      this.URLservidor = this.URLSecundaria;
    }
    this.ConversionBase64 = btoa(usuario+":"+contrasena);

      let headers: HttpHeaders = new HttpHeaders();
      headers = headers.append('Accept', 'application/json;profile=urn:org.apache.isis/v1');
      headers = headers.append('Authorization', 'Basic '+this.ConversionBase64);

    const URL = this.URLservidor+'/restful/services/simple.OrdenTrabajoMenu/actions/listAllActive/invoke';

    return this.http.get<any>(URL, {headers: headers})  
 
  } 



}
