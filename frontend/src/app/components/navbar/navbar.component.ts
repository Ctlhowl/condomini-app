import { Component, OnInit } from '@angular/core';
import { Condominium } from '../../interface/condominium';
import { CondominiumService } from '../../service/condominium.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit{
  selectedCondominiumId: number | null = null;

  constructor(private condominiumService: CondominiumService){}

  ngOnInit(): void {
    this.condominiumService.selectedCondominiumId$.subscribe((id) => {
      this.selectedCondominiumId = id;
    });
  }

  onClearCondominium(): void{
    this.condominiumService.clearSelectedCondominium();
  }
  
}
