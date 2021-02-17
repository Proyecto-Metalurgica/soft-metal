import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-login',
  templateUrl: 'login.page.html',
  styleUrls: ['login.page.scss']
})
export class LoginPage {

  idOT;
  otData;
  param : any;
  
  constructor(private http: HttpClient, private activatedRoute: ActivatedRoute, private router: Router) {}
  
  ngOnInit() {
    
  }

  goToTabs() { 
    this.router.navigate(['/tabs'])
  }

}
