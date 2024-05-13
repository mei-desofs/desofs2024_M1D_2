import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRoleUserMySuffix } from '../role-user-my-suffix.model';
import { RoleUserMySuffixService } from '../service/role-user-my-suffix.service';

@Component({
  standalone: true,
  templateUrl: './role-user-my-suffix-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RoleUserMySuffixDeleteDialogComponent {
  roleUser?: IRoleUserMySuffix;

  protected roleUserService = inject(RoleUserMySuffixService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.roleUserService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
