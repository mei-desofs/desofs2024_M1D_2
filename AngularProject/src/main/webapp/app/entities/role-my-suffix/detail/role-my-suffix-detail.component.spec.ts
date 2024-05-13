import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { RoleMySuffixDetailComponent } from './role-my-suffix-detail.component';

describe('RoleMySuffix Management Detail Component', () => {
  let comp: RoleMySuffixDetailComponent;
  let fixture: ComponentFixture<RoleMySuffixDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoleMySuffixDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RoleMySuffixDetailComponent,
              resolve: { role: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RoleMySuffixDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RoleMySuffixDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load role on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RoleMySuffixDetailComponent);

      // THEN
      expect(instance.role()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
