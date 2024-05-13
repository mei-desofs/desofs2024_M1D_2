import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IReceiptMySuffix } from 'app/entities/receipt-my-suffix/receipt-my-suffix.model';
import { ReceiptMySuffixService } from 'app/entities/receipt-my-suffix/service/receipt-my-suffix.service';
import { IPaymentMySuffix } from '../payment-my-suffix.model';
import { PaymentMySuffixService } from '../service/payment-my-suffix.service';
import { PaymentMySuffixFormService, PaymentMySuffixFormGroup } from './payment-my-suffix-form.service';

@Component({
  standalone: true,
  selector: 'jhi-payment-my-suffix-update',
  templateUrl: './payment-my-suffix-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PaymentMySuffixUpdateComponent implements OnInit {
  isSaving = false;
  payment: IPaymentMySuffix | null = null;

  receiptsCollection: IReceiptMySuffix[] = [];

  protected paymentService = inject(PaymentMySuffixService);
  protected paymentFormService = inject(PaymentMySuffixFormService);
  protected receiptService = inject(ReceiptMySuffixService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PaymentMySuffixFormGroup = this.paymentFormService.createPaymentMySuffixFormGroup();

  compareReceiptMySuffix = (o1: IReceiptMySuffix | null, o2: IReceiptMySuffix | null): boolean =>
    this.receiptService.compareReceiptMySuffix(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      this.payment = payment;
      if (payment) {
        this.updateForm(payment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const payment = this.paymentFormService.getPaymentMySuffix(this.editForm);
    if (payment.id !== null) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentMySuffix>>): void {
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

  protected updateForm(payment: IPaymentMySuffix): void {
    this.payment = payment;
    this.paymentFormService.resetForm(this.editForm, payment);

    this.receiptsCollection = this.receiptService.addReceiptMySuffixToCollectionIfMissing<IReceiptMySuffix>(
      this.receiptsCollection,
      payment.receipt,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.receiptService
      .query({ filter: 'payment-is-null' })
      .pipe(map((res: HttpResponse<IReceiptMySuffix[]>) => res.body ?? []))
      .pipe(
        map((receipts: IReceiptMySuffix[]) =>
          this.receiptService.addReceiptMySuffixToCollectionIfMissing<IReceiptMySuffix>(receipts, this.payment?.receipt),
        ),
      )
      .subscribe((receipts: IReceiptMySuffix[]) => (this.receiptsCollection = receipts));
  }
}
