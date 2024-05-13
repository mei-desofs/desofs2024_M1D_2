import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRoleMySuffix, NewRoleMySuffix } from '../role-my-suffix.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRoleMySuffix for edit and NewRoleMySuffixFormGroupInput for create.
 */
type RoleMySuffixFormGroupInput = IRoleMySuffix | PartialWithRequiredKeyOf<NewRoleMySuffix>;

type RoleMySuffixFormDefaults = Pick<NewRoleMySuffix, 'id'>;

type RoleMySuffixFormGroupContent = {
  id: FormControl<IRoleMySuffix['id'] | NewRoleMySuffix['id']>;
  nameRole: FormControl<IRoleMySuffix['nameRole']>;
};

export type RoleMySuffixFormGroup = FormGroup<RoleMySuffixFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RoleMySuffixFormService {
  createRoleMySuffixFormGroup(role: RoleMySuffixFormGroupInput = { id: null }): RoleMySuffixFormGroup {
    const roleRawValue = {
      ...this.getFormDefaults(),
      ...role,
    };
    return new FormGroup<RoleMySuffixFormGroupContent>({
      id: new FormControl(
        { value: roleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nameRole: new FormControl(roleRawValue.nameRole),
    });
  }

  getRoleMySuffix(form: RoleMySuffixFormGroup): IRoleMySuffix | NewRoleMySuffix {
    return form.getRawValue() as IRoleMySuffix | NewRoleMySuffix;
  }

  resetForm(form: RoleMySuffixFormGroup, role: RoleMySuffixFormGroupInput): void {
    const roleRawValue = { ...this.getFormDefaults(), ...role };
    form.reset(
      {
        ...roleRawValue,
        id: { value: roleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RoleMySuffixFormDefaults {
    return {
      id: null,
    };
  }
}
