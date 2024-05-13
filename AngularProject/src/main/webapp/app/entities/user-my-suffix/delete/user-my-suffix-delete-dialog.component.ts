import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserMySuffix } from '../user-my-suffix.model';
import { UserMySuffixService } from '../service/user-my-suffix.service';

@Component({
  standalone: true,
  templateUrl: './user-my-suffix-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserMySuffixDeleteDialogComponent {
  user?: IUserMySuffix;

  protected userService = inject(UserMySuffixService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
