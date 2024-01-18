import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { jest } from '@jest/globals';
import { of } from 'rxjs';
import { DetailComponent } from './detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { By } from '@angular/platform-browser';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { TeacherService } from 'src/app/services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let router: Router;
  let snackbar: MatSnackBar;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  const session = {
    id: 1,
    name: 'Session 1',
    description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit,' +
      'sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.' +
      ' Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris' +
      ' nisi ut aliquip ex ea commodo consequat.',
    date: new Date('2024-05-01'),
    createdAt: new Date('2024-05-01'),
    updatedAt: new Date('2024-05-01'),
    teacher_id: 1,
    users: [1, 2],
  };

  const teacher = {
    id: 1,
    lastName: 'Mario',
    firstName: 'Mario',
    createdAt: new Date('2024-05-01'),
    updatedAt: new Date('2024-05-01'),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        ReactiveFormsModule,
        NoopAnimationsModule,
        MatSnackBarModule,
        MatCardModule,
        MatIconModule
      ],
      declarations: [DetailComponent],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();

    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    router = TestBed.inject(Router);
    snackbar = TestBed.inject(MatSnackBar);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    component.session = session;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return to sessions page', () => {
    const spy = jest.spyOn(window.history, 'back').mockImplementation(() => { });
    component.back();

    expect(spy).toHaveBeenCalled();
  });

  it('should have a delete button', () => {
    component.isAdmin = true;
    fixture.detectChanges();
    const buttons = fixture.debugElement.queryAll(By.css('button'));
    const deleteButton = buttons.find((button) =>
      button.nativeElement.textContent.includes('Delete')
    );
    expect(deleteButton).toBeTruthy();
  });

  it('should not have a delete button if isAdmin is false', () => {
    component.isAdmin = false;
    fixture.detectChanges();
    const buttons = fixture.debugElement.queryAll(By.css('button'));
    const deleteButton = buttons.find((button) =>
      button.nativeElement.textContent.includes('Delete')
    );
    expect(deleteButton).toBeFalsy();
  });

  it('should call the participate function', () => {
    component.isAdmin = false;
    component.isParticipate = false;
    fixture.detectChanges();

    const buttons = fixture.debugElement.queryAll(By.css('button'));
    const participateButton = buttons.find((button) =>
      button.nativeElement.textContent.includes('Participate')
    );

    const componentSpy = jest
      .spyOn(component, 'participate')
      .mockImplementation(() => { });
    participateButton!.nativeElement.click();

    expect(componentSpy).toHaveBeenCalled();
  });

  it('should call the unParticipate function', () => {
    component.isAdmin = false;
    component.isParticipate = true;
    fixture.detectChanges();

    const buttons = fixture.debugElement.queryAll(By.css('button'));
    const participateButton = buttons.find((button) =>
      button.nativeElement.textContent.includes('Do not participate')
    );

    const componentSpy = jest
      .spyOn(component, 'unParticipate')
      .mockImplementation(() => { });
    participateButton!.nativeElement.click();

    expect(componentSpy).toHaveBeenCalled();
  });

  it('should recover the session on participate', () => {
    component.session = undefined;
    jest
      .spyOn(sessionApiService, 'participate')
      .mockImplementation(() => of(undefined));
    const detailSpy = jest
      .spyOn(sessionApiService, 'detail')
      .mockImplementation(() => of(session));

    component.participate();
    expect(detailSpy).toHaveBeenCalled();
    expect(component.session).toEqual(session);
  });

  it('should recover the session on unParticipate', () => {
    component.session = undefined;

    jest
      .spyOn(sessionApiService, 'unParticipate')
      .mockImplementation(() => of(undefined));
    const detailSpy = jest
      .spyOn(sessionApiService, 'detail')
      .mockImplementation(() => of(session));

    component.unParticipate();
    expect(detailSpy).toHaveBeenCalled();
    expect(component.session).toEqual(session);
  });

  it('should get the teacher when the session is recovered', () => {
    jest
      .spyOn(sessionApiService, 'detail')
      .mockImplementation(() => of(session));
    jest.spyOn(teacherService, 'detail').mockImplementation(() => of(teacher));
    component.ngOnInit();
    expect(component.teacher).toEqual(teacher);
  });

  it('should open a snackbar and redirect to the sessions page on delete', () => {
    component.isAdmin = true;
    fixture.detectChanges();

    const buttons = fixture.debugElement.queryAll(By.css('button'));
    const deleteButton = buttons.find((button) =>
      button.nativeElement.textContent.includes('Delete')
    );

    const componentSpy = jest.spyOn(component, 'delete');
    const sessionApiSpy = jest
      .spyOn(sessionApiService, 'delete')
      .mockImplementation(() => of(true));
    const matSnackBarSpy = jest.spyOn(snackbar, 'open');
    const routerSpy = jest
      .spyOn(router, 'navigate')
      .mockImplementation(async () => true);

    deleteButton!.nativeElement.click();

    expect(componentSpy).toHaveBeenCalled();
    expect(sessionApiSpy).toHaveBeenCalled();
    expect(matSnackBarSpy).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);
  });

});

