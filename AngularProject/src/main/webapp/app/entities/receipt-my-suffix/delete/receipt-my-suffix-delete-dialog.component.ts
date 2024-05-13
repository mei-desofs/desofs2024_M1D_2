import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReceiptMySuffix } from '../receipt-my-suffix.model';
import { ReceiptMySuffixService } from '../service/receipt-my-suffix.service';

@Component({
  standalone: true,
  templateUrl: './receipt-my-suffix-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReceiptMySuffixDeleteDialogComponent {
  receipt?: IReceiptMySuffix;

  protected receiptService = inject(ReceiptMySuffixService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.receiptService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
