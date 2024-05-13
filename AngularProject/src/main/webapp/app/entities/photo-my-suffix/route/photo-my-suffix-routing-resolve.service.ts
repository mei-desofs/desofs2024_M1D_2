import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPhotoMySuffix } from '../photo-my-suffix.model';
import { PhotoMySuffixService } from '../service/photo-my-suffix.service';

const photoResolve = (route: ActivatedRouteSnapshot): Observable<null | IPhotoMySuffix> => {
  const id = route.params['id'];
  if (id) {
    return inject(PhotoMySuffixService)
      .find(id)
      .pipe(
        mergeMap((photo: HttpResponse<IPhotoMySuffix>) => {
          if (photo.body) {
            return of(photo.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default photoResolve;
