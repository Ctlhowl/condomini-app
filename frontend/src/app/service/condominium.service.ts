import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Condominium } from '../interface/condominium';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../../environments/environments';
import { ApiResponse } from '../interface/api-response';

@Injectable({
  providedIn: 'root'
})
export class CondominiumService {
  private apiServerUrl = `${environment.apiBaseUrl}/condominium`;
  private selectedCondominiumIdSubject = new BehaviorSubject<number | null>(null);
  selectedCondominiumId$ = this.selectedCondominiumIdSubject.asObservable();

  constructor(private http: HttpClient ) {}
  
  public setSelectedCondominium(id: number): void {
    this.selectedCondominiumIdSubject.next(id);
  }

  public clearSelectedCondominium(): void {
    this.selectedCondominiumIdSubject.next(null);
  }

  public getCondominiums(): Observable<ApiResponse<Condominium[]>> {
    return this.http.get<ApiResponse<Condominium[]>>(`${this.apiServerUrl}/list`);
  }

  public getCondominium(condominiumId: number): Observable<ApiResponse<Condominium>> {
    return this.http.get<ApiResponse<Condominium>>(`${this.apiServerUrl}/details`, {params: {id: condominiumId}});
  }

  public addCondominium(condominium: Condominium): Observable<ApiResponse<Condominium>> {
      return this.http.post<ApiResponse<Condominium>>(`${this.apiServerUrl}/save`, condominium);
  }

  public updateCondominium(condominium: Condominium): Observable<ApiResponse<Condominium>> {
    return this.http.put<ApiResponse<Condominium>>(`${this.apiServerUrl}/edit`, condominium);
  }

  public deleteCondominium(condominiumId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}/delete`, { params: { id: condominiumId } });
  }
}
