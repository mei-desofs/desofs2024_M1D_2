import { Component, inject, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPortfolioMySuffix } from 'app/entities/portfolio-my-suffix/portfolio-my-suffix.model';
import { PortfolioMySuffixService } from 'app/entities/portfolio-my-suffix/service/portfolio-my-suffix.service';
import { UserMySuffixService } from '../service/user-my-suffix.service';
import { IUserMySuffix } from '../user-my-suffix.model';
import { UserMySuffixFormService, UserMySuffixFormGroup } from './user-my-suffix-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-my-suffix-update',
  templateUrl: './user-my-suffix-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserMySuffixUpdateComponent implements OnInit {
  isSaving = false;
  user: IUserMySuffix | null = null;

  portfoliosSharedCollection: IPortfolioMySuffix[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected userService = inject(UserMySuffixService);
  protected userFormService = inject(UserMySuffixFormService);
  protected portfolioService = inject(PortfolioMySuffixService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserMySuffixFormGroup = this.userFormService.createUserMySuffixFormGroup();

  comparePortfolioMySuffix = (o1: IPortfolioMySuffix | null, o2: IPortfolioMySuffix | null): boolean =>
    this.portfolioService.comparePortfolioMySuffix(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ user }) => {
      this.user = user;
      if (user) {
        this.updateForm(user);
      }

      this.loadRelationshipsOptions();
    });
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
    const user = this.userFormService.getUserMySuffix(this.editForm);
    if (user.id !== null) {
      this.subscribeToSaveResponse(this.userService.update(user));
    } else {
      this.subscribeToSaveResponse(this.userService.create(user));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserMySuffix>>): void {
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

  protected updateForm(user: IUserMySuffix): void {
    this.user = user;
    this.userFormService.resetForm(this.editForm, user);

    this.portfoliosSharedCollection = this.portfolioService.addPortfolioMySuffixToCollectionIfMissing<IPortfolioMySuffix>(
      this.portfoliosSharedCollection,
      user.portfolio,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.portfolioService
      .query()
      .pipe(map((res: HttpResponse<IPortfolioMySuffix[]>) => res.body ?? []))
      .pipe(
        map((portfolios: IPortfolioMySuffix[]) =>
          this.portfolioService.addPortfolioMySuffixToCollectionIfMissing<IPortfolioMySuffix>(portfolios, this.user?.portfolio),
        ),
      )
      .subscribe((portfolios: IPortfolioMySuffix[]) => (this.portfoliosSharedCollection = portfolios));
  }
}
