import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPaymentMySuffix } from '../payment-my-suffix.model';

@Component({
  standalone: true,
  selector: 'jhi-payment-my-suffix-detail',
  templateUrl: './payment-my-suffix-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PaymentMySuffixDetailComponent {
  payment = input<IPaymentMySuffix | null>(null);

  previousState(): void {
    window.history.back();
  }
}
