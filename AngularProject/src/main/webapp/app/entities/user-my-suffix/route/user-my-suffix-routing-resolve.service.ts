import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserMySuffix } from '../user-my-suffix.model';
import { UserMySuffixService } from '../service/user-my-suffix.service';

const userResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserMySuffix> => {
  const id = route.params['id'];
  if (id) {
    return inject(UserMySuffixService)
      .find(id)
      .pipe(
        mergeMap((user: HttpResponse<IUserMySuffix>) => {
          if (user.body) {
            return of(user.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default userResolve;
