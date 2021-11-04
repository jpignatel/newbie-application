import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IReservation, Reservation } from '../reservation.model';
import { ReservationService } from '../service/reservation.service';
import { ResStatus } from 'app/entities/enumerations/res-status.model';

@Component({
  selector: 'jhi-reservation-update',
  templateUrl: './reservation-update.component.html',
})
export class ReservationUpdateComponent implements OnInit {
  isSaving = false;
  resStatusValues = Object.keys(ResStatus);

  editForm = this.fb.group({
    id: [],
    guestFullName: [],
    initialDate: [],
    resStatus: [],
    ship: [],
    portFrom: [],
    from: [],
    portTo: [],
    to: [],
  });

  constructor(protected reservationService: ReservationService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reservation }) => {
      if (reservation.id === undefined) {
        const today = dayjs().startOf('day');
        reservation.initialDate = today;
        reservation.from = today;
        reservation.to = today;
      }

      this.updateForm(reservation);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reservation = this.createFromForm();
    if (reservation.id !== undefined) {
      this.subscribeToSaveResponse(this.reservationService.update(reservation));
    } else {
      this.subscribeToSaveResponse(this.reservationService.create(reservation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReservation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(reservation: IReservation): void {
    this.editForm.patchValue({
      id: reservation.id,
      guestFullName: reservation.guestFullName,
      initialDate: reservation.initialDate ? reservation.initialDate.format(DATE_TIME_FORMAT) : null,
      resStatus: reservation.resStatus,
      ship: reservation.ship,
      portFrom: reservation.portFrom,
      from: reservation.from ? reservation.from.format(DATE_TIME_FORMAT) : null,
      portTo: reservation.portTo,
      to: reservation.to ? reservation.to.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IReservation {
    return {
      ...new Reservation(),
      id: this.editForm.get(['id'])!.value,
      guestFullName: this.editForm.get(['guestFullName'])!.value,
      initialDate: this.editForm.get(['initialDate'])!.value
        ? dayjs(this.editForm.get(['initialDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      resStatus: this.editForm.get(['resStatus'])!.value,
      ship: this.editForm.get(['ship'])!.value,
      portFrom: this.editForm.get(['portFrom'])!.value,
      from: this.editForm.get(['from'])!.value ? dayjs(this.editForm.get(['from'])!.value, DATE_TIME_FORMAT) : undefined,
      portTo: this.editForm.get(['portTo'])!.value,
      to: this.editForm.get(['to'])!.value ? dayjs(this.editForm.get(['to'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
