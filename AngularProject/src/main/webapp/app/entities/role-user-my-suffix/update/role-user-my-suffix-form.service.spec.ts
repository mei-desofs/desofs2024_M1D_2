import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../role-user-my-suffix.test-samples';

import { RoleUserMySuffixFormService } from './role-user-my-suffix-form.service';

describe('RoleUserMySuffix Form Service', () => {
  let service: RoleUserMySuffixFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RoleUserMySuffixFormService);
  });

  describe('Service methods', () => {
    describe('createRoleUserMySuffixFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRoleUserMySuffixFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userId: expect.any(Object),
            roleId: expect.any(Object),
          }),
        );
      });

      it('passing IRoleUserMySuffix should create a new form with FormGroup', () => {
        const formGroup = service.createRoleUserMySuffixFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userId: expect.any(Object),
            roleId: expect.any(Object),
          }),
        );
      });
    });

    describe('getRoleUserMySuffix', () => {
      it('should return NewRoleUserMySuffix for default RoleUserMySuffix initial value', () => {
        const formGroup = service.createRoleUserMySuffixFormGroup(sampleWithNewData);

        const roleUser = service.getRoleUserMySuffix(formGroup) as any;

        expect(roleUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewRoleUserMySuffix for empty RoleUserMySuffix initial value', () => {
        const formGroup = service.createRoleUserMySuffixFormGroup();

        const roleUser = service.getRoleUserMySuffix(formGroup) as any;

        expect(roleUser).toMatchObject({});
      });

      it('should return IRoleUserMySuffix', () => {
        const formGroup = service.createRoleUserMySuffixFormGroup(sampleWithRequiredData);

        const roleUser = service.getRoleUserMySuffix(formGroup) as any;

        expect(roleUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRoleUserMySuffix should not enable id FormControl', () => {
        const formGroup = service.createRoleUserMySuffixFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRoleUserMySuffix should disable id FormControl', () => {
        const formGroup = service.createRoleUserMySuffixFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
