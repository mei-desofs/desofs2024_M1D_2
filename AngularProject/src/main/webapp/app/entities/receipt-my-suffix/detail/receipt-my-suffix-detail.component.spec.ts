import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ReceiptMySuffixDetailComponent } from './receipt-my-suffix-detail.component';

describe('ReceiptMySuffix Management Detail Component', () => {
  let comp: ReceiptMySuffixDetailComponent;
  let fixture: ComponentFixture<ReceiptMySuffixDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReceiptMySuffixDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ReceiptMySuffixDetailComponent,
              resolve: { receipt: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ReceiptMySuffixDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReceiptMySuffixDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load receipt on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ReceiptMySuffixDetailComponent);

      // THEN
      expect(instance.receipt()).toEqual(expect.objectContaining({ id: 123 }));
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
