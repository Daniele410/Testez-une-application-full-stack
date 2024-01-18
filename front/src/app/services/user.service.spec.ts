import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ],
      providers: [UserService]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it("should return a user id", () => {
    const userId = '123';
    const mockUser = {
      id: 123,
      firstName: '',
      lastName: '',
      createdAt: new Date(),
      updatedAt: new Date()
    };

    service.getById(userId).subscribe((user) => {
      expect(user).toEqual(mockUser);
    });

    const req = httpMock.expectOne(`${service['pathService']}/${userId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  });

  it("should delete a user", () => {
    const userId = '123';
    const mockUser = {
      id: 123,
      firstName: '',
      lastName: '',
      createdAt: new Date(),
      updatedAt: new Date()
    };

    service.delete(userId).subscribe((user) => {
      expect(user).toEqual(mockUser);
    });

    const req = httpMock.expectOne(`${service['pathService']}/${userId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush(mockUser);
  });

});
