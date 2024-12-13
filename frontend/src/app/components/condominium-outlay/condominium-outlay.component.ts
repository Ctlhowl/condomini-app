import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { OutlayService } from '../../service/outlay.service';
import { CondominiumService } from '../../service/condominium.service';
import { ActivatedRoute, RouterOutlet } from '@angular/router';
import { Outlay } from '../../interface/outlay';
import { ApiResponse } from '../../interface/api-response';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TableService } from '../../service/table.service';
import { TableAppendix } from '../../interface/table-appendix';

@Component({
  selector: 'app-condominium-outlay',
  standalone: true,
  imports: [RouterOutlet, ReactiveFormsModule],
  templateUrl: './condominium-outlay.component.html',
  styleUrl: './condominium-outlay.component.css',
})
export class CondominiumOutlayComponent implements OnInit{
  outlays: Outlay[] = [];
  tables: TableAppendix[] = [];
  errorMessage: string | null = null;
  condominiumId!: number;

  selectedTable!: TableAppendix;

  addForm!: FormGroup;
  editForm!: FormGroup;

  deleteOutlay?: Outlay;

  constructor(private outlayService: OutlayService,
    private condominiumService: CondominiumService,
    private tableService: TableService,
    private route: ActivatedRoute) { }
  
  ngOnInit(): void {
    this.condominiumId = parseInt(this.route.snapshot.paramMap.get('id')!) 
    this.condominiumService.setSelectedCondominium(this.condominiumId); 
    
    this.createAddForm();
    this.createEditForm();
    this.getTable();
    this.getOutlay(); 
  }


  private createAddForm() {
    this.addForm = new FormGroup({
      description: new FormControl(null, Validators.required),
      amount: new FormControl(null, [Validators.required, Validators.min(0)]),
      operationType: new FormControl('ENTRATA', Validators.required),
      paymentMethod: new FormControl('BANCA', Validators.required),
      outlayType: new FormControl('ORDINARIA', Validators.required),
      table: new FormControl(null, Validators.required)
    });
  }

  private createEditForm() {
    this.editForm = new FormGroup({
      id: new FormControl(null, Validators.required),
      description: new FormControl(null, Validators.required),
      amount: new FormControl(null, [Validators.required, Validators.min(0)]),
      operationType: new FormControl('ENTRATA', Validators.required),
      paymentMethod: new FormControl('BANCA', Validators.required),
      outlayType: new FormControl('ORDINARIA', Validators.required),
      table: new FormControl(null, Validators.required)
    });
  }

  private getTable() {
    this.tableService.getTables().subscribe(
      {
        next: (response: ApiResponse<TableAppendix[]>) => {
          if (response.statusCode === 200) {
            this.tables = response.data['tables'];
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

  private getOutlay() {
    
    this.outlayService.getOutlays(this.condominiumId).subscribe(
      {
        next: (response: ApiResponse<Outlay[]>) => {
          if (response.statusCode === 200) {
            this.outlays = response.data['outlays'];
          } else {
            this.errorMessage = response.message || 'Unexpected error occurred';
          }
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Failed to load outlays';
        }
      }
    )
  }

  public onOpenEditOutlayModal(outlay: Outlay) {
    this.editForm.get('id')?.setValue(outlay.id);
    this.editForm.get('description')?.setValue(outlay.description);
    this.editForm.get('amount')?.setValue(outlay.amount);
    this.editForm.get('operationType')?.setValue(outlay.operationType);
    this.editForm.get('paymentMethod')?.setValue(outlay.paymentMethod);
    this.editForm.get('outlayType')?.setValue(outlay.outlayType);
    this.editForm.get('table')?.setValue(`${outlay.table.category} - ${outlay.table.description}`);
  }

  public onAddOutlay() {
    document.getElementById('add-outlayForm')?.click();
    
    this.outlayService.addOutlay(this.addForm.value, this.condominiumId).subscribe(
      () => {
        this.createAddForm();
        this.getOutlay();
      }
    );
  }

  public onUpdateOutlay(): void{
    let tab: TableAppendix[] = this.tables.filter((table) => {
      let str: string = `${table.category} - ${table.description}`
      if (str === this.editForm.get('table')?.value) {
        return table;
      }
      return null;
    });
    
    document.getElementById('edit-outlayForm')?.click();
    
    this.editForm.patchValue({
      table: tab[0]
    });
   
    this.outlayService.updateOutlay(this.editForm.value, this.condominiumId).subscribe(
      () => {
        this.createEditForm();
        this.getOutlay();
      }
    );
  }

  public onOpenDeleteOutlayModal(outlay: Outlay) {
    this.deleteOutlay = outlay;
  }

  public onDeleteOutlay(outlayId: number | undefined = -1): void{
    document.getElementById('delete-outlayForm')?.click();

    this.outlayService.deleteOutlay(outlayId, this.condominiumId).subscribe(
      () => {
        this.getOutlay();
      }
    );
  }

  public getTotalOutlay(): number{
    let total: number = 0;
    this.outlays.forEach((outlay) => {
      total += outlay.amount;
    });

    return total;
  }
 }

