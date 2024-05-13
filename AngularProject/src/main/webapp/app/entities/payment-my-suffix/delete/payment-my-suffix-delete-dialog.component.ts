import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPaymentMySuffix } from '../payment-my-suffix.model';
import { PaymentMySuffixService } from '../service/payment-my-suffix.service';

@Component({
  standalone: true,
  templateUrl: './payment-my-suffix-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PaymentMySuffixDeleteDialogComponent {
  payment?: IPaymentMySuffix;

  protected paymentService = inject(PaymentMySuffixService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paymentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
