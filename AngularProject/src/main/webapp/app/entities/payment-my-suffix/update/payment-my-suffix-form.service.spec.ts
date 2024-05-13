import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../payment-my-suffix.test-samples';

import { PaymentMySuffixFormService } from './payment-my-suffix-form.service';

describe('PaymentMySuffix Form Service', () => {
  let service: PaymentMySuffixFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaymentMySuffixFormService);
  });

  describe('Service methods', () => {
    describe('createPaymentMySuffixFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaymentMySuffixFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idCart: expect.any(Object),
            date: expect.any(Object),
            receipt: expect.any(Object),
          }),
        );
      });

      it('passing IPaymentMySuffix should create a new form with FormGroup', () => {
        const formGroup = service.createPaymentMySuffixFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idCart: expect.any(Object),
            date: expect.any(Object),
            receipt: expect.any(Object),
          }),
        );
      });
    });

    describe('getPaymentMySuffix', () => {
      it('should return NewPaymentMySuffix for default PaymentMySuffix initial value', () => {
        const formGroup = service.createPaymentMySuffixFormGroup(sampleWithNewData);

        const payment = service.getPaymentMySuffix(formGroup) as any;

        expect(payment).toMatchObject(sampleWithNewData);
      });

      it('should return NewPaymentMySuffix for empty PaymentMySuffix initial value', () => {
        const formGroup = service.createPaymentMySuffixFormGroup();

        const payment = service.getPaymentMySuffix(formGroup) as any;

        expect(payment).toMatchObject({});
      });

      it('should return IPaymentMySuffix', () => {
        const formGroup = service.createPaymentMySuffixFormGroup(sampleWithRequiredData);

        const payment = service.getPaymentMySuffix(formGroup) as any;

        expect(payment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPaymentMySuffix should not enable id FormControl', () => {
        const formGroup = service.createPaymentMySuffixFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPaymentMySuffix should disable id FormControl', () => {
        const formGroup = service.createPaymentMySuffixFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
