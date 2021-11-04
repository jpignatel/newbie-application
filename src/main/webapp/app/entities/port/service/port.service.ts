import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPort, getPortIdentifier } from '../port.model';

export type EntityResponseType = HttpResponse<IPort>;
export type EntityArrayResponseType = HttpResponse<IPort[]>;

@Injectable({ providedIn: 'root' })
export class PortService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPort>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPort[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  addPortToCollectionIfMissing(portCollection: IPort[], ...portsToCheck: (IPort | null | undefined)[]): IPort[] {
    const ports: IPort[] = portsToCheck.filter(isPresent);
    if (ports.length > 0) {
      const portCollectionIdentifiers = portCollection.map(portItem => getPortIdentifier(portItem)!);
      const portsToAdd = ports.filter(portItem => {
        const portIdentifier = getPortIdentifier(portItem);
        if (portIdentifier == null || portCollectionIdentifiers.includes(portIdentifier)) {
          return false;
        }
        portCollectionIdentifiers.push(portIdentifier);
        return true;
      });
      return [...portsToAdd, ...portCollection];
    }
    return portCollection;
  }
}
