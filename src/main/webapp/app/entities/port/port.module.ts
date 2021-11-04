import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PortComponent } from './list/port.component';
import { PortDetailComponent } from './detail/port-detail.component';
import { PortRoutingModule } from './route/port-routing.module';

@NgModule({
  imports: [SharedModule, PortRoutingModule],
  declarations: [PortComponent, PortDetailComponent],
})
export class PortModule {}
