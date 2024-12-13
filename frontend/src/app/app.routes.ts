import { Routes } from '@angular/router';
import { CondominiumsListComponent } from './components/condominiums-list/condominiums-list.component';
import { QuotesComponent } from './components/quotes/quotes.component';
import { CondominiumOutlayComponent } from './components/condominium-outlay/condominium-outlay.component';
import { ApartmentListComponent } from './components/apartment-list/apartment-list.component';
import { ApartmentOutlayComponent } from './components/apartment-outlay/apartment-outlay.component';

export const routes: Routes = [
    { path: '', component: CondominiumsListComponent },
    { path: 'quotes/:id', component: QuotesComponent },
    { path: 'condominium/outlays/:id', component: CondominiumOutlayComponent },
    { path: 'condominium/apartments/:id', component: ApartmentListComponent },
    { path: 'condominium/apartments/:condominiumId/details/:id/owner/:owner', component: ApartmentOutlayComponent}
];
    
export class AppRoutingModule {}
