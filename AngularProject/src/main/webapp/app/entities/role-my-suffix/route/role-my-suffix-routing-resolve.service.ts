import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRoleMySuffix } from '../role-my-suffix.model';
import { RoleMySuffixService } from '../service/role-my-suffix.service';

const roleResolve = (route: ActivatedRouteSnapshot): Observable<null | IRoleMySuffix> => {
  const id = route.params['id'];
  if (id) {
    return inject(RoleMySuffixService)
      .find(id)
      .pipe(
        mergeMap((role: HttpResponse<IRoleMySuffix>) => {
          if (role.body) {
            return of(role.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default roleResolve;
