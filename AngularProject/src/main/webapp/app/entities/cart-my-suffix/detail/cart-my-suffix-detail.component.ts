import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ICartMySuffix } from '../cart-my-suffix.model';

@Component({
  standalone: true,
  selector: 'jhi-cart-my-suffix-detail',
  templateUrl: './cart-my-suffix-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CartMySuffixDetailComponent {
  cart = input<ICartMySuffix | null>(null);

  previousState(): void {
    window.history.back();
  }
}
