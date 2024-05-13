import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { UserMySuffixDetailComponent } from './user-my-suffix-detail.component';

describe('UserMySuffix Management Detail Component', () => {
  let comp: UserMySuffixDetailComponent;
  let fixture: ComponentFixture<UserMySuffixDetailComponent>;
  let dataUtils: DataUtils;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserMySuffixDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UserMySuffixDetailComponent,
              resolve: { user: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UserMySuffixDetailComponent, '')
      .compileComponents();
    dataUtils = TestBed.inject(DataUtils);
    jest.spyOn(window, 'open').mockImplementation(() => null);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserMySuffixDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load user on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UserMySuffixDetailComponent);

      // THEN
      expect(instance.user()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });

  describe('byteSize', () => {
    it('Should call byteSize from DataUtils', () => {
      // GIVEN
      jest.spyOn(dataUtils, 'byteSize');
      const fakeBase64 = 'fake base64';

      // WHEN
      comp.byteSize(fakeBase64);

      // THEN
      expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
    });
  });

  describe('openFile', () => {
    it('Should call openFile from DataUtils', () => {
      const newWindow = { ...window };
      newWindow.document.write = jest.fn();
      window.open = jest.fn(() => newWindow);
      window.onload = jest.fn(() => newWindow) as any;
      window.URL.createObjectURL = jest.fn() as any;
      // GIVEN
      jest.spyOn(dataUtils, 'openFile');
      const fakeContentType = 'fake content type';
      const fakeBase64 = 'fake base64';

      // WHEN
      comp.openFile(fakeBase64, fakeContentType);

      // THEN
      expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
    });
  });
});
