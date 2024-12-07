import { Routes } from '@angular/router';
import { CondominiumsListComponent } from './components/condominiums-list/condominiums-list.component';
import { CondominiumDetailsComponent } from './components/condominium-details/condominium-details.component';
import { QuotesComponent } from './components/quotes/quotes.component';

export const routes: Routes = [
    { path: '', component: CondominiumsListComponent },
    { path: 'preventivi/:id', component: QuotesComponent}
];
    
export class AppRoutingModule {}
