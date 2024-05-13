import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRoleMySuffix } from '../role-my-suffix.model';
import { RoleMySuffixService } from '../service/role-my-suffix.service';
import { RoleMySuffixFormService, RoleMySuffixFormGroup } from './role-my-suffix-form.service';

@Component({
  standalone: true,
  selector: 'jhi-role-my-suffix-update',
  templateUrl: './role-my-suffix-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RoleMySuffixUpdateComponent implements OnInit {
  isSaving = false;
  role: IRoleMySuffix | null = null;

  protected roleService = inject(RoleMySuffixService);
  protected roleFormService = inject(RoleMySuffixFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RoleMySuffixFormGroup = this.roleFormService.createRoleMySuffixFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ role }) => {
      this.role = role;
      if (role) {
        this.updateForm(role);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const role = this.roleFormService.getRoleMySuffix(this.editForm);
    if (role.id !== null) {
      this.subscribeToSaveResponse(this.roleService.update(role));
    } else {
      this.subscribeToSaveResponse(this.roleService.create(role));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoleMySuffix>>): void {
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

  protected updateForm(role: IRoleMySuffix): void {
    this.role = role;
    this.roleFormService.resetForm(this.editForm, role);
  }
}
