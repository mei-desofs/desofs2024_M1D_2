import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../cart-my-suffix.test-samples';

import { CartMySuffixFormService } from './cart-my-suffix-form.service';

describe('CartMySuffix Form Service', () => {
  let service: CartMySuffixFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CartMySuffixFormService);
  });

  describe('Service methods', () => {
    describe('createCartMySuffixFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCartMySuffixFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            total: expect.any(Object),
            payment: expect.any(Object),
          }),
        );
      });

      it('passing ICartMySuffix should create a new form with FormGroup', () => {
        const formGroup = service.createCartMySuffixFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            total: expect.any(Object),
            payment: expect.any(Object),
          }),
        );
      });
    });

    describe('getCartMySuffix', () => {
      it('should return NewCartMySuffix for default CartMySuffix initial value', () => {
        const formGroup = service.createCartMySuffixFormGroup(sampleWithNewData);

        const cart = service.getCartMySuffix(formGroup) as any;

        expect(cart).toMatchObject(sampleWithNewData);
      });

      it('should return NewCartMySuffix for empty CartMySuffix initial value', () => {
        const formGroup = service.createCartMySuffixFormGroup();

        const cart = service.getCartMySuffix(formGroup) as any;

        expect(cart).toMatchObject({});
      });

      it('should return ICartMySuffix', () => {
        const formGroup = service.createCartMySuffixFormGroup(sampleWithRequiredData);

        const cart = service.getCartMySuffix(formGroup) as any;

        expect(cart).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICartMySuffix should not enable id FormControl', () => {
        const formGroup = service.createCartMySuffixFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCartMySuffix should disable id FormControl', () => {
        const formGroup = service.createCartMySuffixFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
