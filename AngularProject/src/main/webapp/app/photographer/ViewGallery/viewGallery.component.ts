import { Component, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { combineLatest, filter, Observable, Subscription, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { sortStateSignal, SortDirective, SortByDirective, type SortState, SortService } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { FormsModule } from '@angular/forms';
import { SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { DataUtils } from 'app/core/util/data-util.service';
import { IPhotoMySuffix } from '../photo-my-suffix.model';
import { EntityArrayResponseType, PhotoMySuffixService } from '../service/photo-my-suffix.service';
import { PhotoMySuffixDeleteDialogComponent } from '../delete/photo-my-suffix-delete-dialog.component';

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './viewGallery.component.html',
  styleUrl: './viewGallery.component.scss',
   [imports: [SharedModule,
                        RouterModule,
                        FormsModule,
                        SortDirective,
                        SortByDirective,
                        DurationPipe,
                        FormatMediumDatetimePipe,
                        FormatMediumDatePipe],
})

export default class ViewGalleryComponent implements OnInit {

    subscription: Subscription | null = null;
    photos?: IPhotoMySuffix[];
    isLoading = false;

    sortState = sortStateSignal({});

    public router = inject(Router);
    protected photoService = inject(PhotoMySuffixService);
    protected activatedRoute = inject(ActivatedRoute);
    protected sortService = inject(SortService);
    protected dataUtils = inject(DataUtils);
    protected modalService = inject(NgbModal);
    protected ngZone = inject(NgZone);

    trackId = (_index: number, item: IPhotoMySuffix): number => this.photoService.getPhotoMySuffixIdentifier(item);

    ngOnInit(): void {
      this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
        .pipe(
          tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
          tap(() => {
            if (!this.photos || this.photos.length === 0) {
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
      this.photos = this.refineData(dataFromBody);
    }

    protected refineData(data: IPhotoMySuffix[]): IPhotoMySuffix[] {
      const { predicate, order } = this.sortState();
      return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
    }

    protected fillComponentAttributesFromResponseBody(data: IPhotoMySuffix[] | null): IPhotoMySuffix[] {
      return data ?? [];
    }

    protected queryBackend(): Observable<EntityArrayResponseType> {
      this.isLoading = true;
      const queryObject: any = {
        sort: this.sortService.buildSortParam(this.sortState()),
      };
      return this.photoService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
