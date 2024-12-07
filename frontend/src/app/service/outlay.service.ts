import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Outlay } from '../interface/outlay';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class OutlayService {
  private apiServerUrl = `${environment.apiBaseUrl}/condominium`;

  constructor(private http: HttpClient) {}

  public getOutlays(condominiumId: number): Observable<Outlay[]> {
    return this.http.get<Outlay[]>(`${this.apiServerUrl}/${condominiumId}/outlay/list`);
  }

  public addOutlay(outlay: Outlay, condominiumId: number): Observable<Outlay> {
    return this.http.post<Outlay>(`${this.apiServerUrl}/${condominiumId}/outlay/save`, outlay);
  }

  public updateOutlay(outlay: Outlay, condominiumId: number): Observable<Outlay> {
    return this.http.put<Outlay>(`${this.apiServerUrl}/${condominiumId}/outlay/edit`, outlay);
  }

  public deleteOutlay(condominiumId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}/${condominiumId}/outlay/delete`);
  }
}
