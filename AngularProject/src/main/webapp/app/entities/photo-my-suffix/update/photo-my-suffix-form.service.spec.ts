import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../photo-my-suffix.test-samples';

import { PhotoMySuffixFormService } from './photo-my-suffix-form.service';

describe('PhotoMySuffix Form Service', () => {
  let service: PhotoMySuffixFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PhotoMySuffixFormService);
  });

  describe('Service methods', () => {
    describe('createPhotoMySuffixFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPhotoMySuffixFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            photo: expect.any(Object),
            date: expect.any(Object),
            state: expect.any(Object),
            portfolio: expect.any(Object),
            cart: expect.any(Object),
          }),
        );
      });

      it('passing IPhotoMySuffix should create a new form with FormGroup', () => {
        const formGroup = service.createPhotoMySuffixFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            photo: expect.any(Object),
            date: expect.any(Object),
            state: expect.any(Object),
            portfolio: expect.any(Object),
            cart: expect.any(Object),
          }),
        );
      });
    });

    describe('getPhotoMySuffix', () => {
      it('should return NewPhotoMySuffix for default PhotoMySuffix initial value', () => {
        const formGroup = service.createPhotoMySuffixFormGroup(sampleWithNewData);

        const photo = service.getPhotoMySuffix(formGroup) as any;

        expect(photo).toMatchObject(sampleWithNewData);
      });

      it('should return NewPhotoMySuffix for empty PhotoMySuffix initial value', () => {
        const formGroup = service.createPhotoMySuffixFormGroup();

        const photo = service.getPhotoMySuffix(formGroup) as any;

        expect(photo).toMatchObject({});
      });

      it('should return IPhotoMySuffix', () => {
        const formGroup = service.createPhotoMySuffixFormGroup(sampleWithRequiredData);

        const photo = service.getPhotoMySuffix(formGroup) as any;

        expect(photo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPhotoMySuffix should not enable id FormControl', () => {
        const formGroup = service.createPhotoMySuffixFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPhotoMySuffix should disable id FormControl', () => {
        const formGroup = service.createPhotoMySuffixFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
