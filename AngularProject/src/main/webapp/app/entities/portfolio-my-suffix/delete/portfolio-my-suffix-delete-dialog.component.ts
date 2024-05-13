import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPortfolioMySuffix } from '../portfolio-my-suffix.model';
import { PortfolioMySuffixService } from '../service/portfolio-my-suffix.service';

@Component({
  standalone: true,
  templateUrl: './portfolio-my-suffix-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PortfolioMySuffixDeleteDialogComponent {
  portfolio?: IPortfolioMySuffix;

  protected portfolioService = inject(PortfolioMySuffixService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.portfolioService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
