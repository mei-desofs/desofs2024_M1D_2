import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPaymentMySuffix } from 'app/entities/payment-my-suffix/payment-my-suffix.model';
import { PaymentMySuffixService } from 'app/entities/payment-my-suffix/service/payment-my-suffix.service';
import { ICartMySuffix } from '../cart-my-suffix.model';
import { CartMySuffixService } from '../service/cart-my-suffix.service';
import { CartMySuffixFormService, CartMySuffixFormGroup } from './cart-my-suffix-form.service';

@Component({
  standalone: true,
  selector: 'jhi-cart-my-suffix-update',
  templateUrl: './cart-my-suffix-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CartMySuffixUpdateComponent implements OnInit {
  isSaving = false;
  cart: ICartMySuffix | null = null;

  paymentsCollection: IPaymentMySuffix[] = [];

  protected cartService = inject(CartMySuffixService);
  protected cartFormService = inject(CartMySuffixFormService);
  protected paymentService = inject(PaymentMySuffixService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CartMySuffixFormGroup = this.cartFormService.createCartMySuffixFormGroup();

  comparePaymentMySuffix = (o1: IPaymentMySuffix | null, o2: IPaymentMySuffix | null): boolean =>
    this.paymentService.comparePaymentMySuffix(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cart }) => {
      this.cart = cart;
      if (cart) {
        this.updateForm(cart);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cart = this.cartFormService.getCartMySuffix(this.editForm);
    if (cart.id !== null) {
      this.subscribeToSaveResponse(this.cartService.update(cart));
    } else {
      this.subscribeToSaveResponse(this.cartService.create(cart));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICartMySuffix>>): void {
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

  protected updateForm(cart: ICartMySuffix): void {
    this.cart = cart;
    this.cartFormService.resetForm(this.editForm, cart);

    this.paymentsCollection = this.paymentService.addPaymentMySuffixToCollectionIfMissing<IPaymentMySuffix>(
      this.paymentsCollection,
      cart.payment,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.paymentService
      .query({ filter: 'cart-is-null' })
      .pipe(map((res: HttpResponse<IPaymentMySuffix[]>) => res.body ?? []))
      .pipe(
        map((payments: IPaymentMySuffix[]) =>
          this.paymentService.addPaymentMySuffixToCollectionIfMissing<IPaymentMySuffix>(payments, this.cart?.payment),
        ),
      )
      .subscribe((payments: IPaymentMySuffix[]) => (this.paymentsCollection = payments));
  }
}
