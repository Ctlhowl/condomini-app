import { Routes } from '@angular/router';
import { CondominiumsListComponent } from './components/condominiums-list/condominiums-list.component';
import { QuotesComponent } from './components/quotes/quotes.component';
import { CondominiumOutlayComponent } from './components/condominium-outlay/condominium-outlay.component';

export const routes: Routes = [
    { path: '', component: CondominiumsListComponent },
    { path: 'quotes/:id', component: QuotesComponent },
    { path: 'condominium/outlay/:id', component: CondominiumOutlayComponent}
];
    
export class AppRoutingModule {}
