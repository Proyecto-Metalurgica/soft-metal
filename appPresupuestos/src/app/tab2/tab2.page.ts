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
  param : any;
  
  constructor(private http: HttpClient, private activatedRoute: ActivatedRoute,) {}
  
  ngOnInit() {
    this.param = this.activatedRoute.snapshot.params;
    if (Object.keys(this.param).length) {
			this.listarUnaOT(this.param.idOT);
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
}
