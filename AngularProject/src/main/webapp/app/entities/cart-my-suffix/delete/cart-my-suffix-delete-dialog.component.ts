import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICartMySuffix } from '../cart-my-suffix.model';
import { CartMySuffixService } from '../service/cart-my-suffix.service';

@Component({
  standalone: true,
  templateUrl: './cart-my-suffix-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CartMySuffixDeleteDialogComponent {
  cart?: ICartMySuffix;

  protected cartService = inject(CartMySuffixService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cartService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
