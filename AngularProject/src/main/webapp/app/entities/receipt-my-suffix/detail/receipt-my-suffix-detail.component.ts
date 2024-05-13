import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IReceiptMySuffix } from '../receipt-my-suffix.model';

@Component({
  standalone: true,
  selector: 'jhi-receipt-my-suffix-detail',
  templateUrl: './receipt-my-suffix-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ReceiptMySuffixDetailComponent {
  receipt = input<IReceiptMySuffix | null>(null);

  previousState(): void {
    window.history.back();
  }
}
