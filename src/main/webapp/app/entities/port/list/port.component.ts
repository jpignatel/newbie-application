import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';

import { IPort } from '../port.model';
import { PortService } from '../service/port.service';

@Component({
  selector: 'jhi-port',
  templateUrl: './port.component.html',
})
export class PortComponent implements OnInit {
  ports?: IPort[];
  isLoading = false;

  constructor(protected portService: PortService) {}

  loadAll(): void {
    this.isLoading = true;

    this.portService.query().subscribe(
      (res: HttpResponse<IPort[]>) => {
        this.isLoading = false;
        this.ports = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPort): number {
    return item.id!;
  }
}
