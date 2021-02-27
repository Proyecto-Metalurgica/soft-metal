import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-tab1',
  templateUrl: 'tab1.page.html',
  styleUrls: ['tab1.page.scss']
})
export class Tab1Page {

  constructor(private http: HttpClient, private router: Router) { }


  public resultadosArray: any = null;


  ngOnInit() {
    this.listarTodasLasOT();
  }

  listarTodasLasOT() {
    const httpOptions = {
      headers: new HttpHeaders({
        'Accept': 'application/json;profile=urn:org.apache.isis/v1',
        'Authorization': 'Basic c3ZlbjpwYXNz',
      })
    }
    const URL = 'http://localhost:8080/restful/services/simple.OrdenTrabajoMenu/actions/listAllActive/invoke';
    this.http.get(URL, httpOptions)
      .subscribe((resultados: Array<any>) => {
        var array = resultados;
        array.pop();
        this.resultadosArray = array;
        console.log(this.resultadosArray)
      });

  }

  goToOrdenTrabajo(id_ot) { 
    this.router.navigate(['/tabs/tab2', { idOT: id_ot }])
  }

  filterItemsOfType() {
    return this.resultadosArray.filter(resultado => resultado.title != null);
  }

}
