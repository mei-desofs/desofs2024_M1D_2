import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserMySuffix } from 'app/entities/user-my-suffix/user-my-suffix.model';
import { UserMySuffixService } from 'app/entities/user-my-suffix/service/user-my-suffix.service';
import { IRoleMySuffix } from 'app/entities/role-my-suffix/role-my-suffix.model';
import { RoleMySuffixService } from 'app/entities/role-my-suffix/service/role-my-suffix.service';
import { RoleUserMySuffixService } from '../service/role-user-my-suffix.service';
import { IRoleUserMySuffix } from '../role-user-my-suffix.model';
import { RoleUserMySuffixFormService, RoleUserMySuffixFormGroup } from './role-user-my-suffix-form.service';

@Component({
  standalone: true,
  selector: 'jhi-role-user-my-suffix-update',
  templateUrl: './role-user-my-suffix-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RoleUserMySuffixUpdateComponent implements OnInit {
  isSaving = false;
  roleUser: IRoleUserMySuffix | null = null;

  userIdsCollection: IUserMySuffix[] = [];
  roleIdsCollection: IRoleMySuffix[] = [];

  protected roleUserService = inject(RoleUserMySuffixService);
  protected roleUserFormService = inject(RoleUserMySuffixFormService);
  protected userService = inject(UserMySuffixService);
  protected roleService = inject(RoleMySuffixService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RoleUserMySuffixFormGroup = this.roleUserFormService.createRoleUserMySuffixFormGroup();

  compareUserMySuffix = (o1: IUserMySuffix | null, o2: IUserMySuffix | null): boolean => this.userService.compareUserMySuffix(o1, o2);

  compareRoleMySuffix = (o1: IRoleMySuffix | null, o2: IRoleMySuffix | null): boolean => this.roleService.compareRoleMySuffix(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roleUser }) => {
      this.roleUser = roleUser;
      if (roleUser) {
        this.updateForm(roleUser);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const roleUser = this.roleUserFormService.getRoleUserMySuffix(this.editForm);
    if (roleUser.id !== null) {
      this.subscribeToSaveResponse(this.roleUserService.update(roleUser));
    } else {
      this.subscribeToSaveResponse(this.roleUserService.create(roleUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoleUserMySuffix>>): void {
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

  protected updateForm(roleUser: IRoleUserMySuffix): void {
    this.roleUser = roleUser;
    this.roleUserFormService.resetForm(this.editForm, roleUser);

    this.userIdsCollection = this.userService.addUserMySuffixToCollectionIfMissing<IUserMySuffix>(this.userIdsCollection, roleUser.userId);
    this.roleIdsCollection = this.roleService.addRoleMySuffixToCollectionIfMissing<IRoleMySuffix>(this.roleIdsCollection, roleUser.roleId);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query({ filter: 'roleuser-is-null' })
      .pipe(map((res: HttpResponse<IUserMySuffix[]>) => res.body ?? []))
      .pipe(
        map((users: IUserMySuffix[]) => this.userService.addUserMySuffixToCollectionIfMissing<IUserMySuffix>(users, this.roleUser?.userId)),
      )
      .subscribe((users: IUserMySuffix[]) => (this.userIdsCollection = users));

    this.roleService
      .query({ filter: 'roleuser-is-null' })
      .pipe(map((res: HttpResponse<IRoleMySuffix[]>) => res.body ?? []))
      .pipe(
        map((roles: IRoleMySuffix[]) => this.roleService.addRoleMySuffixToCollectionIfMissing<IRoleMySuffix>(roles, this.roleUser?.roleId)),
      )
      .subscribe((roles: IRoleMySuffix[]) => (this.roleIdsCollection = roles));
  }
}
