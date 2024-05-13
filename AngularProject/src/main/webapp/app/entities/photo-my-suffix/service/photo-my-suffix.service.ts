import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPhotoMySuffix, NewPhotoMySuffix } from '../photo-my-suffix.model';

export type PartialUpdatePhotoMySuffix = Partial<IPhotoMySuffix> & Pick<IPhotoMySuffix, 'id'>;

type RestOf<T extends IPhotoMySuffix | NewPhotoMySuffix> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestPhotoMySuffix = RestOf<IPhotoMySuffix>;

export type NewRestPhotoMySuffix = RestOf<NewPhotoMySuffix>;

export type PartialUpdateRestPhotoMySuffix = RestOf<PartialUpdatePhotoMySuffix>;

export type EntityResponseType = HttpResponse<IPhotoMySuffix>;
export type EntityArrayResponseType = HttpResponse<IPhotoMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class PhotoMySuffixService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/photos');

  create(photo: NewPhotoMySuffix): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(photo);
    return this.http
      .post<RestPhotoMySuffix>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(photo: IPhotoMySuffix): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(photo);
    return this.http
      .put<RestPhotoMySuffix>(`${this.resourceUrl}/${this.getPhotoMySuffixIdentifier(photo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(photo: PartialUpdatePhotoMySuffix): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(photo);
    return this.http
      .patch<RestPhotoMySuffix>(`${this.resourceUrl}/${this.getPhotoMySuffixIdentifier(photo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPhotoMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPhotoMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPhotoMySuffixIdentifier(photo: Pick<IPhotoMySuffix, 'id'>): number {
    return photo.id;
  }

  comparePhotoMySuffix(o1: Pick<IPhotoMySuffix, 'id'> | null, o2: Pick<IPhotoMySuffix, 'id'> | null): boolean {
    return o1 && o2 ? this.getPhotoMySuffixIdentifier(o1) === this.getPhotoMySuffixIdentifier(o2) : o1 === o2;
  }

  addPhotoMySuffixToCollectionIfMissing<Type extends Pick<IPhotoMySuffix, 'id'>>(
    photoCollection: Type[],
    ...photosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const photos: Type[] = photosToCheck.filter(isPresent);
    if (photos.length > 0) {
      const photoCollectionIdentifiers = photoCollection.map(photoItem => this.getPhotoMySuffixIdentifier(photoItem));
      const photosToAdd = photos.filter(photoItem => {
        const photoIdentifier = this.getPhotoMySuffixIdentifier(photoItem);
        if (photoCollectionIdentifiers.includes(photoIdentifier)) {
          return false;
        }
        photoCollectionIdentifiers.push(photoIdentifier);
        return true;
      });
      return [...photosToAdd, ...photoCollection];
    }
    return photoCollection;
  }

  protected convertDateFromClient<T extends IPhotoMySuffix | NewPhotoMySuffix | PartialUpdatePhotoMySuffix>(photo: T): RestOf<T> {
    return {
      ...photo,
      date: photo.date?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPhotoMySuffix: RestPhotoMySuffix): IPhotoMySuffix {
    return {
      ...restPhotoMySuffix,
      date: restPhotoMySuffix.date ? dayjs(restPhotoMySuffix.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPhotoMySuffix>): HttpResponse<IPhotoMySuffix> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPhotoMySuffix[]>): HttpResponse<IPhotoMySuffix[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
