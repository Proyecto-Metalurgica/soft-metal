import { Component } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Component({
  selector: 'app-tab1',
  templateUrl: 'tab1.page.html',
  styleUrls: ['tab1.page.scss']
})
export class Tab1Page {

  constructor(private http: HttpClient) {}


      public resultadosArray : any = null;;


       ngOnInit() {
        this.listarTodasLasOT();
      }

     listarTodasLasOT(){
           const httpOptions = {
             headers: new HttpHeaders({
               'Accept':  'application/json;profile=urn:org.apache.isis/v1',
               'Authorization': 'Basic c3ZlbjpwYXNz',
             })
           }
           const URL = 'http://localhost:8080/restful/services/simple.OrdenTrabajoMenu/actions/listAll/invoke';
           this.http.get(URL, httpOptions)
           .subscribe(resultados => (this.resultadosArray = resultados)
           );

         }

      filterItemsOfType(){
        return this.resultadosArray.filter(resultado => resultado.titulo != null);
      }

}
