import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../role-my-suffix.test-samples';

import { RoleMySuffixFormService } from './role-my-suffix-form.service';

describe('RoleMySuffix Form Service', () => {
  let service: RoleMySuffixFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RoleMySuffixFormService);
  });

  describe('Service methods', () => {
    describe('createRoleMySuffixFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRoleMySuffixFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nameRole: expect.any(Object),
          }),
        );
      });

      it('passing IRoleMySuffix should create a new form with FormGroup', () => {
        const formGroup = service.createRoleMySuffixFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nameRole: expect.any(Object),
          }),
        );
      });
    });

    describe('getRoleMySuffix', () => {
      it('should return NewRoleMySuffix for default RoleMySuffix initial value', () => {
        const formGroup = service.createRoleMySuffixFormGroup(sampleWithNewData);

        const role = service.getRoleMySuffix(formGroup) as any;

        expect(role).toMatchObject(sampleWithNewData);
      });

      it('should return NewRoleMySuffix for empty RoleMySuffix initial value', () => {
        const formGroup = service.createRoleMySuffixFormGroup();

        const role = service.getRoleMySuffix(formGroup) as any;

        expect(role).toMatchObject({});
      });

      it('should return IRoleMySuffix', () => {
        const formGroup = service.createRoleMySuffixFormGroup(sampleWithRequiredData);

        const role = service.getRoleMySuffix(formGroup) as any;

        expect(role).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRoleMySuffix should not enable id FormControl', () => {
        const formGroup = service.createRoleMySuffixFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRoleMySuffix should disable id FormControl', () => {
        const formGroup = service.createRoleMySuffixFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
