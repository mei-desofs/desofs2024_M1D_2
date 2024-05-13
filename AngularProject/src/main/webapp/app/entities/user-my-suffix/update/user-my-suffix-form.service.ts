import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUserMySuffix, NewUserMySuffix } from '../user-my-suffix.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserMySuffix for edit and NewUserMySuffixFormGroupInput for create.
 */
type UserMySuffixFormGroupInput = IUserMySuffix | PartialWithRequiredKeyOf<NewUserMySuffix>;

type UserMySuffixFormDefaults = Pick<NewUserMySuffix, 'id'>;

type UserMySuffixFormGroupContent = {
  id: FormControl<IUserMySuffix['id'] | NewUserMySuffix['id']>;
  email: FormControl<IUserMySuffix['email']>;
  password: FormControl<IUserMySuffix['password']>;
  address: FormControl<IUserMySuffix['address']>;
  contact: FormControl<IUserMySuffix['contact']>;
  profilePhoto: FormControl<IUserMySuffix['profilePhoto']>;
  profilePhotoContentType: FormControl<IUserMySuffix['profilePhotoContentType']>;
  portfolio: FormControl<IUserMySuffix['portfolio']>;
};

export type UserMySuffixFormGroup = FormGroup<UserMySuffixFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserMySuffixFormService {
  createUserMySuffixFormGroup(user: UserMySuffixFormGroupInput = { id: null }): UserMySuffixFormGroup {
    const userRawValue = {
      ...this.getFormDefaults(),
      ...user,
    };
    return new FormGroup<UserMySuffixFormGroupContent>({
      id: new FormControl(
        { value: userRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      email: new FormControl(userRawValue.email),
      password: new FormControl(userRawValue.password),
      address: new FormControl(userRawValue.address),
      contact: new FormControl(userRawValue.contact),
      profilePhoto: new FormControl(userRawValue.profilePhoto),
      profilePhotoContentType: new FormControl(userRawValue.profilePhotoContentType),
      portfolio: new FormControl(userRawValue.portfolio),
    });
  }

  getUserMySuffix(form: UserMySuffixFormGroup): IUserMySuffix | NewUserMySuffix {
    return form.getRawValue() as IUserMySuffix | NewUserMySuffix;
  }

  resetForm(form: UserMySuffixFormGroup, user: UserMySuffixFormGroupInput): void {
    const userRawValue = { ...this.getFormDefaults(), ...user };
    form.reset(
      {
        ...userRawValue,
        id: { value: userRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserMySuffixFormDefaults {
    return {
      id: null,
    };
  }
}
