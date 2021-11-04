import * as dayjs from 'dayjs';
import { ResStatus } from 'app/entities/enumerations/res-status.model';

export interface IReservation {
  id?: number;
  guestFullName?: string | null;
  initialDate?: dayjs.Dayjs | null;
  resStatus?: ResStatus | null;
  ship?: string | null;
  portFrom?: string | null;
  from?: dayjs.Dayjs | null;
  portTo?: string | null;
  to?: dayjs.Dayjs | null;
}

export class Reservation implements IReservation {
  constructor(
    public id?: number,
    public guestFullName?: string | null,
    public initialDate?: dayjs.Dayjs | null,
    public resStatus?: ResStatus | null,
    public ship?: string | null,
    public portFrom?: string | null,
    public from?: dayjs.Dayjs | null,
    public portTo?: string | null,
    public to?: dayjs.Dayjs | null
  ) {}
}

export function getReservationIdentifier(reservation: IReservation): number | undefined {
  return reservation.id;
}
