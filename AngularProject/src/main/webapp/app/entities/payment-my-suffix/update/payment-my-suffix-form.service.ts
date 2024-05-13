import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPaymentMySuffix, NewPaymentMySuffix } from '../payment-my-suffix.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaymentMySuffix for edit and NewPaymentMySuffixFormGroupInput for create.
 */
type PaymentMySuffixFormGroupInput = IPaymentMySuffix | PartialWithRequiredKeyOf<NewPaymentMySuffix>;

type PaymentMySuffixFormDefaults = Pick<NewPaymentMySuffix, 'id'>;

type PaymentMySuffixFormGroupContent = {
  id: FormControl<IPaymentMySuffix['id'] | NewPaymentMySuffix['id']>;
  idCart: FormControl<IPaymentMySuffix['idCart']>;
  date: FormControl<IPaymentMySuffix['date']>;
  receipt: FormControl<IPaymentMySuffix['receipt']>;
};

export type PaymentMySuffixFormGroup = FormGroup<PaymentMySuffixFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaymentMySuffixFormService {
  createPaymentMySuffixFormGroup(payment: PaymentMySuffixFormGroupInput = { id: null }): PaymentMySuffixFormGroup {
    const paymentRawValue = {
      ...this.getFormDefaults(),
      ...payment,
    };
    return new FormGroup<PaymentMySuffixFormGroupContent>({
      id: new FormControl(
        { value: paymentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      idCart: new FormControl(paymentRawValue.idCart),
      date: new FormControl(paymentRawValue.date),
      receipt: new FormControl(paymentRawValue.receipt),
    });
  }

  getPaymentMySuffix(form: PaymentMySuffixFormGroup): IPaymentMySuffix | NewPaymentMySuffix {
    return form.getRawValue() as IPaymentMySuffix | NewPaymentMySuffix;
  }

  resetForm(form: PaymentMySuffixFormGroup, payment: PaymentMySuffixFormGroupInput): void {
    const paymentRawValue = { ...this.getFormDefaults(), ...payment };
    form.reset(
      {
        ...paymentRawValue,
        id: { value: paymentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PaymentMySuffixFormDefaults {
    return {
      id: null,
    };
  }
}
