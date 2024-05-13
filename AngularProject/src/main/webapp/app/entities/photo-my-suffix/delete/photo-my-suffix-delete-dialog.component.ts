import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPhotoMySuffix } from '../photo-my-suffix.model';
import { PhotoMySuffixService } from '../service/photo-my-suffix.service';

@Component({
  standalone: true,
  templateUrl: './photo-my-suffix-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PhotoMySuffixDeleteDialogComponent {
  photo?: IPhotoMySuffix;

  protected photoService = inject(PhotoMySuffixService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.photoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
