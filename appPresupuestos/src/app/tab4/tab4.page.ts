import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { ToastController } from '@ionic/angular';

@Component({
  selector: 'app-tab4',
  templateUrl: 'tab4.page.html',
  styleUrls: ['tab4.page.scss']
})
export class Tab4Page {

  idOT;
  otData;
  listadoData;
  param : any;
  private autenticacion = '';
  
  constructor(private http: HttpClient, private activatedRoute: ActivatedRoute,public toastController: ToastController) {}
  
  ngOnInit() {
    if(window.localStorage.autenticacion){
      this.autenticacion = window.localStorage.autenticacion;
    }
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
        'Authorization': 'Basic '+this.autenticacion,
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
        'Authorization': 'Basic '+this.autenticacion,
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

}
