import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRoleUserMySuffix, NewRoleUserMySuffix } from '../role-user-my-suffix.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRoleUserMySuffix for edit and NewRoleUserMySuffixFormGroupInput for create.
 */
type RoleUserMySuffixFormGroupInput = IRoleUserMySuffix | PartialWithRequiredKeyOf<NewRoleUserMySuffix>;

type RoleUserMySuffixFormDefaults = Pick<NewRoleUserMySuffix, 'id'>;

type RoleUserMySuffixFormGroupContent = {
  id: FormControl<IRoleUserMySuffix['id'] | NewRoleUserMySuffix['id']>;
  userId: FormControl<IRoleUserMySuffix['userId']>;
  roleId: FormControl<IRoleUserMySuffix['roleId']>;
};

export type RoleUserMySuffixFormGroup = FormGroup<RoleUserMySuffixFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RoleUserMySuffixFormService {
  createRoleUserMySuffixFormGroup(roleUser: RoleUserMySuffixFormGroupInput = { id: null }): RoleUserMySuffixFormGroup {
    const roleUserRawValue = {
      ...this.getFormDefaults(),
      ...roleUser,
    };
    return new FormGroup<RoleUserMySuffixFormGroupContent>({
      id: new FormControl(
        { value: roleUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      userId: new FormControl(roleUserRawValue.userId),
      roleId: new FormControl(roleUserRawValue.roleId),
    });
  }

  getRoleUserMySuffix(form: RoleUserMySuffixFormGroup): IRoleUserMySuffix | NewRoleUserMySuffix {
    return form.getRawValue() as IRoleUserMySuffix | NewRoleUserMySuffix;
  }

  resetForm(form: RoleUserMySuffixFormGroup, roleUser: RoleUserMySuffixFormGroupInput): void {
    const roleUserRawValue = { ...this.getFormDefaults(), ...roleUser };
    form.reset(
      {
        ...roleUserRawValue,
        id: { value: roleUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RoleUserMySuffixFormDefaults {
    return {
      id: null,
    };
  }
}
