import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Apartment } from '../interface/apartment';
import { environment } from '../../environments/environments';
import { ApiResponse } from '../interface/api-response';

@Injectable({
  providedIn: 'root'
})
export class ApartmentService {
  private apiServerUrl = `${environment.apiBaseUrl}/condominium`;

  constructor(private http: HttpClient) {}

  public getApartments(condominiumId: number): Observable<ApiResponse<Apartment[]>> {
    return this.http.get<ApiResponse<Apartment[]>>(`${this.apiServerUrl}/${condominiumId}/apartment/list`);
  }

  public addApartment(apartment: Apartment, condominiumId: number): Observable<ApiResponse<Apartment>> {
    return this.http.post<ApiResponse<Apartment>>(`${this.apiServerUrl}/${condominiumId}/apartment/save`, apartment);
  }

  public updateApartment(apartment: Apartment, condominiumId: number): Observable<ApiResponse<Apartment>> {
    return this.http.put<ApiResponse<Apartment>>(`${this.apiServerUrl}/${condominiumId}/apartment/edit`, apartment);
  }

  public deleteApartment(condominiumId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}/${condominiumId}/apartment/delete`, {params: {id: condominiumId}});
  }
}
