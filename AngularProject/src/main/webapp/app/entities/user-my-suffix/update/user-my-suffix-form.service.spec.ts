import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../user-my-suffix.test-samples';

import { UserMySuffixFormService } from './user-my-suffix-form.service';

describe('UserMySuffix Form Service', () => {
  let service: UserMySuffixFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserMySuffixFormService);
  });

  describe('Service methods', () => {
    describe('createUserMySuffixFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserMySuffixFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            email: expect.any(Object),
            password: expect.any(Object),
            address: expect.any(Object),
            contact: expect.any(Object),
            profilePhoto: expect.any(Object),
            portfolio: expect.any(Object),
          }),
        );
      });

      it('passing IUserMySuffix should create a new form with FormGroup', () => {
        const formGroup = service.createUserMySuffixFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            email: expect.any(Object),
            password: expect.any(Object),
            address: expect.any(Object),
            contact: expect.any(Object),
            profilePhoto: expect.any(Object),
            portfolio: expect.any(Object),
          }),
        );
      });
    });

    describe('getUserMySuffix', () => {
      it('should return NewUserMySuffix for default UserMySuffix initial value', () => {
        const formGroup = service.createUserMySuffixFormGroup(sampleWithNewData);

        const user = service.getUserMySuffix(formGroup) as any;

        expect(user).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserMySuffix for empty UserMySuffix initial value', () => {
        const formGroup = service.createUserMySuffixFormGroup();

        const user = service.getUserMySuffix(formGroup) as any;

        expect(user).toMatchObject({});
      });

      it('should return IUserMySuffix', () => {
        const formGroup = service.createUserMySuffixFormGroup(sampleWithRequiredData);

        const user = service.getUserMySuffix(formGroup) as any;

        expect(user).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserMySuffix should not enable id FormControl', () => {
        const formGroup = service.createUserMySuffixFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserMySuffix should disable id FormControl', () => {
        const formGroup = service.createUserMySuffixFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
