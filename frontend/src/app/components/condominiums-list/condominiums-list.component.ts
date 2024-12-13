import { Component, inject, OnInit } from '@angular/core';
import { Condominium } from '../../interface/condominium';
import { CondominiumService } from '../../service/condominium.service';
import { ApiResponse } from '../../interface/api-response';
import { HttpClientModule } from '@angular/common/http';
import { RouterLink, RouterOutlet } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { timestamp } from 'rxjs';

@Component({
  selector: 'app-condominiums-list',
  standalone: true,
  imports: [HttpClientModule, RouterOutlet, ReactiveFormsModule, RouterLink],
  templateUrl: './condominiums-list.component.html',
  styleUrl: './condominiums-list.component.css'
})
export class CondominiumsListComponent implements OnInit {
  condominiums: Condominium[] = [];
  errorMessage: string | null = null;

  addForm!: FormGroup;
  editForm!: FormGroup;

  deleteCondominium?: Condominium;

  constructor(private condominiumService: CondominiumService) {  }
  
  ngOnInit(): void {
    this.condominiumService.clearSelectedCondominium();

    this.createAddForm();
    this.createEditForm();
    this.getCondominiums();
  }

  private createAddForm() {
    this.addForm = new FormGroup({
      name: new FormControl(null, Validators.required),
      address: new FormControl(null, Validators.required),
      lastYearBalance: new FormControl(0, Validators.required)
    });
  }

  private createEditForm() {
    this.editForm = new FormGroup({
      id: new FormControl(null, Validators.required),
      name: new FormControl(null, Validators.required),
      address: new FormControl(null, Validators.required),
      lastYearBalance: new FormControl(0, Validators.required)
    });
  }

  private getCondominiums(): void {
    this.condominiumService.getCondominiums().subscribe(
      {
        next: (response: ApiResponse<Condominium[]>) => {
          if (response.statusCode === 200) {
            this.condominiums = response.data['condominiums'];
          } else {
            this.errorMessage = response.message || 'Unexpected error occurred';
          }
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Failed to load condominium';
        }
      });
  }

  public onOpenEditCondominiumModal(condominium: Condominium) {
    this.editForm.get('id')?.setValue(condominium.id);
    this.editForm.get('name')?.setValue(condominium.name);
    this.editForm.get('address')?.setValue(condominium.address);
    this.editForm.get('lastYearBalance')?.setValue(condominium.lastYearBalance)
  }

  public onAddCondominium(): void{
    document.getElementById('add-condominiumForm')?.click();
    
    this.condominiumService.addCondominium(this.addForm.value).subscribe(
      () => {
        this.createAddForm()
        this.getCondominiums();
      }
    );
  }


  public onUpdateCondominium(): void{
    document.getElementById('edit-condominiumForm')?.click();

    this.condominiumService.updateCondominium(this.editForm.value).subscribe(
      () => {
        this.createEditForm();
        this.getCondominiums();
      }
    );
  }

  public onOpenDeleteCondominiumModal(condominium: Condominium) {
    this.deleteCondominium = condominium;
  }

  public onDeleteCondominium(condominiumId: number | undefined = -1): void{
    document.getElementById('delete-condominiumForm')?.click();

    this.condominiumService.deleteCondominium(condominiumId).subscribe(
      () => {this.getCondominiums();}
    );
  }

  public exportToPDF(condominium: Condominium) {
    this.condominiumService.exportPDF(condominium.id).subscribe((pdf) => {
      const blob = new Blob([pdf], { type: 'application/pdf' });

      const data = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = data;

      let dateTime = new Date();
      link.download = `${dateTime.toLocaleString().split(',')[0]}_report.pdf`;
      link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

      setTimeout(function () {
        window.URL.revokeObjectURL(data);
        link.remove();
      }, 100);
    });
  }
}