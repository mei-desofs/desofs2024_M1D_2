import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PaymentMySuffixDetailComponent } from './payment-my-suffix-detail.component';

describe('PaymentMySuffix Management Detail Component', () => {
  let comp: PaymentMySuffixDetailComponent;
  let fixture: ComponentFixture<PaymentMySuffixDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PaymentMySuffixDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PaymentMySuffixDetailComponent,
              resolve: { payment: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PaymentMySuffixDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PaymentMySuffixDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load payment on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PaymentMySuffixDetailComponent);

      // THEN
      expect(instance.payment()).toEqual(expect.objectContaining({ id: 123 }));
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
