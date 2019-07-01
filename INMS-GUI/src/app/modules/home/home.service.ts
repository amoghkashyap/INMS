import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class HomeService {
  constructor(private http: HttpClient) {}
  public ip = 'http://0.0.0.0:9090';

  // get account list
  public getIngredients(): Observable<any> {
    console.log('hellooo');
    const url = this.ip + '/api-internal/v2/inms/INMS?containerId=inms';
    return this.http.get(url);
  }
  public getRecipe(params): Observable<any> {
    console.log('recipe');
    const url = this.ip + '/api-internal/v2/inms/recipes';
    return this.http.post(url, params);
  }
  public addIng(params): Observable<any> {
    console.log('ing');
    const url = this.ip + '/api-internal/v2/inms/INMS';
    return this.http.put(url, params);
  }
}
