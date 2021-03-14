import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { ToastController } from '@ionic/angular';

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
  private autenticacion = '';
  
  public URLservidor: String;
  //Si no encuentra URL en la cookie usara la siguiente URL
  public URLSecundaria: String =  'https://heroku-otyp.herokuapp.com';
  
  constructor(private http: HttpClient, private activatedRoute: ActivatedRoute,public toastController: ToastController) {}
  
  ngOnInit() {
    if(window.localStorage.autenticacion){
      this.autenticacion = window.localStorage.autenticacion;
    }
    if(window.localStorage.URLservidor){
      this.URLservidor = window.localStorage.URLservidor;
    }else{ 
      this.URLservidor = this.URLSecundaria;
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
    const URL = this.URLservidor+'/restful/objects/simple.OrdenTrabajo/'+idOT;
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
    const URL = this.URLservidor+'/restful/objects/simple.OrdenTrabajo/'+idOT+'/collections/itemsOT';
    this.http.get(URL, httpOptions)
      .subscribe((resultados: Array<any>) => {
        var array = resultados;
        array.pop();
        this.listadoData = array;
      });
  }

  actualizarEstadoOT() {
    let idOT = this.idOT;
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json;profile=urn:org.apache.isis/v1',
        'Authorization': 'Basic '+this.autenticacion,
      })
    }
    const URL = this.URLservidor+'/restful/objects/simple.OrdenTrabajo/'+idOT+'/actions/actualizarEstadoOT/invoke';
    this.http.put(URL,{}, httpOptions)
      .subscribe((resultados : any) => {
        if(resultados){
           if(resultados.estadoOT === 'Ejecucion' && this.otData.estadoOT === 'Ejecucion'){
            this.faltaTerminarItemsToast();
          } 
          this.listarUnaOT(this.idOT);
        }
      });
  }

  async faltaTerminarItemsToast() {
    const toast = await this.toastController.create({
      message: 'Hay items sin Terminar, no se puede colocar la Orden de Trabajo como Terminado',
      duration: 3500
    });
    toast.present();
  }

  estadoEspera(idItem) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json;profile=urn:org.apache.isis/v1',
        'Authorization': 'Basic '+this.autenticacion,
      })
    }
    const URL = this.URLservidor+'/restful/objects/simple.ItemOT/'+idItem+'/actions/estadoEspera/invoke';
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
        'Authorization': 'Basic '+this.autenticacion,
      })
    }
    const URL = this.URLservidor+'/restful/objects/simple.ItemOT/'+idItem+'/actions/estadoEjecucion/invoke';
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
        'Authorization': 'Basic '+this.autenticacion,
      })
    }
    const URL = this.URLservidor+'/restful/objects/simple.ItemOT/'+idItem+'/actions/estadoTerminado/invoke';
    this.http.put(URL,{}, httpOptions)
      .subscribe((resultados) => {
        if(resultados){
          this.listadoItemsOT(this.idOT);
        }
      });
  }

}
