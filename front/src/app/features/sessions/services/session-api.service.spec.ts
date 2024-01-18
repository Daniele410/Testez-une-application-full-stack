import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionApiService } from './session-api.service';
import { Session } from "../interfaces/session.interface";


describe('SessionsService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  const sessions: Session[] = [
    {
      id: 1,
      name: 'Session test 1',
      description: 'Session 1 description',
      date: new Date(),
      teacher_id: 1,
      users: [1, 2],
      createdAt: new Date('2024-01-01'),
      updatedAt: new Date('2024-01-01'),
    },
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService],
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all sessions', () => {
    service.all().subscribe((response) => {
      expect(response).toBeTruthy();
      expect(response.length).toEqual(1);
      expect(response[0].name).toEqual('Session test 1');

    });

    const request = httpMock.expectOne('api/session');
    expect(request.request.method).toEqual('GET');

    request.flush(sessions);

  });


    it('should delete session', () => {
      service.delete('1').subscribe((response) => {
        expect(response).toBeUndefined();
      });
  
      const request = httpMock.expectOne('api/session/1');
      expect(request.request.method).toEqual('DELETE');
  
      request.flush({});
    });

    it('should create session', () => {
      const session: Session = {
        id: 1,
        name: 'Session test 1',
        description: 'Session 1 description',
        date: new Date(),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date('2024-01-01'),
        updatedAt: new Date('2024-01-01'),
      };
  
      service.create(session).subscribe((response) => {
        expect(response).toEqual(session);
      });
  
      const request = httpMock.expectOne('api/session');
      expect(request.request.method).toEqual('POST');
  
      request.flush(session);
    });

    it('should update session', () => {
      const session: Session = {
        id: 1,
        name: 'Session test 1',
        description: 'Session 1 description',
        date: new Date(),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date('2024-01-01'),
        updatedAt: new Date('2024-01-01'),
      };
  
      service.update('1', session).subscribe((response) => {
        expect(response).toEqual(session);
      });
  
      const request = httpMock.expectOne('api/session/1');
      expect(request.request.method).toEqual('PUT');
  
      request.flush(session);
    });

    it('should participate session', () => {
      service.participate('1', '1').subscribe((response) => {
        expect(response).toBeUndefined();
      });
  
      const request = httpMock.expectOne('api/session/1/participate/1');
      expect(request.request.method).toEqual('POST');
  
      request.flush({});
    });

    it('should unparticipate session', () => {
      service.unParticipate('1', '1').subscribe((response) => {
        expect(response).toBeUndefined();
      });
  
      const request = httpMock.expectOne('api/session/1/participate/1');
      expect(request.request.method).toEqual('DELETE');
  
      request.flush({});
    });

    it('should get session detail', () => {
      const session: Session = {
        id: 1,
        name: 'Session test 1',
        description: 'Session 1 description',
        date: new Date(),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date('2024-01-01'),
        updatedAt: new Date('2024-01-01'),
      };
  
      service.detail('1').subscribe((response) => {
        expect(response).toEqual(session);
      });
  
      const request = httpMock.expectOne('api/session/1');
      expect(request.request.method).toEqual('GET');
  
      request.flush(session);
    });

    afterEach(() => {
      httpMock.verify();
    });

});

