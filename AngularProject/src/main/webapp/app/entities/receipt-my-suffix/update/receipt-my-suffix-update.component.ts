import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IReceiptMySuffix } from '../receipt-my-suffix.model';
import { ReceiptMySuffixService } from '../service/receipt-my-suffix.service';
import { ReceiptMySuffixFormService, ReceiptMySuffixFormGroup } from './receipt-my-suffix-form.service';

@Component({
  standalone: true,
  selector: 'jhi-receipt-my-suffix-update',
  templateUrl: './receipt-my-suffix-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReceiptMySuffixUpdateComponent implements OnInit {
  isSaving = false;
  receipt: IReceiptMySuffix | null = null;

  protected receiptService = inject(ReceiptMySuffixService);
  protected receiptFormService = inject(ReceiptMySuffixFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReceiptMySuffixFormGroup = this.receiptFormService.createReceiptMySuffixFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ receipt }) => {
      this.receipt = receipt;
      if (receipt) {
        this.updateForm(receipt);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const receipt = this.receiptFormService.getReceiptMySuffix(this.editForm);
    if (receipt.id !== null) {
      this.subscribeToSaveResponse(this.receiptService.update(receipt));
    } else {
      this.subscribeToSaveResponse(this.receiptService.create(receipt));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReceiptMySuffix>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(receipt: IReceiptMySuffix): void {
    this.receipt = receipt;
    this.receiptFormService.resetForm(this.editForm, receipt);
  }
}
