import { Component, inject, signal, OnInit, NgZone } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { sortStateSignal, SortDirective, SortByDirective, type SortState, SortService } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPhotoMySuffix } from 'app/entities/photo-my-suffix/photo-my-suffix.model';
import { EntityArrayResponseType, RoleUserMySuffixService } from 'app/entities/role-user-my-suffix/service/role-user-my-suffix.service';
import { SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { DataUtils } from 'app/core/util/data-util.service';
import SharedModule from 'app/shared/shared.module';
import {FormsModule} from "@angular/forms";
import { IRoleUserMySuffix } from 'app/entities/role-user-my-suffix/role-user-my-suffix.model';
import { combineLatest, filter, Observable, Subscription, tap } from "rxjs";
import { RoleUserMySuffixDeleteDialogComponent } from 'app/entities/role-user-my-suffix/delete/role-user-my-suffix-delete-dialog.component';

@Component({
  standalone: true,
  selector: 'jhi-role-user-my-suffix',
  templateUrl: './browseUserRoles.component.html',
  imports: [
    SharedModule,
    RouterModule,
    FormsModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
  ],
})
export default class BrowseUserRolesComponent implements OnInit {
  subscription: Subscription | null = null;
  roleUsers?: IRoleUserMySuffix[];
  isLoading = false;

  sortState = sortStateSignal({});

  public router = inject(Router);
  protected roleUserService = inject(RoleUserMySuffixService);
  protected activatedRoute = inject(ActivatedRoute);
  protected sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  trackId = (_index: number, item: IRoleUserMySuffix): number => this.roleUserService.getRoleUserMySuffixIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (!this.roleUsers || this.roleUsers.length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  delete(roleUser: IRoleUserMySuffix): void {
    const modalRef = this.modalService.open(RoleUserMySuffixDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.roleUser = roleUser;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.roleUsers = this.refineData(dataFromBody);
  }

  protected refineData(data: IRoleUserMySuffix[]): IRoleUserMySuffix[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IRoleUserMySuffix[] | null): IRoleUserMySuffix[] {
    return data ?? [];
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    return this.roleUserService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(sortState: SortState): void {
    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }

}
