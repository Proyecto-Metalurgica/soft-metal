import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-tab2',
  templateUrl: 'tab2.page.html',
  styleUrls: ['tab2.page.scss']
})
export class Tab2Page {

  idOT;
  otData;
  listadoData;
  param : any;
  
  constructor(private http: HttpClient, private activatedRoute: ActivatedRoute,) {}
  
  ngOnInit() {
    this.param = this.activatedRoute.snapshot.params;
    if (Object.keys(this.param).length) {
			this.listarUnaOT(this.param.idOT);
      this.listadoItemsOT(this.param.idOT);
      this.idOT = this.param.idOT;
		}
  }

  listarUnaOT(idOT) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Accept': 'application/json;profile=urn:org.apache.isis/v1',
        'Authorization': 'Basic c3ZlbjpwYXNz',
      })
    }
    const URL = 'http://localhost:8080/restful/objects/simple.OrdenTrabajo/'+idOT;
    this.http.get(URL, httpOptions)
      .subscribe((resultados) => {
        this.otData = resultados;
      });
  }

  listadoItemsOT(idOT) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Accept': 'application/json;profile=urn:org.apache.isis/v1',
        'Authorization': 'Basic c3ZlbjpwYXNz',
      })
    }
    const URL = 'http://localhost:8080/restful/objects/simple.OrdenTrabajo/'+idOT+'/collections/itemsOT';
    this.http.get(URL, httpOptions)
      .subscribe((resultados: Array<any>) => {
        var array = resultados;
        array.pop();
        this.listadoData = array;
      });
  }

  estadoEspera(idItem) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json;profile=urn:org.apache.isis/v1',
        'Authorization': 'Basic c3ZlbjpwYXNz',
      })
    }
    const URL = 'http://localhost:8080/restful/objects/simple.ItemOT/'+idItem+'/actions/estadoEspera/invoke';
    this.http.put(URL,{}, httpOptions)
      .subscribe((resultados) => {
        if(resultados){
          this.listadoItemsOT(this.idOT);
        }
      });
  }

  estadoEjecucion(idItem) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json;profile=urn:org.apache.isis/v1',
        'Authorization': 'Basic c3ZlbjpwYXNz',
      })
    }
    const URL = 'http://localhost:8080/restful/objects/simple.ItemOT/'+idItem+'/actions/estadoEjecucion/invoke';
    this.http.put(URL,{}, httpOptions)
      .subscribe((resultados) => {
        if(resultados){
          this.listadoItemsOT(this.idOT);
        }
      });
  }

  estadoTerminado(idItem) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json;profile=urn:org.apache.isis/v1',
        'Authorization': 'Basic c3ZlbjpwYXNz',
      })
    }
    const URL = 'http://localhost:8080/restful/objects/simple.ItemOT/'+idItem+'/actions/estadoTerminado/invoke';
    this.http.put(URL,{}, httpOptions)
      .subscribe((resultados) => {
        if(resultados){
          this.listadoItemsOT(this.idOT);
        }
      });
  }

  
}
