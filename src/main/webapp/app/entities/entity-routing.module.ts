import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'reservation',
        data: { pageTitle: 'newbieApp.reservation.home.title' },
        loadChildren: () => import('./reservation/reservation.module').then(m => m.ReservationModule),
      },
      {
        path: 'port',
        data: { pageTitle: 'newbieApp.port.home.title' },
        loadChildren: () => import('./port/port.module').then(m => m.PortModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
