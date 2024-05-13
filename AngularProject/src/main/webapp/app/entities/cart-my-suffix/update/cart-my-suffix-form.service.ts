import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICartMySuffix, NewCartMySuffix } from '../cart-my-suffix.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICartMySuffix for edit and NewCartMySuffixFormGroupInput for create.
 */
type CartMySuffixFormGroupInput = ICartMySuffix | PartialWithRequiredKeyOf<NewCartMySuffix>;

type CartMySuffixFormDefaults = Pick<NewCartMySuffix, 'id'>;

type CartMySuffixFormGroupContent = {
  id: FormControl<ICartMySuffix['id'] | NewCartMySuffix['id']>;
  total: FormControl<ICartMySuffix['total']>;
  payment: FormControl<ICartMySuffix['payment']>;
};

export type CartMySuffixFormGroup = FormGroup<CartMySuffixFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CartMySuffixFormService {
  createCartMySuffixFormGroup(cart: CartMySuffixFormGroupInput = { id: null }): CartMySuffixFormGroup {
    const cartRawValue = {
      ...this.getFormDefaults(),
      ...cart,
    };
    return new FormGroup<CartMySuffixFormGroupContent>({
      id: new FormControl(
        { value: cartRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      total: new FormControl(cartRawValue.total),
      payment: new FormControl(cartRawValue.payment),
    });
  }

  getCartMySuffix(form: CartMySuffixFormGroup): ICartMySuffix | NewCartMySuffix {
    return form.getRawValue() as ICartMySuffix | NewCartMySuffix;
  }

  resetForm(form: CartMySuffixFormGroup, cart: CartMySuffixFormGroupInput): void {
    const cartRawValue = { ...this.getFormDefaults(), ...cart };
    form.reset(
      {
        ...cartRawValue,
        id: { value: cartRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CartMySuffixFormDefaults {
    return {
      id: null,
    };
  }
}
