import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRoleMySuffix } from '../role-my-suffix.model';
import { RoleMySuffixService } from '../service/role-my-suffix.service';

@Component({
  standalone: true,
  templateUrl: './role-my-suffix-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RoleMySuffixDeleteDialogComponent {
  role?: IRoleMySuffix;

  protected roleService = inject(RoleMySuffixService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.roleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
