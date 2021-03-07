import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-tab3',
  templateUrl: 'tab3.page.html',
  styleUrls: ['tab3.page.scss']
})
export class Tab3Page {

  constructor(private http: HttpClient, private router: Router) { }


  public resultadosArray: any = null;
  private autenticacion = '';

  ngOnInit() {
    
  }
  ionViewWillEnter(){
    if(window.localStorage.autenticacion){
      this.autenticacion = window.localStorage.autenticacion;
    }
    this.listarTodasLasOT();
 }

  listarTodasLasOT() {
    const httpOptions = {
      headers: new HttpHeaders({
        'Accept': 'application/json;profile=urn:org.apache.isis/v1',
        'Authorization': 'Basic '+this.autenticacion,
      })
    }
    const URL = 'http://localhost:8080/restful/services/simple.OrdenTrabajoMenu/actions/listAllInactive/invoke';
    this.http.get(URL, httpOptions)
      .subscribe((resultados: Array<any>) => {
        var array = resultados;
        array.pop();
        this.resultadosArray = array;
        console.log(this.resultadosArray)
      });

  }

  goToOrdenTrabajo(id_ot) { 
    this.router.navigate(['/tabs/tab4', { idOT: id_ot }])
  }

  filterItemsOfType() {
    return this.resultadosArray.filter(resultado => resultado.title != null);
  }

}
