import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPortfolioMySuffix } from '../portfolio-my-suffix.model';
import { PortfolioMySuffixService } from '../service/portfolio-my-suffix.service';
import { PortfolioMySuffixFormService, PortfolioMySuffixFormGroup } from './portfolio-my-suffix-form.service';

@Component({
  standalone: true,
  selector: 'jhi-portfolio-my-suffix-update',
  templateUrl: './portfolio-my-suffix-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PortfolioMySuffixUpdateComponent implements OnInit {
  isSaving = false;
  portfolio: IPortfolioMySuffix | null = null;

  protected portfolioService = inject(PortfolioMySuffixService);
  protected portfolioFormService = inject(PortfolioMySuffixFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PortfolioMySuffixFormGroup = this.portfolioFormService.createPortfolioMySuffixFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ portfolio }) => {
      this.portfolio = portfolio;
      if (portfolio) {
        this.updateForm(portfolio);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const portfolio = this.portfolioFormService.getPortfolioMySuffix(this.editForm);
    if (portfolio.id !== null) {
      this.subscribeToSaveResponse(this.portfolioService.update(portfolio));
    } else {
      this.subscribeToSaveResponse(this.portfolioService.create(portfolio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPortfolioMySuffix>>): void {
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

  protected updateForm(portfolio: IPortfolioMySuffix): void {
    this.portfolio = portfolio;
    this.portfolioFormService.resetForm(this.editForm, portfolio);
  }
}
