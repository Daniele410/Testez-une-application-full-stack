import { TestBed } from "@angular/core/testing";
import { expect } from '@jest/globals';
import { AuthService } from "./auth.service";
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RegisterRequest } from '../interfaces/registerRequest.interface';

describe('AuthService', () => {
    let service: AuthService;
    let httpMock: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
            providers: [AuthService]
        });

        service = TestBed.inject(AuthService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
        httpMock.verify();
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });

    it('should user register', () => {
        const form: RegisterRequest = {
            firstName: 'Mario',
            lastName: 'Mario',
            email: 'mario@test.com',
            password: '12345'
        };

        service.register(form).subscribe((response) => {
            expect(response).toBeFalsy();
        });

        const request = httpMock.expectOne('api/auth/register');
        expect(request.request.body).toEqual(form);
        expect(request.request.method).toEqual('POST');
        request.flush({});
    });

    it('should user login', () => {
        const form = {
            email: 'mario@test.com',
            password: '12345',
        };

        service.login(form).subscribe((response) => {
            expect(response).toBeTruthy();
            expect(response).toHaveProperty('token');
        });

        const request = httpMock.expectOne('api/auth/login');
        expect(request.request.body).toEqual(form);
        expect(request.request.method).toEqual('POST');
        request.flush({ token: 'token' });
    });

    afterEach(() => {
        httpMock.verify();
    });
});