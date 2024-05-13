import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../portfolio-my-suffix.test-samples';

import { PortfolioMySuffixFormService } from './portfolio-my-suffix-form.service';

describe('PortfolioMySuffix Form Service', () => {
  let service: PortfolioMySuffixFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PortfolioMySuffixFormService);
  });

  describe('Service methods', () => {
    describe('createPortfolioMySuffixFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPortfolioMySuffixFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing IPortfolioMySuffix should create a new form with FormGroup', () => {
        const formGroup = service.createPortfolioMySuffixFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getPortfolioMySuffix', () => {
      it('should return NewPortfolioMySuffix for default PortfolioMySuffix initial value', () => {
        const formGroup = service.createPortfolioMySuffixFormGroup(sampleWithNewData);

        const portfolio = service.getPortfolioMySuffix(formGroup) as any;

        expect(portfolio).toMatchObject(sampleWithNewData);
      });

      it('should return NewPortfolioMySuffix for empty PortfolioMySuffix initial value', () => {
        const formGroup = service.createPortfolioMySuffixFormGroup();

        const portfolio = service.getPortfolioMySuffix(formGroup) as any;

        expect(portfolio).toMatchObject({});
      });

      it('should return IPortfolioMySuffix', () => {
        const formGroup = service.createPortfolioMySuffixFormGroup(sampleWithRequiredData);

        const portfolio = service.getPortfolioMySuffix(formGroup) as any;

        expect(portfolio).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPortfolioMySuffix should not enable id FormControl', () => {
        const formGroup = service.createPortfolioMySuffixFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPortfolioMySuffix should disable id FormControl', () => {
        const formGroup = service.createPortfolioMySuffixFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
