import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';
import { expect } from '@jest/globals';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientTestingModule],
      providers: [TeacherService]
    });

    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

    afterEach(() => {
      httpMock.verify();
    });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('detail', () => {
    it('should return a teacher id', () => {
      const teacherId = '123';
      const mockTeacher: Teacher = {
        id: 123,
        firstName: '',
        lastName: '',
        createdAt: new Date(),
        updatedAt: new Date()
      };

      service.detail(teacherId).subscribe((teacher: Teacher) => {
        expect(teacher).toEqual(mockTeacher);
      });

      const req = httpMock.expectOne(`${service['pathService']}/${teacherId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockTeacher);
    });
  }); 
});
