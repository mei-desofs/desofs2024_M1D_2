import { Component, inject, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPortfolioMySuffix } from 'app/entities/portfolio-my-suffix/portfolio-my-suffix.model';
import { PortfolioMySuffixService } from 'app/entities/portfolio-my-suffix/service/portfolio-my-suffix.service';
import { ICartMySuffix } from 'app/entities/cart-my-suffix/cart-my-suffix.model';
import { CartMySuffixService } from 'app/entities/cart-my-suffix/service/cart-my-suffix.service';
import { PhotoState } from 'app/entities/enumerations/photo-state.model';
import { PhotoMySuffixService } from '../../entities/photo-my-suffix/service/photo-my-suffix.service';
import { IPhotoMySuffix } from '../../entities/photo-my-suffix/photo-my-suffix.model';
import { PhotoMySuffixFormService, PhotoMySuffixFormGroup } from '../../entities/photo-my-suffix/update/photo-my-suffix-form.service';
import dayjs from 'dayjs/esm';

@Component({
  standalone: true,
  selector: 'jhi-photo-my-suffix-update',
  templateUrl: './putUpforSale.component.html',
  styleUrl: './putUpforSale.component.css',

  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PutUpforSaleComponent implements OnInit {
  isSaving = false;
  photo: IPhotoMySuffix | null = null;
  photoStateValues = Object.keys(PhotoState);

  portfoliosSharedCollection: IPortfolioMySuffix[] = [];
  cartsSharedCollection: ICartMySuffix[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected photoService = inject(PhotoMySuffixService);
  protected photoFormService = inject(PhotoMySuffixFormService);
  protected portfolioService = inject(PortfolioMySuffixService);
  protected cartService = inject(CartMySuffixService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);
  private router = inject(Router);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PhotoMySuffixFormGroup = this.photoFormService.createPhotoMySuffixFormGroup();

  comparePortfolioMySuffix = (o1: IPortfolioMySuffix | null, o2: IPortfolioMySuffix | null): boolean =>
    this.portfolioService.comparePortfolioMySuffix(o1, o2);

  compareCartMySuffix = (o1: ICartMySuffix | null, o2: ICartMySuffix | null): boolean => this.cartService.compareCartMySuffix(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ photo }) => {
      this.photo = photo;
      if (photo) {
        this.updateForm(photo);
      }

      this.loadRelationshipsOptions();
    });
    this.editForm.valueChanges.subscribe(value => {});
  }
  getPhotoSize(base64String: string): number {
    return this.dataUtils.size(base64String);
  }
  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('photoStoreApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const photo = this.photoFormService.getPhotoMySuffix(this.editForm);
    photo.cart = null; // Set cart to null
    photo.date = dayjs(); // Set date to current date
    photo.state = PhotoState.ACTIVE; // Set state to ACTIVE
    if (photo.id !== null) {
      this.subscribeToSaveResponse(this.photoService.update(photo));
    } else {
      this.subscribeToSaveResponse(this.photoService.create(photo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPhotoMySuffix>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });

    // Log the response from the photo service
    result.subscribe(response => {});
  }

  protected onSaveSuccess(): void {
    this.router.navigate(['/']);
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(photo: IPhotoMySuffix): void {
    this.photo = photo;
    this.photoFormService.resetForm(this.editForm, photo);

    this.portfoliosSharedCollection = this.portfolioService.addPortfolioMySuffixToCollectionIfMissing<IPortfolioMySuffix>(
      this.portfoliosSharedCollection,
      photo.portfolio,
    );
    this.cartsSharedCollection = this.cartService.addCartMySuffixToCollectionIfMissing<ICartMySuffix>(
      this.cartsSharedCollection,
      photo.cart,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.portfolioService
      .query()
      .pipe(map((res: HttpResponse<IPortfolioMySuffix[]>) => res.body ?? []))
      .pipe(
        map((portfolios: IPortfolioMySuffix[]) =>
          this.portfolioService.addPortfolioMySuffixToCollectionIfMissing<IPortfolioMySuffix>(portfolios, this.photo?.portfolio),
        ),
      )
      .subscribe((portfolios: IPortfolioMySuffix[]) => (this.portfoliosSharedCollection = portfolios));

    this.cartService
      .query()
      .pipe(map((res: HttpResponse<ICartMySuffix[]>) => res.body ?? []))
      .pipe(map((carts: ICartMySuffix[]) => this.cartService.addCartMySuffixToCollectionIfMissing<ICartMySuffix>(carts, this.photo?.cart)))
      .subscribe((carts: ICartMySuffix[]) => (this.cartsSharedCollection = carts));
  }
}
