import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReceiptMySuffix, NewReceiptMySuffix } from '../receipt-my-suffix.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReceiptMySuffix for edit and NewReceiptMySuffixFormGroupInput for create.
 */
type ReceiptMySuffixFormGroupInput = IReceiptMySuffix | PartialWithRequiredKeyOf<NewReceiptMySuffix>;

type ReceiptMySuffixFormDefaults = Pick<NewReceiptMySuffix, 'id'>;

type ReceiptMySuffixFormGroupContent = {
  id: FormControl<IReceiptMySuffix['id'] | NewReceiptMySuffix['id']>;
  idPayment: FormControl<IReceiptMySuffix['idPayment']>;
  description: FormControl<IReceiptMySuffix['description']>;
};

export type ReceiptMySuffixFormGroup = FormGroup<ReceiptMySuffixFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReceiptMySuffixFormService {
  createReceiptMySuffixFormGroup(receipt: ReceiptMySuffixFormGroupInput = { id: null }): ReceiptMySuffixFormGroup {
    const receiptRawValue = {
      ...this.getFormDefaults(),
      ...receipt,
    };
    return new FormGroup<ReceiptMySuffixFormGroupContent>({
      id: new FormControl(
        { value: receiptRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      idPayment: new FormControl(receiptRawValue.idPayment),
      description: new FormControl(receiptRawValue.description),
    });
  }

  getReceiptMySuffix(form: ReceiptMySuffixFormGroup): IReceiptMySuffix | NewReceiptMySuffix {
    return form.getRawValue() as IReceiptMySuffix | NewReceiptMySuffix;
  }

  resetForm(form: ReceiptMySuffixFormGroup, receipt: ReceiptMySuffixFormGroupInput): void {
    const receiptRawValue = { ...this.getFormDefaults(), ...receipt };
    form.reset(
      {
        ...receiptRawValue,
        id: { value: receiptRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReceiptMySuffixFormDefaults {
    return {
      id: null,
    };
  }
}
