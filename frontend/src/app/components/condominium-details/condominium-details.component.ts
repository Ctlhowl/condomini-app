import { Component, OnInit } from '@angular/core';
import { Condominium } from '../../interface/condominium';
import { CondominiumService } from '../../service/condominium.service';
import { ApiResponse } from '../../interface/api-response';
import { ActivatedRoute } from '@angular/router';



@Component({
  selector: 'app-condominium-details',
  standalone: true,
  imports: [],
  templateUrl: './condominium-details.component.html',
  styleUrl: './condominium-details.component.css'
})
export class CondominiumDetailsComponent implements OnInit{
  condominium!: Condominium;
  errorMessage: string | null = null;

  constructor(private condominiumService: CondominiumService, private route: ActivatedRoute) { }
  
  ngOnInit() {
    const condominiumId: number = parseInt(this.route.snapshot.paramMap.get('id')!) 
    
    this.getCondominium(condominiumId);
    this.condominiumService.setSelectedCondominium(condominiumId);  
  }

  private getCondominium(condominiumId: number): void {
    this.condominiumService.getCondominium(condominiumId).subscribe(
      {
        next: (response: ApiResponse<Condominium>) => {
          if (response.statusCode === 200) {
            this.condominium = response.data['condominiums'];
          } else {
            this.errorMessage = response.message || 'Unexpected error occurred';
          }
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Failed to load users';
        },
        complete: () => {
          console.log('Request completed');
        }
      });
  }
}
