import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TableAppendix } from '../interface/table-appendix';
import { environment } from '../../environments/environments';
import { ApiResponse } from '../interface/api-response';

@Injectable({
  providedIn: 'root'
})
export class TableService {
  private apiServerUrl = `${environment.apiBaseUrl}/table`;

  constructor(private http: HttpClient) {}

  public getTables(): Observable<ApiResponse<TableAppendix []>> {
    return this.http.get<ApiResponse<TableAppendix []>>(`${this.apiServerUrl}/list`);
  }

  public getTablesByCategory(categoty: string): Observable<ApiResponse<TableAppendix[]>> {
    return this.http.get<ApiResponse<TableAppendix[]>>(`${this.apiServerUrl}/details`, {params: {id: categoty}});
  }
}
