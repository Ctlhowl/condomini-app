import { Component } from '@angular/core';
import { TableAppendix } from '../../interface/table-appendix';
import { TableService } from '../../service/table.service';
import { ActivatedRoute, RouterOutlet } from '@angular/router';
import { CondominiumService } from '../../service/condominium.service';
import { ApiResponse } from '../../interface/api-response';
import { Quote } from '../../interface/quote';
import { QuoteService } from '../../service/quote.service';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-quotes',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './quotes.component.html',
  styleUrl: './quotes.component.css'
})
export class QuotesComponent {
  quotes!: Quote[];

  quotesA!: Quote[];
  quotesB!: Quote[];
  quotesC!: Quote[];
  quotesD!: Quote[];

  errorMessage: string | null = null;

  constructor(private tableService: TableService,
    private quoteService: QuoteService,
    private condominiumService: CondominiumService,
    private route: ActivatedRoute) { }
  
  ngOnInit() {
    const condominiumId: number = parseInt(this.route.snapshot.paramMap.get('id')!) 
    this.condominiumService.setSelectedCondominium(condominiumId); 

    this.getQuote(condominiumId); 
  }

  private getQuote(condominiumId: number) {
    this.quoteService.getQuotes(condominiumId).subscribe(
      {
        next: (response: ApiResponse<Quote[]>) => {
          if (response.statusCode === 200) {
            this.quotes = response.data['quotes'];
          }
          else {
            this.errorMessage = response.message || 'Unexpected error occurred';
          }
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Failed to load users';
        },
        complete: () => {
          this.quotesA = this.quotes.filter((quote) => quote.table.category === 'A');
          this.quotesB = this.quotes.filter((quote) => quote.table.category === 'B');
          this.quotesC = this.quotes.filter((quote) => quote.table.category === 'C');
          this.quotesD = this.quotes.filter((quote) => quote.table.category === 'D');

          console.log('Request quote completed');
        }
      });
  }

  public onSaveQuote() {
    const condominiumId: number = parseInt(this.route.snapshot.paramMap.get('id')!);

    this.quotesA.forEach(quote => {
      const amount = (<HTMLInputElement>document.getElementById(`quote_${quote.id}`)).value;
      quote.totalAmount = parseFloat(amount);
      
      this.quoteService.addQuote(quote, condominiumId).subscribe(
        () => { this.getQuote(condominiumId); }
      )
    });

    this.quotesB.forEach(quote => {
      const amount = (<HTMLInputElement>document.getElementById(`quote_${quote.id}`)).value;
      quote.totalAmount = parseFloat(amount);
      
      this.quoteService.addQuote(quote, condominiumId).subscribe(
        () => { this.getQuote(condominiumId); }
      )
    });


    this.quotesC.forEach(quote => {
      const amount = (<HTMLInputElement>document.getElementById(`quote_${quote.id}`)).value;
      quote.totalAmount = parseFloat(amount);
      
      this.quoteService.addQuote(quote, condominiumId).subscribe(
        () => { this.getQuote(condominiumId); }
      )
    });

    this.quotesD.forEach(quote => {
      const amount = (<HTMLInputElement>document.getElementById(`quote_${quote.id}`)).value;
      quote.totalAmount = parseFloat(amount);
      
      this.quoteService.addQuote(quote, condominiumId).subscribe(
        () => { this.getQuote(condominiumId); }
      )
    });
    
  }

  
}
