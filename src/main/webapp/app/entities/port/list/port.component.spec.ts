import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PortService } from '../service/port.service';

import { PortComponent } from './port.component';

describe('Port Management Component', () => {
  let comp: PortComponent;
  let fixture: ComponentFixture<PortComponent>;
  let service: PortService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PortComponent],
    })
      .overrideTemplate(PortComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PortComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PortService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.ports?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
