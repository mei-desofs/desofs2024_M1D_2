import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { RoleUserMySuffixDetailComponent } from './role-user-my-suffix-detail.component';

describe('RoleUserMySuffix Management Detail Component', () => {
  let comp: RoleUserMySuffixDetailComponent;
  let fixture: ComponentFixture<RoleUserMySuffixDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoleUserMySuffixDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RoleUserMySuffixDetailComponent,
              resolve: { roleUser: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RoleUserMySuffixDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RoleUserMySuffixDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load roleUser on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RoleUserMySuffixDetailComponent);

      // THEN
      expect(instance.roleUser()).toEqual(expect.objectContaining({ id: 123 }));
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
