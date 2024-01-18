import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { jest } from '@jest/globals';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let router: Router;
  let sessionService: SessionService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);
  });

  it('should verify input password type', () => {
    const imputPassword = fixture.debugElement.nativeElement.querySelector('[data-testId="password_for_login"]');
    expect(imputPassword.type).toBe("password");
  });

  it('should verify submit metode when click', ()  => {
    const submitSpy = jest.spyOn(component, 'submit');
    const submitButton = fixture.debugElement.nativeElement.querySelector('[data-testId="submit_for_login"]');
    submitButton.click();
    setTimeout(() => { 
    expect(submitSpy).toHaveBeenCalled(); }, 1000);
  });

  it('should submit login request and navigate on successful login', () => {
    const authService = TestBed.inject(AuthService);

    const navigateSpy = jest
      .spyOn(router, 'navigate')
      .mockImplementation(async () => true);

    const authSpy = jest
      .spyOn(authService, 'login')
      .mockImplementation(() => of({} as SessionInformation));

    jest.spyOn(sessionService, 'logIn').mockImplementation(() => {});
    component.submit();
    expect(authSpy).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should handle error on login', () => {
    const authService = TestBed.inject(AuthService);
    const loginSpy = jest
      .spyOn(authService, 'login')
      .mockImplementation(() => throwError(() => new Error('err')));

    component.submit();
    expect(loginSpy).toHaveBeenCalled();
    expect(component.onError).toBe(true);
  });

});
