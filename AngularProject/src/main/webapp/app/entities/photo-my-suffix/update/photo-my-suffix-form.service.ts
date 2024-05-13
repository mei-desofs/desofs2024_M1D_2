import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPhotoMySuffix, NewPhotoMySuffix } from '../photo-my-suffix.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPhotoMySuffix for edit and NewPhotoMySuffixFormGroupInput for create.
 */
type PhotoMySuffixFormGroupInput = IPhotoMySuffix | PartialWithRequiredKeyOf<NewPhotoMySuffix>;

type PhotoMySuffixFormDefaults = Pick<NewPhotoMySuffix, 'id'>;

type PhotoMySuffixFormGroupContent = {
  id: FormControl<IPhotoMySuffix['id'] | NewPhotoMySuffix['id']>;
  photo: FormControl<IPhotoMySuffix['photo']>;
  photoContentType: FormControl<IPhotoMySuffix['photoContentType']>;
  date: FormControl<IPhotoMySuffix['date']>;
  state: FormControl<IPhotoMySuffix['state']>;
  portfolio: FormControl<IPhotoMySuffix['portfolio']>;
  cart: FormControl<IPhotoMySuffix['cart']>;
};

export type PhotoMySuffixFormGroup = FormGroup<PhotoMySuffixFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PhotoMySuffixFormService {
  createPhotoMySuffixFormGroup(photo: PhotoMySuffixFormGroupInput = { id: null }): PhotoMySuffixFormGroup {
    const photoRawValue = {
      ...this.getFormDefaults(),
      ...photo,
    };
    return new FormGroup<PhotoMySuffixFormGroupContent>({
      id: new FormControl(
        { value: photoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      photo: new FormControl(photoRawValue.photo),
      photoContentType: new FormControl(photoRawValue.photoContentType),
      date: new FormControl(photoRawValue.date),
      state: new FormControl(photoRawValue.state),
      portfolio: new FormControl(photoRawValue.portfolio),
      cart: new FormControl(photoRawValue.cart),
    });
  }

  getPhotoMySuffix(form: PhotoMySuffixFormGroup): IPhotoMySuffix | NewPhotoMySuffix {
    return form.getRawValue() as IPhotoMySuffix | NewPhotoMySuffix;
  }

  resetForm(form: PhotoMySuffixFormGroup, photo: PhotoMySuffixFormGroupInput): void {
    const photoRawValue = { ...this.getFormDefaults(), ...photo };
    form.reset(
      {
        ...photoRawValue,
        id: { value: photoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PhotoMySuffixFormDefaults {
    return {
      id: null,
    };
  }
}
