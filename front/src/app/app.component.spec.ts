import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { jest } from '@jest/globals';

import { AppComponent } from './app.component';
import { of } from 'rxjs';
import { SessionService } from './services/session.service';


describe('AppComponent', () => {

  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],providers : [ { provide : SessionService, useValue: {$isLogged: () => of(true),
        logOut: () => {},
            },
          }]
        }).compileComponents();

      fixture = TestBed.createComponent(AppComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it("should have a title", () => { 
    const title = fixture.nativeElement.querySelector('span.link');
    expect(title.textContent).toContain('Sessions');
  });

  it('should have a not logged header', () => {
    TestBed.inject(SessionService).$isLogged = () => of(false);

    fixture.detectChanges();
    const links = fixture.nativeElement.querySelectorAll('span.link');
    expect(links.length).toBe(2);
  });

  it('should have a logged header', () => {
    const links = fixture.nativeElement.querySelectorAll('span.link');
    expect(links.length).toBe(3);
  });

  it('should have a logout button', () => {
    const links = fixture.nativeElement.querySelectorAll('span.link');
    expect(links[2].textContent).toContain('Logout');
  });

  it('should have a sessions button', () => {
    const links = fixture.nativeElement.querySelectorAll('span.link');
    expect(links[0].textContent).toContain('Sessions');
  });

  it('should have a login button', () => {
    TestBed.inject(SessionService).$isLogged = () => of(false);

    fixture.detectChanges();
    const links = fixture.nativeElement.querySelectorAll('span.link');
    expect(links[0].textContent).toContain('Login');
  });

  it("should logout", () => {
    const spy = jest.spyOn(component, 'logout');
    const logOutSpan = fixture.nativeElement.querySelectorAll('span.link')[2];
    logOutSpan.click();
    expect(spy).toHaveBeenCalled();
  });

});
