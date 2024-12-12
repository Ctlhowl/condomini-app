import { Component, OnInit } from '@angular/core';
import { Apartment } from '../../interface/apartment';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink, RouterOutlet } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { ApartmentService } from '../../service/apartment.service';
import { CondominiumService } from '../../service/condominium.service';
import { ApiResponse } from '../../interface/api-response';
import { TableService } from '../../service/table.service';

@Component({
  selector: 'app-apartment-list',
  standalone: true,
  imports: [HttpClientModule, RouterOutlet, ReactiveFormsModule, RouterLink],
  templateUrl: './apartment-list.component.html',
  styleUrl: './apartment-list.component.css'
})
export class ApartmentListComponent implements OnInit{
  apartments: Apartment[] = [];
  totalTabA!: number;
  totalTabB!: number;
  totalTabC!: number;
  totalTabD!: number;

  errorMessage: string | null = null;
  condominiumId!: number;
  
  addForm!: FormGroup;
  editForm!: FormGroup;
  
  deleteApartment?: Apartment;

  constructor(private apartmentService: ApartmentService,
              private condominiumService: CondominiumService,
              private tableService: TableService,
              private route: ActivatedRoute) { }
  
  ngOnInit(): void {
    this.condominiumId = parseInt(this.route.snapshot.paramMap.get('id')!) 
    this.condominiumService.setSelectedCondominium(this.condominiumId); 

    this.createAddForm();
    this.createEditForm();
    this.getApartment();
    this.getTotalQuoteTable();
  }

  private createAddForm() {
      this.addForm = new FormGroup({
        owner: new FormControl(null, Validators.required),
        tenant: new FormControl(null),
        millTabA: new FormControl(0, [Validators.required, Validators.min(0), Validators.max(1000)]),
        millTabB: new FormControl(0, [Validators.required, Validators.min(0), Validators.max(1000)]),
        millTabC: new FormControl(0, [Validators.required, Validators.min(0), Validators.max(1000)]),
        millTabD: new FormControl(0, [Validators.required, Validators.min(0), Validators.max(1000)]),
        scala: new FormControl(null, Validators.required),
        lastYearBalance: new FormControl(0, Validators.required)
      });
    }
  
    private createEditForm() {
      this.editForm = new FormGroup({
        id: new FormControl(null, Validators.required),
        owner: new FormControl(null, Validators.required),
        tenant: new FormControl(null),
        millTabA: new FormControl(0, [Validators.required, Validators.min(0), Validators.max(1000)]),
        millTabB: new FormControl(0, [Validators.required, Validators.min(0), Validators.max(1000)]),
        millTabC: new FormControl(0, [Validators.required, Validators.min(0), Validators.max(1000)]),
        millTabD: new FormControl(0, [Validators.required, Validators.min(0), Validators.max(1000)]),
        scala: new FormControl(null, Validators.required),
        lastYearBalance: new FormControl(0, Validators.required)
      });
  }
  
  private getApartment() {
    this.apartmentService.getApartments(this.condominiumId).subscribe(
      {
         next: (response: ApiResponse<Apartment[]>) => {
            if (response.statusCode === 200) {
              this.apartments = response.data['apartments'];
            } else {
              this.errorMessage = response.message || 'Unexpected error occurred';
            }
          },
          error: (err) => {
            this.errorMessage = err.error?.message || 'Failed to load tables';
          }
      }
    )
  }

  private getTotalQuoteTable() {
    this.tableService.getTotalQuoteByCategory('A').subscribe(
      {
        next: (response: ApiResponse<number>) => {
          if (response.statusCode === 200) {
            this.totalTabA = response.data['totalQuote'];
          } else {
            this.errorMessage = response.message || 'Unexpected error occurred';
          }
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Failed to load tables';
        }
      }
    );

    this.tableService.getTotalQuoteByCategory('B').subscribe(
      {
        next: (response: ApiResponse<number>) => {
          if (response.statusCode === 200) {
            this.totalTabB = response.data['totalQuote'];
          } else {
            this.errorMessage = response.message || 'Unexpected error occurred';
          }
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Failed to load tables';
        }
      }
    );

    this.tableService.getTotalQuoteByCategory('C').subscribe(
      {
        next: (response: ApiResponse<number>) => {
          if (response.statusCode === 200) {
            this.totalTabC = response.data['totalQuote'];
          } else {
            this.errorMessage = response.message || 'Unexpected error occurred';
          }
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Failed to load tables';
        }
      }
    );

    this.tableService.getTotalQuoteByCategory('D').subscribe(
      {
        next: (response: ApiResponse<number>) => {
          if (response.statusCode === 200) {
            this.totalTabD = response.data['totalQuote'];
          } else {
            this.errorMessage = response.message || 'Unexpected error occurred';
          }
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Failed to load tables';
        }
      }
    );
  }

  public onOpenEditApartmentModal(apartment: Apartment) {
    this.editForm.get('id')?.setValue(apartment.id);
    this.editForm.get('owner')?.setValue(apartment.owner);
    this.editForm.get('tenant')?.setValue(apartment.tenant);
    this.editForm.get('millTabA')?.setValue(apartment.millTabA);
    this.editForm.get('millTabB')?.setValue(apartment.millTabB);
    this.editForm.get('millTabC')?.setValue(apartment.millTabC);
    this.editForm.get('millTabB')?.setValue(apartment.millTabD);
    this.editForm.get('scala')?.setValue(apartment.scala);
    this.editForm.get('lastYearBalance')?.setValue(apartment.lastYearBalance);
  }
  
    public onAddApartment() {
      document.getElementById('add-apartmentForm')?.click();

      this.apartmentService.addApartment(this.addForm.value, this.condominiumId).subscribe(
        () => {
          this.createAddForm();
          this.getApartment();
        }
      );
    }
  
    public onUpdateApartment(): void{
      document.getElementById('edit-apartmentForm')?.click();
  
      this.apartmentService.updateApartment(this.editForm.value, this.condominiumId).subscribe(
        () => {
          this.createEditForm();
          this.getApartment();
        }
      );
    }
  
    public onOpenDeleteApartmentModal(apartment: Apartment) {
      this.deleteApartment = apartment;
    }
  
    public onDeleteApartment(apartmentId: number | undefined = -1): void{
      document.getElementById('delete-apartmentForm')?.click();
  
      this.apartmentService.deleteApartment(apartmentId).subscribe(
        () => {
          const condominiumId: number = parseInt(this.route.snapshot.paramMap.get('id')!) 
          this.getApartment();
        }
      );
    }
}
