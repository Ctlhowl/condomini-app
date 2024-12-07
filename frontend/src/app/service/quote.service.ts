import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Quote } from '../interface/quote';
import { environment } from '../../environments/environments';
import { ApiResponse } from '../interface/api-response';

@Injectable({
  providedIn: 'root'
})
export class QuoteService {
  private apiServerUrl = `${environment.apiBaseUrl}/condominium`;

  constructor(private http: HttpClient) {}

  public getQuotes(condominiumId: number): Observable<ApiResponse<Quote[]>> {
    return this.http.get<ApiResponse<Quote[]>>(`${this.apiServerUrl}/${condominiumId}/quote/list`);
  }

  public getQuoteByTableAndCondominium(condominiumId: number, tableId: number): Observable<ApiResponse<Quote>> {
    return this.http.get<ApiResponse<Quote>>(`${this.apiServerUrl}/${condominiumId}/quote/details`, {params: {tableId: tableId}});
  }

  public addQuote(quote: Quote, condominiumId: number): Observable<Quote> {
    return this.http.post<Quote>(`${this.apiServerUrl}/${condominiumId}/quote/save`, quote);
  }

  public updateQuote(quote: Quote, condominiumId: number): Observable<Quote> {
    return this.http.put<Quote>(`${this.apiServerUrl}/${condominiumId}/quote/edit`, quote);
  }

  public deleteQuote(condominiumId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}/${condominiumId}/quote/delete`);
  }
}
