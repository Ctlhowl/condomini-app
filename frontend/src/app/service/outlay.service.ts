import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Outlay } from '../interface/outlay';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environments';
import { ApiResponse } from '../interface/api-response';

@Injectable({
  providedIn: 'root'
})
export class OutlayService {
  private apiServerUrl = `${environment.apiBaseUrl}/condominium`;

  constructor(private http: HttpClient) {}

  public getOutlays(condominiumId: number): Observable<ApiResponse<Outlay[]>> {
    return this.http.get<ApiResponse<Outlay[]>>(`${this.apiServerUrl}/${condominiumId}/outlay/list`);
  }

  public addOutlay(outlay: Outlay, condominiumId: number): Observable<ApiResponse<Outlay>> {
    return this.http.post<ApiResponse<Outlay>>(`${this.apiServerUrl}/${condominiumId}/outlay/save`, outlay);
  }

  public updateOutlay(outlay: Outlay, condominiumId: number): Observable<ApiResponse<Outlay>> {
    return this.http.put<ApiResponse<Outlay>>(`${this.apiServerUrl}/${condominiumId}/outlay/edit`, outlay);
  }

  public deleteOutlay(outlayId: number, condominiumId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}/${condominiumId}/outlay/delete`, {params: {id: outlayId}});
  }
}
