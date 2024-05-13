import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPortfolioMySuffix, NewPortfolioMySuffix } from '../portfolio-my-suffix.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPortfolioMySuffix for edit and NewPortfolioMySuffixFormGroupInput for create.
 */
type PortfolioMySuffixFormGroupInput = IPortfolioMySuffix | PartialWithRequiredKeyOf<NewPortfolioMySuffix>;

type PortfolioMySuffixFormDefaults = Pick<NewPortfolioMySuffix, 'id'>;

type PortfolioMySuffixFormGroupContent = {
  id: FormControl<IPortfolioMySuffix['id'] | NewPortfolioMySuffix['id']>;
  date: FormControl<IPortfolioMySuffix['date']>;
  name: FormControl<IPortfolioMySuffix['name']>;
};

export type PortfolioMySuffixFormGroup = FormGroup<PortfolioMySuffixFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PortfolioMySuffixFormService {
  createPortfolioMySuffixFormGroup(portfolio: PortfolioMySuffixFormGroupInput = { id: null }): PortfolioMySuffixFormGroup {
    const portfolioRawValue = {
      ...this.getFormDefaults(),
      ...portfolio,
    };
    return new FormGroup<PortfolioMySuffixFormGroupContent>({
      id: new FormControl(
        { value: portfolioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      date: new FormControl(portfolioRawValue.date),
      name: new FormControl(portfolioRawValue.name),
    });
  }

  getPortfolioMySuffix(form: PortfolioMySuffixFormGroup): IPortfolioMySuffix | NewPortfolioMySuffix {
    return form.getRawValue() as IPortfolioMySuffix | NewPortfolioMySuffix;
  }

  resetForm(form: PortfolioMySuffixFormGroup, portfolio: PortfolioMySuffixFormGroupInput): void {
    const portfolioRawValue = { ...this.getFormDefaults(), ...portfolio };
    form.reset(
      {
        ...portfolioRawValue,
        id: { value: portfolioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PortfolioMySuffixFormDefaults {
    return {
      id: null,
    };
  }
}
