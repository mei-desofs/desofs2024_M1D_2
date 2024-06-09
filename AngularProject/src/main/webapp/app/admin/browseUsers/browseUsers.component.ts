import { Component, inject, signal, OnInit, NgZone } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { sortStateSignal, SortDirective, SortByDirective, type SortState, SortService } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPhotoMySuffix } from 'app/entities/photo-my-suffix/photo-my-suffix.model';
import { EntityArrayResponseType, UserMySuffixService } from 'app/entities/user-my-suffix/service/user-my-suffix.service';
import { SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { DataUtils } from 'app/core/util/data-util.service';
import SharedModule from 'app/shared/shared.module';
import { FormsModule } from '@angular/forms';
import { IUserMySuffix } from 'app/entities/user-my-suffix/user-my-suffix.model';
import { combineLatest, filter, Observable, Subscription, tap } from 'rxjs';
import { UserMySuffixDeleteDialogComponent } from 'app/entities/user-my-suffix/delete/user-my-suffix-delete-dialog.component';

@Component({
  standalone: true,
  selector: 'jhi-user-my-suffix',
  templateUrl: './browseUsers.component.html',
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
export default class BrowseUsersComponent implements OnInit {
  subscription: Subscription | null = null;
  users?: IUserMySuffix[];
  isLoading = false;

  sortState = sortStateSignal({});

  public router = inject(Router);
  protected userService = inject(UserMySuffixService);
  protected activatedRoute = inject(ActivatedRoute);
  protected sortService = inject(SortService);
  protected dataUtils = inject(DataUtils);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  trackId = (_index: number, item: IUserMySuffix): number => this.userService.getUserMySuffixIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (!this.users || this.users.length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(user: IUserMySuffix): void {
    const modalRef = this.modalService.open(UserMySuffixDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.user = user;
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
    this.users = this.refineData(dataFromBody);
  }

  protected refineData(data: IUserMySuffix[]): IUserMySuffix[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IUserMySuffix[] | null): IUserMySuffix[] {
    return data ?? [];
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    return this.userService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
