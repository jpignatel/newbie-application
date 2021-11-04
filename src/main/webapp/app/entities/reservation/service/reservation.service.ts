import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReservation, getReservationIdentifier } from '../reservation.model';

export type EntityResponseType = HttpResponse<IReservation>;
export type EntityArrayResponseType = HttpResponse<IReservation[]>;

@Injectable({ providedIn: 'root' })
export class ReservationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reservations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(reservation: IReservation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reservation);
    return this.http
      .post<IReservation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(reservation: IReservation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reservation);
    return this.http
      .put<IReservation>(`${this.resourceUrl}/${getReservationIdentifier(reservation) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(reservation: IReservation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reservation);
    return this.http
      .patch<IReservation>(`${this.resourceUrl}/${getReservationIdentifier(reservation) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IReservation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReservation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addReservationToCollectionIfMissing(
    reservationCollection: IReservation[],
    ...reservationsToCheck: (IReservation | null | undefined)[]
  ): IReservation[] {
    const reservations: IReservation[] = reservationsToCheck.filter(isPresent);
    if (reservations.length > 0) {
      const reservationCollectionIdentifiers = reservationCollection.map(reservationItem => getReservationIdentifier(reservationItem)!);
      const reservationsToAdd = reservations.filter(reservationItem => {
        const reservationIdentifier = getReservationIdentifier(reservationItem);
        if (reservationIdentifier == null || reservationCollectionIdentifiers.includes(reservationIdentifier)) {
          return false;
        }
        reservationCollectionIdentifiers.push(reservationIdentifier);
        return true;
      });
      return [...reservationsToAdd, ...reservationCollection];
    }
    return reservationCollection;
  }

  protected convertDateFromClient(reservation: IReservation): IReservation {
    return Object.assign({}, reservation, {
      initialDate: reservation.initialDate?.isValid() ? reservation.initialDate.toJSON() : undefined,
      from: reservation.from?.isValid() ? reservation.from.toJSON() : undefined,
      to: reservation.to?.isValid() ? reservation.to.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.initialDate = res.body.initialDate ? dayjs(res.body.initialDate) : undefined;
      res.body.from = res.body.from ? dayjs(res.body.from) : undefined;
      res.body.to = res.body.to ? dayjs(res.body.to) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((reservation: IReservation) => {
        reservation.initialDate = reservation.initialDate ? dayjs(reservation.initialDate) : undefined;
        reservation.from = reservation.from ? dayjs(reservation.from) : undefined;
        reservation.to = reservation.to ? dayjs(reservation.to) : undefined;
      });
    }
    return res;
  }
}
