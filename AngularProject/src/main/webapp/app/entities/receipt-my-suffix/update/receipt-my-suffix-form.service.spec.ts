import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../receipt-my-suffix.test-samples';

import { ReceiptMySuffixFormService } from './receipt-my-suffix-form.service';

describe('ReceiptMySuffix Form Service', () => {
  let service: ReceiptMySuffixFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReceiptMySuffixFormService);
  });

  describe('Service methods', () => {
    describe('createReceiptMySuffixFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReceiptMySuffixFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idPayment: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IReceiptMySuffix should create a new form with FormGroup', () => {
        const formGroup = service.createReceiptMySuffixFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idPayment: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getReceiptMySuffix', () => {
      it('should return NewReceiptMySuffix for default ReceiptMySuffix initial value', () => {
        const formGroup = service.createReceiptMySuffixFormGroup(sampleWithNewData);

        const receipt = service.getReceiptMySuffix(formGroup) as any;

        expect(receipt).toMatchObject(sampleWithNewData);
      });

      it('should return NewReceiptMySuffix for empty ReceiptMySuffix initial value', () => {
        const formGroup = service.createReceiptMySuffixFormGroup();

        const receipt = service.getReceiptMySuffix(formGroup) as any;

        expect(receipt).toMatchObject({});
      });

      it('should return IReceiptMySuffix', () => {
        const formGroup = service.createReceiptMySuffixFormGroup(sampleWithRequiredData);

        const receipt = service.getReceiptMySuffix(formGroup) as any;

        expect(receipt).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReceiptMySuffix should not enable id FormControl', () => {
        const formGroup = service.createReceiptMySuffixFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReceiptMySuffix should disable id FormControl', () => {
        const formGroup = service.createReceiptMySuffixFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
